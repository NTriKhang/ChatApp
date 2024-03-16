import React, { useState } from 'react';
import Contacts from "../components/Contacts"; 
import styled from 'styled-components';
import ChatContainer from "../components/ChatContainer";

const ChatPage = () => {
  const [currentChat, setCurrentChat] = useState(null);

  const changeChat = (newChat) => {
    setCurrentChat(newChat);
  };

  return (
    <PageContainer>
      <Contacts changeChat={changeChat} />
      <ChatContainer currentChat={currentChat} />
    </PageContainer>
  );
};

export default ChatPage;
const PageContainer = styled.div`
  display: flex;
  height: 100vh;
`;