import React, { useState, useEffect } from 'react';
import Contacts from "../components/Contacts";
import styled from 'styled-components';
import ChatContainer from "../components/ChatContainer";

import Welcome from "../components/Welcome";
///Khang
import { over } from 'stompjs';
import SockJS from 'sockjs-client';
import { getCurrentUserLocal, setConnectState, setConnectStateLocal } from '../utils/LocalStorage';

var stompClient = null;

const ChatPage = () => {
  const [currentChat, setCurrentChat] = useState(null);
  const [showWelcome, setShowWelcome] = useState(true);
  const [contacts, setContacts] = useState([]);
  const [updatename, setUpdateName] = useState('');
  const [message, setMessage] = useState({})
  ///Khang
  const [isConnect, setIsConnect] = useState(false)
  const connect = () => {
    let Sock = new SockJS('http://localhost:8080/ws');
    stompClient = over(Sock);
    stompClient.connect({}, onConnected, onError);
    // stompClient.disconnect(function(){
    //   console.log("disconnected")
    // })
    console.log(stompClient)

  }
  const onConnected = () => {
    var userId = getCurrentUserLocal()["_id"];
    console.log("id " + userId)
    stompClient.subscribe('/user/' + userId + '/message_group', onGroupMessage);
    stompClient.subscribe('/user/' + userId + '/message', onMessage);
  }
  const onMessage = (payload) => {
    var payloadData = JSON.parse(payload.body);
    console.log("On socket response ", payloadData)
  }
  const onGroupMessage = (payload) => {
    var payloadData = JSON.parse(payload.body);
    console.log("On socket response ", payloadData)
    setMessage(payloadData)
  }
  const onError = (err) => {
    console.log(err);

  }
  const onSave = (newName) => {
    setUpdateName(newName);
  }

  const updateContactInfo = (updatedContact) => {
    const updatedContacts = contacts.map(contact => {
      if (contact.MessageGroupId === updatedContact.MessageGroupId) {
        return updatedContact;
      }
      return contact;
    });
    setContacts(updatedContacts);
  };
  const changeChat = (newChat) => {
    setCurrentChat(newChat);
  };

  const changeCurrentChat = (index, contact) => {
    changeChat(contact);
    setShowWelcome(false);
  };
  const updateGroupName = (newGroupName) => {
    setCurrentChat(prevChat => ({
      ...prevChat,
      Message_group_name: newGroupName
    }));
  };

  useEffect(() => {
    if (isConnect === false) {
      connect();
    }
  }, []);

  return (
    <PageContainer>
      <div className='container'>
        <Contacts
          changeChat={changeChat}
          changeCurrentChat={changeCurrentChat}
          onSave={updatename}
          stompClient={stompClient} />

        {currentChat ? ( 
          <ChatContainer 
          currentChat={currentChat} 
          onSave={onSave}
          stompClient={stompClient}
          messagePayload={message}/>
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