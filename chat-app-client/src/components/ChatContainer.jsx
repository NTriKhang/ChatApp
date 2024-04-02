import React, { useState, useEffect, useRef } from "react";
import styled from "styled-components";
import Logout from "./Logout";
import { v4 as uuidv4 } from "uuid";
import axios from "axios";
import ChatInput from "./ChatInput";
import { getCurrentUserLocal } from "../utils/LocalStorage";
import UpdateNameMG from "./UpdateNameMG";
import UploadImages from "./UploadImages";

export default function ChatContainer({ currentChat }) {
  const [messages, setMessages] = useState([]);
  const scrollRef = useRef();
  const { MessageGroupId, Message_group_name, Message_group_image } = currentChat;
  const currentPage = useRef(1);
  const [loading, setLoading] = useState(false);
  const [reachedEnd, setReachedEnd] = useState(false);
  const [initialLoad, setInitialLoad] = useState(false);
  const [isScrolled, setIsScrolled] = useState(false);
  const lastFetchLength = useRef(0);


  const fetchMessages = async (page) => {
    try {
      const response = await axios.get(`http://localhost:8080/api/v1/messages/${MessageGroupId}?page=${page}`, {
        withCredentials: true
      });
      if (!response.data) {
        throw new Error('Không thể lấy dữ liệu từ API');
      }
      if (response.data == null) {
        return;
      }
      return response.data;
    } catch (error) {
      console.error('Lỗi:', error);
      return [];
    }
  };

  const fetchMoreMessages = async () => {
    if (loading || reachedEnd) return;
  
    try {
      setLoading(true);
  
      let oldScrollHeight = scrollRef.current.scrollHeight;
      let newMessages = [];
      let nextPage = currentPage.current + 1;
      currentPage.current = nextPage;
      if (lastFetchLength.current > 0 && lastFetchLength.current < 20) {
        setReachedEnd(true);
        setLoading(false);
        return;
      }
      newMessages = await fetchMessages(nextPage);
  
      if (newMessages.length > 0) {
        setMessages((prevMessages) => [...prevMessages, ...newMessages]);
        lastFetchLength.current = newMessages.length;
  
        if (newMessages.length < 20) {
          setReachedEnd(true);
        }
  
        if (newMessages.length >= 20) {
          scrollRef.current.scrollTop = scrollRef.current.scrollHeight - oldScrollHeight;
        }
      } else {
        if (lastFetchLength.current < 20) {
          setReachedEnd(true);
        }
      }
      
      setLoading(false);
    } catch (error) {
      console.log(`Failed to fetch messages: ${error.message}`);
      setLoading(false);
    }
  };
  const handleCloseEditDialog = () => {
    setShowEditDialog(false);
  };

  // Callback function để cập nhật tên nhóm trong state chat
  const updateGroupName = (newName) => {
    setChat({ ...chat, Message_group_name: newName });
    onSave?.(newName);
  };
  const uploadImg = (newImg) => {
    setChat({ ...chat, 
      Message_group_image: newImg });
    onSave?.(newImg);
  };
  const openImageDialog = () => {
    setShowImageDialog(true);
  };

  const closeImageDialog = () => {
    setShowImageDialog(false);
  };
  useEffect(() => {
    const handleScroll = (e) => {
      const { scrollTop } = e.currentTarget;
      if (scrollTop === 0 && !isScrolled && !reachedEnd) {
        fetchMoreMessages();
      }
      setIsScrolled(scrollTop > 0);
    };
  
    if (scrollRef.current) {
      scrollRef.current.addEventListener('scroll', handleScroll);
    }
  
    return () => {
      if (scrollRef.current) {
        scrollRef.current.removeEventListener('scroll', handleScroll);
      }
    };
  }, [isScrolled, reachedEnd]); 

  useEffect(() => {
    if ((initialLoad && isScrolled && !reachedEnd) || (lastFetchLength.current < 20 && !reachedEnd)) {
      scrollRef.current?.scrollIntoView({ behavior: "smooth", block: "end" });
    }
  }, [initialLoad, isScrolled, reachedEnd]);

  useEffect(() => {
    fetchMessages();
  }, []); 


  useEffect(() => {
    const fetchData = async () => {
      try {
        const initialMessages = await fetchMessages(currentPage.current);
        setMessages(initialMessages);
        setInitialLoad(true);
      } catch (error) {
        console.error('Lỗi khi tải tin nhắn:', error);
      }
    };
    fetchData();
  }, []);

  return (
    <Container>
      <div className="chat-header">
        <div className="user-details">
        <div className="avatar" onClick={openImageDialog}> 
        {
            Message_group_image?
            <img src={`http://localhost:8080/${Message_group_image}`} alt="" />:
            <img src="https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcSUdO2qhODLgmxWPYWgpV9P4BOqAGx5-LNM0A&usqp=CAU" alt="Defaut Image" />
          
        }
        </div>
          <div className="nameChat">
            {Message_group_name ? (
              <h3>{Message_group_name}</h3>
            ) : (
              <h3>Không có tên</h3>
            )}
          </div>
          <div className="editName">
            <button className="editButton" onClick={() => setShowEditDialog(true)}>
              <p>✎</p>
            </button>
            {showEditDialog && (
              <UpdateNameMG
                handleClose={handleCloseEditDialog}
                groupId={MessageGroupId}
                updateGroupName={updateGroupName} // Truyền hàm callback vào component con
              />
            )}
          </div>
        </div>
        <Logout />
      </div>
      {showImageDialog && (
         <Modal onClick={closeImageDialog}>
          <ModalContent onClick={(e) => e.stopPropagation()}>
            <UploadImages 
                  closeDialog={closeImageDialog} 
                  GroupID={MessageGroupId} 
                  onImageUpload={uploadImg} />  
            <CloseButton onClick={closeImageDialog}>&times;</CloseButton>
          </ModalContent>
        </Modal>
      )}
       <div className="chat-messages" ref={scrollRef}>
        {messages.slice().reverse().map((message, index) => (
          <MessageBubble ref={index === messages.length - 1 ? scrollRef : null} key={uuidv4()} fromSelf={message.fromSelf}>
            {message.fromSelf ? null : <div className="sender-name">{message.Sender_user.user_name}</div>}
            <div className={`message ${message.fromSelf ? "sended" : "recieved"}`}>
              <div className="content">
                <p>{message.Content}</p>
                {message.Type === "image" && <img src={message.Media_path} alt="Attached" />}
                {message.Attach_file && (
                  <div>
                    <a href={message.Attach_file.path} target="_blank" rel="noopener noreferrer">Download File</a>
                  </div>
                )}
              </div>

              <div className="message-info">
                <span>Sent at {new Date(message.Created_date).toLocaleString()}</span>
                {message.Reply_to_msg && <span> | Replying to: {message.Reply_to_msg.content}</span>}
                <div>Seen by: {message.Seen_by?.join(', ') || 'None'}</div>
              </div>
            </div>
            <div className="message-time">
              {new Date(message.Created_date).toLocaleTimeString([], { hour: '2-digit', minute: '2-digit' })}
            </div>
          </MessageBubble>
        ))}
      </div>
      <ChatInput />
    </Container>
  );
}

