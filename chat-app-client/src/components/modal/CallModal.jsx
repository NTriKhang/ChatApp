import { Button, Image, Modal, Select, Switch } from "antd";
import { useEffect, useState } from "react";
import { getCurrentUserLocal } from "../../utils/LocalStorage";
const { Option } = Select;

const CallModal = ({
  currentCall,
  open,
  setOpen,
  isReceiving,
  stompClient,
  offer,
  answer,
  iceCandidate,
  shutdown,
  setShutdown,
}) => {
  const currentUser = getCurrentUserLocal();
  const [isCalling, setIsCalling] = useState(false);
  const [localStream, setLocalStream] = useState(null);
  const [remoteStream, setRemoteStream] = useState(null);
  let [peerConnection, setPeerConnection] = useState(null);
  const [isTrack, setIsTrack] = useState(false);
  const [confirmLoading, setConfirmLoading] = useState(false);
  const [iceCandidates, setIceCandidates] = useState([]);
  const [videoDevices, setVideoDevices] = useState([]);
  const [selectedDeviceId, setSelectedDeviceId] = useState(null);

  const stunServers = {
    iceServers: [
      {
        url: ["stun:stun.l.google.com:19302"],
      },
    ],
  };
  const handleOk = async () => {
    setConfirmLoading(true);

    if (offer) {
      //này là tạo peer connection từ phía người nhận, nên 'người nhận' trong hàm nghĩ là người gửi offer đi
      await createAnswer(offer.senderId);
      isReceiving = false;
    }
  };
  const handleCancel = () => {
    var receiver = {
      receiverId: currentCall.receiverId,
    };
    console.log(receiver);
    stompClient.send("/app/shutdown-call", {}, JSON.stringify(receiver));
    endCall();
    setOpen(false);
  };
  const createAnswer = async (receiverId) => {
    //set local stream
    // await init();
    //send ice from user-2 to user 1
    createPeerConnection(receiverId).then(async (peerConnectionTmp) => {
      await peerConnectionTmp.setRemoteDescription(offer.rtcDescription);

      var answerCreated = await peerConnectionTmp.createAnswer();
      await peerConnectionTmp.setLocalDescription(answerCreated);
      for (let candidate of iceCandidates) {
        try {
          console.log("add ice candidate --------------");
          await peerConnectionTmp.addIceCandidate(candidate);
        } catch (error) {
          console.error("Error adding ICE candidate", error);
        }
      }
      let userId2 = getCurrentUserLocal()["_id"];
      var answer = {
        rtcDescription: answerCreated,
        receiverId: receiverId,
        senderId: userId2,
      };
      //send answer from user-2 to user-1
      stompClient.send("/app/answer-private-call", {}, JSON.stringify(answer));
      setPeerConnection(peerConnectionTmp);
    });
  };
  const createOffer = async (receiverId, avatar) => {
    //in this function, it has sent iceCandidate to user-2
    createPeerConnection(receiverId).then(async (peerConnectionTmp) => {
      let offerCreated = await peerConnectionTmp.createOffer();
      await peerConnectionTmp.setLocalDescription(offerCreated);
      setPeerConnection(peerConnectionTmp);
      let userId2 = getCurrentUserLocal()["_id"];
      var offer = {
        rtcDescription: offerCreated,
        receiverId: receiverId,
        senderId: userId2,
        receiverImageUrl: avatar,
      };
      stompClient.send("/app/offer-private-call", {}, JSON.stringify(offer));
    });
  };

  const createPeerConnection = (receiverId) => {
    return new Promise(async (resolve, reject) => {
      try {
        await getCameras();

        if (peerConnection === null) {
          let localStream = await getMediaStream(selectedDeviceId);
          let peerConnectionTmp = new RTCPeerConnection(stunServers);
          let remoteStream = new MediaStream();

          document.getElementById("remoteAudio").srcObject = remoteStream;
          document.getElementById("localVideo").srcObject = localStream;
          document.getElementById("localVideo").muted = true;

          // console.log("--------------------------create peer connection --------------------")
          let localStreamTrack = localStream.getTracks();
          for (const track of localStreamTrack) {
            peerConnectionTmp.addTrack(track, localStream);
          }

          peerConnectionTmp.ontrack = (event) => {
            // if (remoteStream.srcObject) {
            //     return;
            // }
            event.streams[0].getTracks().forEach((track) => {
              remoteStream.addTrack(track);
            });
            setIsTrack(true);
          };

          peerConnectionTmp.onicecandidate = ({ candidate }) => {
            if (candidate) {
              let userId2 = getCurrentUserLocal()["_id"];
              var iceCandidate = {
                candidate: candidate,
                receiverId: currentCall.receiverId,
                senderId: userId2,
              };
              stompClient.send(
                "/app/private-call",
                {},
                JSON.stringify(iceCandidate)
              );
            }
            setLocalStream(localStream);
            setRemoteStream(remoteStream);
          };
          resolve(peerConnectionTmp);
        } else {
          resolve(peerConnection);
        }
      } catch (error) {
        reject(error);
      }
    });
  };
  const getMediaStream = async (deviceId) => {
    const constraints = {
      video: { deviceId: deviceId ? { exact: deviceId } : undefined },
      audio: true,
    };
    return await navigator.mediaDevices.getUserMedia(constraints);
  };
  const handleCameraChange = async (value) => {
    setSelectedDeviceId(value);
    if (localStream) {
      const newStream = await getMediaStream(value);
      const videoTrack = newStream.getVideoTracks()[0];
      const sender = peerConnection
        .getSenders()
        .find((s) => s.track.kind === videoTrack.kind);
      sender.replaceTrack(videoTrack);

      localStream.getTracks().forEach((track) => track.stop());
      setLocalStream(newStream);
      document.getElementById("localVideo").srcObject = newStream;
    }
  };
  const getCameras = async () => {
    const devices = await navigator.mediaDevices.enumerateDevices();
    const videoDevices = devices.filter(
      (device) => device.kind === "videoinput"
    );
    setVideoDevices(videoDevices);
    if (videoDevices.length > 0) {
      setSelectedDeviceId(videoDevices[0].deviceId);
    }
    console.log(videoDevices);
  };
  const endCall = async () => {
    if (peerConnection) {
      peerConnection.close();
      setPeerConnection(null);
    }

    if (localStream) {
      localStream.getTracks().forEach((track) => track.stop());
      setLocalStream(null);
    }

    if (remoteStream) {
      remoteStream.getTracks().forEach((track) => track.stop());
      setRemoteStream(null);
    }

    setIsCalling(false);
    setIsTrack(false);
    setIceCandidates([]);
    setVideoDevices([]);
    setSelectedDeviceId(null);
    document.getElementById("remoteAudio").srcObject = null;
    document.getElementById("localVideo").srcObject = null;
  };
  const toggleCamera = () => {
    if (localStream) {
      const videoTrack = localStream.getVideoTracks()[0];
      videoTrack.enabled = !videoTrack.enabled;
    }
  };
  const toggleAudio = () => {
    if (localStream) {
      const audioTrack = localStream.getAudioTracks()[0];
      audioTrack.enabled = !audioTrack.enabled;
    }
  };
  useEffect(() => {
    if (!isCalling && currentCall && isReceiving === false) {
      console.log("create offer");
      createOffer(currentCall.receiverId, currentUser.Image_path);
      //document.getElementById("user-1").srcObject

      setIsCalling(true);
    }
  }, [currentCall]);

  useEffect(() => {
    if (offer !== null && isReceiving === true) {
      setOpen(true);
      setIsCalling(true);
      //console.log('akfjbwjuhfewineaoifewiuyveiuyd', offer)
    }
  }, [offer]);
  useEffect(() => {
    if (iceCandidate !== null) {
      setIceCandidates((prevCandidates) => [
        ...prevCandidates,
        iceCandidate.candidate,
      ]);
    }
  }, [iceCandidate]);
  useEffect(() => {
    console.log(answer);
    if (answer !== null) {
      peerConnection.setRemoteDescription(answer.rtcDescription);

      setTimeout(() => {
        for (let candidate of iceCandidates) {
          try {
            if (peerConnection && peerConnection.signalingState !== "closed") {
              try {
                peerConnection.addIceCandidate(candidate);
              } catch (error) {
                console.error("Error adding ICE candidate:", error);
              }
            }
          } catch (error) {
            console.error("Error adding ICE candidate", error);
          }
        }
        console.log(peerConnection);
      }, 3000);
    }
  }, [answer]);
  useEffect(() => {
    if (shutdown === true) {
      setShutdown(false);
      endCall();
      setOpen(false);
    }
  }, [shutdown]);

  return (
    <Modal
      title="Linging"
      open={open}
      onOk={handleOk}
      confirmLoading={confirmLoading}
      onCancel={handleCancel}
      width={800}
      footer={[
        <div key="footer" style={{ textAlign: "center" }}>
          {isReceiving === true && isTrack === false ? (
            <Button
              type=""
              style={{ color: "white", backgroundColor: "green" }}
              onClick={handleOk}
            >
              Pick up
            </Button>
          ) : (
            <span></span>
          )}
          <Button
            type=""
            style={{ color: "white", backgroundColor: "red" }}
            onClick={handleCancel}
          >
            Shutdown
          </Button>
        </div>,
      ]}
    >
      {currentCall !== null ? (
        <>
          <div className="flex mb-3 items-center">
            <img
              src={currentCall.Message_group_image}
              alt="Image"
              className="me-5"
              style={{ borderRadius: "50%", width: "50px", height: "50px" }}
            />
            <div className="flex mt-1">
              <span class="material-symbols-outlined me-2">photo_camera</span>
              <Switch onClick={toggleCamera} />
            </div>

            <div className="flex ms-5 mt-1">
              <span class="material-symbols-outlined me-2">mic</span>
              <Switch onClick={toggleAudio}></Switch>
            </div>

            <Select
              className="ms-3"
              value={selectedDeviceId}
              style={{ width: 200 }}
              onChange={handleCameraChange}
              placeholder="Select Camera"
            >
              {videoDevices.map((device) => (
                <Option key={device.deviceId} value={device.deviceId}>
                  {device.label}
                </Option>
              ))}
            </Select>
          </div>

          <div className="flex w-full justify-between">
            <div className="w-80">
                <span className="w-full text-center">me</span>
              <video id="localVideo" autoPlay playsInline></video>
            </div>
            <div className="w-80">
            <span className="w-full text-center">you</span>
              <video id="remoteAudio" autoPlay playsInline></video>
            </div>
          </div>
        </>
      ) : (
        <div>No image available</div>
      )}
    </Modal>
  );
};

export { CallModal };
