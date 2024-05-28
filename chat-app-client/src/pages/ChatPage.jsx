import React, { useState, useEffect } from "react";
import Contacts from "../components/Contacts";
import styled from "styled-components";
import ChatContainer from "../components/ChatContainer";

import Welcome from "../components/Welcome";
///Khang
import { over } from "stompjs";
import SockJS from "sockjs-client";
import { getCurrentUserLocal } from "../utils/LocalStorage";
import { useGetMessageGroup } from "../hooks/useGetMessageGroup";
import { CallModal } from "../components/modal/CallModal";
import { conforms, every, set } from "lodash";
import { useNavigate } from "react-router-dom";

var stompClient = null;
const ChatPage = () => {
  const navigate = useNavigate();

  const [currentChat, setCurrentChat] = useState(null);
  const [currentCall, setCurrentCall] = useState(null);
  const [showWelcome, setShowWelcome] = useState(true);
  const [message, setMessage] = useState({});
  //const [notify, setNotify] = useState({});
  const [isConnect, setIsConnect] = useState(false);
  const currentUser = getCurrentUserLocal();
  const { data: messageGroup, refetch } = useGetMessageGroup(currentUser._id);

  //calling session
  const [open, setOpen] = useState(false);
  const [offer, setOffer] = useState(null);
  const [answer, setAnswer] = useState(null)
  const [iceCandidate, setIceCandidate] = useState(null);
  const [isReceive, setIsReceive] = useState(false);
  const [shutdown, setShutdown] = useState(false);

  const connect = () => {
    let Sock = new SockJS("http://localhost:8080/ws");
    stompClient = over(Sock);
    stompClient.connect({}, onConnected, onError);
    console.log(stompClient);
  };

  const onConnected = () => {

    let userId2 = currentUser['_id']
    stompClient.subscribe('/user/' + userId2 + '/message_group', onGroupMessage);
    stompClient.subscribe('/user/' + userId2 + '/message', onMessage);
    stompClient.subscribe('/user/' + userId2 + '/notify', onNotify)
    //call
    stompClient.subscribe('/user/' + userId2 + '/private_call', onPrivateCall);
    stompClient.subscribe('/user/' + userId2 + '/offer_private_call', onOfferPrivateCall);
    stompClient.subscribe('/user/' + userId2 + '/answer_private_call', onAnswerPrivateCall);
    stompClient.subscribe('/user/' + userId2 + '/shutdown_call', onShutdownCall);


  }

  const onNotify = (payload) => {
    refetch()
  }
  const onMessage = (payload) => {
    var payloadData = JSON.parse(payload.body);
    setMessage(payloadData)
    refetch()
  }
  const onGroupMessage = (payload) => {
    var payloadData = JSON.parse(payload.body);
    setMessage(payloadData);
    refetch()
  };
  const onError = (err) => {
    console.log(err);
  }

  const onSave = () => {
    refetch();
  };
  // on get answer from user-2
  const onAnswerPrivateCall = (event) => {
    let answer = JSON.parse(event.body)
    //console.log(peerConnection.remoteDescription)
    setAnswer(answer)
  }
  // on get offer from user-1 
  const onOfferPrivateCall = (event) => {
    let offer = JSON.parse(event.body)
    setIsReceive(true)
    setOffer(offer);
    setCurrentCall({ receiverId: offer.senderId, Message_group_image: offer.receiverImageUrl });
  }
  // on get iceCandidate from both side
  const onPrivateCall = (event) => {
    let iceCandidate = JSON.parse(event.body);
    setIceCandidate(iceCandidate)
  }
  const onShutdownCall = (event) => {
    setShutdown(true)
    setCurrentCall(null)
    setOffer(null)
    setAnswer(null)
    setIceCandidate(null)
    setIsReceive(false)
  }
  const changeSelectedSearch = (user) => {
    //console.log(user)
    let newContact = {
      Is_read: true,
      Last_message: { message_id: null, content: null, user_name: null, created_date: null },
      MessageGroupId: "",
      Message_group_image: user.Image_path,
      Message_group_name: user.Display_name,
      Message_group_type: "Individual",
      ReceiverId: user._id,
      Role: "Participant"
    }
    console.log(newContact)
    setCurrentChat(newContact)
  }
  const changeCurrentChat = (index, contact) => {
    console.log(contact)
    setCurrentChat(contact);
    setShowWelcome(false);
  };

  const showCallModal = (receiverId, Message_group_image) => {
    setCurrentCall({ receiverId, Message_group_image })
    //console.log(Message_group_image)
    setOpen(true);
  };
  useEffect(() => {
    console.log(currentUser)
    if (isConnect === false) {
      connect();
    }
  }, []);
  return (
    <PageContainer>
      <div className="container">
        <Contacts messageGroup={messageGroup}
          changeChat={changeCurrentChat}
          currentChat={currentChat}
          stompClient={stompClient}
          changeSelectedSearch={changeSelectedSearch}
          refetch={refetch}
        />

        {currentChat ? (
          <ChatContainer
            messageGroup={messageGroup}
            currentChat={currentChat}
            onSave={onSave}
            stompClient={stompClient}
            messagePayload={message}
            showCallModal={showCallModal}
          />
        ) : (
          <Welcome />
        )}
        <CallModal
          currentCall={currentCall}
          stompClient={stompClient}
          open={open}
          setOpen={setOpen}
          isReceiving={isReceive}
          offer={offer}
          iceCandidate={iceCandidate}
          answer={answer}
          shutdown={shutdown}
          setShutdown={setShutdown}
        ></CallModal>
      </div>
    </PageContainer>
  );
}

export default ChatPage;
const PageContainer = styled.div`
  height: 100vh;
  width: 100vw;
  display: flex;
  flex-direction: column;
  justify-content: center;
  gap: 1rem;
  align-items: center;
  background-color: #131324;
  .container {
    height: 85vh;
    width: 85vw;
    background-color: #00000076;
    display: grid;
    grid-template-columns: 25% 75%;
    @media screen and (min-width: 720px) and (max-width: 1080px) {
      grid-template-columns: 35% 65%;
    }
  }
`;