const Container = styled.div`
  display: flex;
  flex-direction: column;
  background-color: #f0f0f0;
  width: 100%;

  .chat-header {
    display: flex;
    justify-content: space-between;
    align-items: center;
    padding: 1rem 2rem;
    background-color: rgb(48, 43, 99);
    color: white;
    height: 12%;

    .user-details {
      display: flex;
      align-items: center;
      gap: 1rem;

      .avatar img {
        height: 50px;
        width: 50px;
        border-radius: 50%;
        border: 2px solid #ffffff;
      }

      .nameChat h3 {
        margin: 0;
      }

      .editName .editButton {
        background: none;
        border: none;
        cursor: pointer;
        padding: 0;
      }

      .editName .editButton p {
        color: #fff;
        font-size: 20px;
        padding-bottom: 5px;
      }

      .editName .editButton:hover p {
        color: #666;
      }
    }
  }

  .chat-messages {
    flex: 1;
    padding: 1rem 2rem;
    padding-bottom: 2rem;
    display: flex;
    flex-direction: column;
    gap: 10px;

    overflow-y: auto; 
    max-height: calc(100vh - 200px);


    .message {
      position: relative;
      display: flex;
      flex-direction: column;
      gap: 10px;
      padding: 1rem;
      border-radius: 8px;
      background-color: #ffffff;
      width: fit-content;
    }

    .sended {
      align-self: flex-end;
      background-color: #00b4d8;
      color: white;
    }

    .recieved {
      align-self: flex-start;
      background-color: #ffffff;
    }

    .content img {
      max-width: 100%;
      max-height: 200px;
      border-radius: 8px;
      margin-top: 10px;
      object-fit: contain;
    }

    .message-info {
      font-size: 0.75rem;
      color: #666;
      text-align: right;
    }

    .message-info span {
      display: block;
    }
  }
`;
const Modal = styled.div`
  position: fixed;
  top: 0;
  left: 0;
  width: 100%;
  height: 100%;
  background-color: rgba(15, 12, 41, 0.6); /* Màu nền với độ mờ */
  display: flex;
  justify-content: center;
  align-items: center;
`;

const ModalContent = styled.div`
  background-color: #ffffff;
  padding: 20px;
  border-radius: 8px;
  box-shadow: 0px 0px 10px rgba(0, 0, 0, 0.1);
  z-index: 1000;
`;

const CloseButton = styled.button`
  position: absolute;
  top: 10px;
  right: 10px;
  background: none;
  border: none;
  font-size: 20px;
  cursor: pointer;
  color: #333;
`;

const MessageBubble = styled.div`
  display: flex;
  flex-direction: column;
  gap: 10px;
  padding: 1rem;
  border-radius: 8px;
  background-color: #ffffff;
  box-shadow: 0 2px 5px rgba(0, 0, 0, 0.1);
  margin-bottom: 10px;
  width: fit-content;
  max-width: 70%;
  position: relative;

  .sender-name {
    color: #333;
    padding-left: 10px;
    font-size: 20px;
  }

  .message {
    position: relative;
    background-color: #ffffff;
    padding: 1rem;
    border-radius: 8px;
    margin-bottom: 2px;

    &.sended {
      align-self: flex-end;
      background-color: #dcf8c6;
    }

    &.recieved {
      align-self: flex-start;
    }

    .content img {
      max-width: 100%;
      height: auto;
      border-radius: 8px;
      margin-top: 10px;
    }

    .message-info {
      display: none;
    }
    .message-time {
      position: absolute;
      right: 10px;
      bottom: 10px;
      font-size: 0.75rem;
      color: #666;
    }
  }
  .message-time {
    text-align: right;
  }
  .message-info {
    font-size: 0.75rem;
    color: #666;
    padding: 4px 8px;
    background-color: #f0f0f0;
    border-radius: 8px;
    position: absolute;
    bottom: -20px;
    left: 0;
    right: 0;
    margin: auto;
    text-align: center;
    width: max-content;
    max-width: 100%;
    box-sizing: border-box;
  }
`;
