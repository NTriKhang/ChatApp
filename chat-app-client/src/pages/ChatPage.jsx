import React, { useState, useEffect } from 'react';
import Contacts from "../components/Contacts"; 
import styled from 'styled-components';
import ChatContainer from "../components/ChatContainer";

import Welcome from "../components/Welcome";

const ChatPage = () => {
  const [currentChat, setCurrentChat] = useState(null);
  const [showWelcome, setShowWelcome] = useState(true);
console.log(123123);

  const changeChat = (newChat) => {
    setCurrentChat(newChat);
  };
  const changeCurrentChat = (index, contact) => {
    setCurrentSelected(index);
    changeChat(contact); // Cập nhật currentChat với contact được chọn
    setShowWelcome(false);
  };
  
  return (
    <PageContainer>
      <div className='container'>
      <Contacts changeChat={changeChat} changeCurrentChat={changeCurrentChat} />
        {currentChat ? ( // Nếu currentChat tồn tại, hiển thị ChatContainer
          <ChatContainer currentChat={currentChat} />
        ) : (
          <Welcome /> // Ngược lại, hiển thị Welcome component
        )}
      </div>
    </PageContainer>
  );
};

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