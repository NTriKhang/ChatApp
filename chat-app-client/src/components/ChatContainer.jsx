import React, { useState, useEffect, useRef } from "react";
import styled from "styled-components";
import Logout from "./Logout";
import { v4 as uuidv4 } from "uuid";
import axios from "axios";
import ChatInput from "./ChatInput";
import {  getCurrentUserLocal } from "../utils/LocalStorage"

export default function ChatContainer({ currentChat }) {
  const currentUser = getCurrentUserLocal();
  const [messages, setMessages] = useState([]);
  const scrollRef = useRef();
  const { MessageGroupId, Message_group_name, Message_group_image } = currentChat;
  // Cập nhật hàm để sử dụng ID người dùng cố định
  const fetchMessages = async () => {
    const messGroupID = "65dfd1f51e074622e7cd00c1"; // Sử dụng ID cố định
    const response = await axios.get(`http://localhost:8080/api/v1/messages/${MessageGroupId}`);//currentUser._id
    setMessages(response.data);
  };

  useEffect(() => {
    // Code xử lý tương ứng với currentChat
  }, [currentChat]);
  useEffect(() => {
    fetchMessages();
  }, []); // Chỉ gọi một lần khi component được mount

  useEffect(() => {
    scrollRef.current?.scrollIntoView({ behavior: "smooth" });
  }, [messages]);

  return (
    <Container>
      <div className="chat-header">
        <div className="user-details">
          <div className="avatar">
                {
                  Message_group_image?
                  <img src={`http://localhost:8080/${Message_group_image}`} alt="" />:
                  <img src="https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcSUdO2qhODLgmxWPYWgpV9P4BOqAGx5-LNM0A&usqp=CAU" alt="Defaut Image" />
                }
          </div>
          <div className="nameChat">
            {Message_group_name?
              <h3>{Message_group_name}</h3>:
              <h3>Không có tên</h3>
            }
          </div>
          <div className="editName">
            <button className="editButton" > 
              {/* onClick={handleEditName} */}
              <p>✎</p>
            </button>
          </div>
        </div>
        <Logout />
      </div>
      <div className="chat-messages">
        {messages.map((message) => (
          <MessageBubble ref={scrollRef} key={uuidv4()} fromSelf={message.fromSelf}>
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
                {/* <div>Unseen by: {message.Unseen?.join(', ') || 'None'}</div> */}
              </div>
            </div>
            <div className="message-time">
              {new Date(message.Created_date).toLocaleTimeString([], { hour: '2-digit', minute: '2-digit' })}
            </div>
          </MessageBubble>
        ))}
      </div>
      <ChatInput></ChatInput>
    </Container>
  );
}
/* Định nghĩa style cho container chứa chat */
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
        padding-bottom:5px;
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

const MessageBubble = styled.div`
  display: flex;
  flex-direction: column;
  gap: 10px;
  padding: 1rem;
  border-radius: 8px;
  background-color: #ffffff;
  box-shadow: 0 2px 5px rgba(0,0,0,0.1);
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

    text-align:right;
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
