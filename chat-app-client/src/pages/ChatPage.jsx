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

var stompClient = null;

const ChatPage = () => {
  const [currentChat, setCurrentChat] = useState(null);
  const [showWelcome, setShowWelcome] = useState(true);
  const [message, setMessage] = useState({});
  const [notify, setNotify] = useState({});
  const [isConnect, setIsConnect] = useState(false);
  const currentUser = getCurrentUserLocal();
  const { data: messageGroup, refetch } = useGetMessageGroup(currentUser._id);
  
  const connect = () => {
    let Sock = new SockJS("http://localhost:8080/ws");
    stompClient = over(Sock);
    stompClient.connect({}, onConnected, onError);
    console.log(stompClient);
  };
  const onConnected = () => {
    var userId = getCurrentUserLocal()["_id"];
    stompClient.subscribe('/user/' + userId + '/message_group', onGroupMessage);
    stompClient.subscribe('/user/' + userId + '/message', onMessage);
    stompClient.subscribe('/user/' + userId + '/notify', onNotify)
  }
  const onNotify = (payload) => {
    var payloadData = JSON.parse(payload.body);
    setNotify(payloadData)
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

  const changeSelectedSearch = (user) => {
    //console.log(user)
    let newContact = {
      Is_read : true,
      Last_message : {message_id: null, content: null, user_name: null, created_date: null},
      MessageGroupId : "",
      Message_group_image : user.Image_path,
      Message_group_name : user.Display_name,
      Message_group_type : "Individual",
      ReceiverId : user._id,
      Role : "Participant"
    }
    console.log(newContact)
    setCurrentChat(newContact)
  }
  const changeCurrentChat = (index, contact) => {
    console.log(contact)
    setCurrentChat(contact);
    setShowWelcome(false);
  };

  useEffect(() => {
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
                  changeSelectedSearch={changeSelectedSearch}/>

        {currentChat ? (
          <ChatContainer
            messageGroup={messageGroup}
            currentChat={currentChat}
            onSave={onSave}
            stompClient={stompClient}
            messagePayload={message}
          />
        ) : (
          <Welcome />
        )}
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
