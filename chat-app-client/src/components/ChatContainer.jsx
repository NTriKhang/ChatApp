import React, { useState, useEffect, useRef } from "react";
import styled from "styled-components";
import Logout from "./Logout";
import { v4 as uuidv4 } from "uuid";
import axios from "axios";

export default function ChatContainer() {
  const [messages, setMessages] = useState([]);
  const scrollRef = useRef();

  // Cập nhật hàm để sử dụng ID người dùng cố định
  const fetchMessages = async () => {
    const userId = "65dfd1f51e074622e7cd00c1"; // Sử dụng ID cố định
    const response = await axios.get(`http://localhost:8080/api/v1/messages/${userId}`);
    setMessages(response.data);
  };

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
            <img src="https://cdn.sforum.vn/sforum/wp-content/uploads/2023/10/avatar-trang-4.jpg" alt="" />
          </div>
          <div className="username">
            <h3>Test User</h3> {/* Thay thế bằng username thực tế nếu có */}
          </div>
        </div>
        <Logout />
      </div>
      <div className="chat-messages">
      {messages.map((message) => (
        <div ref={scrollRef} key={uuidv4()}>
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
              <span> | From: {message.Sender_user.name}</span>
              {message.Reply_to_msg && <span> | Replying to: {message.Reply_to_msg.content}</span>}
              <div>Seen by: {message.Seen_by?.join(', ') || 'None'}</div>
              <div>Unseen by: {message.Unseen?.join(', ') || 'None'}</div>
            </div>
          </div>
        </div>
      ))}

      </div>
    </Container>
  );
}
/* Định nghĩa style cho container chứa chat */
const Container = styled.div`
  display: flex;
  flex-direction: column;
  height: 100vh; /* Full height */
  background-color: #f0f0f0; /* Light grey background */
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
        border-radius: 50%; /* Makes the image circular */
        border: 2px solid #ffffff; /* White border around the avatar */
      }

      .username h3 {
        margin: 0; /* Removes default margin */
      }
    }
  }

  .chat-messages {
    flex: 1;
    padding: 1rem 2rem;
    display: flex;
    flex-direction: column;
    gap: 10px;
    overflow-y: auto; /* Enables vertical scrolling if the content overflows */

  .message {
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
  height: auto;
  border-radius: 8px;
  margin-top: 10px;
}

.message-info {
  font-size: 0.75rem;
  color: #666;
  text-align: right;
}

.message-info span {
  display: block; /* Makes each piece of info appear on a new line */
}
  }
`;
