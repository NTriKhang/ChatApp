import React, { useState, useEffect } from "react";
import styled from "styled-components";
import Logo from "../assets/logo.svg";
import {  getConnectStateLocal, getCurrentUserLocal, setConnectStateLocal } from "../utils/LocalStorage"
import axios from 'axios';

export default function Contacts({  changeChat, onSave }) {
  const currentUser = getCurrentUserLocal();
  const [contacts, setContacts] = useState([]);
  const [currentUserImage, setCurrentUserImage] = useState('');
  const [currentSelected, setCurrentSelected] = useState(null);

  useEffect(() => {
    const fetchUserGroups = async () => {
        try {
            var connectStateString = getConnectStateLocal();
            var connectStateBoolean = connectStateString === "true";
            const response = await axios.get(`http://localhost:8080/api/v1/message_group/${currentUser._id}?isConnected=${connectStateBoolean}`);
            if (response.status !== 200) {
                throw new Error('Network response was not ok');
            }
            setContacts(response.data);
            setConnectStateLocal(true) 
        } catch (error) {
            console.error('There was a problem with fetching user groups:', error);
        }
    };
    fetchUserGroups();
}, [onSave]);
  
  const changeCurrentChat = (index, contact) => {
  setCurrentSelected(index);
  changeChat(contact);
  };

  return (
    <>
        <Container>
          <div className="brand bg">
            <img src={Logo} alt="logo" />
            <h3>App chat</h3>
          </div>
          <div className="contacts">
            {contacts.map((contact, index) => (
              <div
                key={contact.MessageGroupId}
                className={`contact ${index === currentSelected ? "selected" : ""}`}
                onClick={() => changeCurrentChat(index, contact) }
              >
                <div className="avatar">
                  {
                    contact.Message_group_image?
                    <img src={`http://localhost:8080/${contact.Message_group_image}`} alt="" />:
                    <img src="https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcSUdO2qhODLgmxWPYWgpV9P4BOqAGx5-LNM0A&usqp=CAU" alt="Defaut Image" />
                  }
                  
                </div>
                <div className="username">
                  <h3>{contact.username}</h3>
                  <h3>{contact.Message_group_name}</h3>
                  {
                    contact.last_message ?
                    <p>{contact.last_message.length >= 26 ? contact.last_message.slice(0, 21) + '...' : contact.last_message }</p> :
                    <p>Chưa có tin nhắn cuối</p>
                  }
                  {contact.is_read ? <span>✅</span> : <span>❌</span>}
                </div>
              </div>
            ))}
            
          </div>
          <div className="current-user">
            <div className="avatar">
              {
                currentUserImage?
                <img src={`data:image/svg+xml;base64,${currentUserImage}`} alt="avatar" />:
                <img src="https://cdn.sforum.vn/sforum/wp-content/uploads/2023/10/avatar-trang-4.jpg" alt="avatar" />
              }
            </div>
            <div className="username"> 
            <h2 className="text-white">{currentUser?.Display_name}</h2>
            </div>
          </div>
        </Container>
    </>
  );
}
const Container = styled.div`
  display: flex;
  flex-direction: column;
  background-color: #0F0C29; /* Deep blue background */
  
  .brand {
    display: flex;
    justify-content: center;
    align-items: center;
    padding: 20px;
    background-color: #302b63;
    border-left:1px solid black;
    img {
      height: 40px;
    }
    h3 {
      margin-left: 10px;
      color: #EAEAEA;
      font-size: 24px;
    }
  }

  .contacts {
    flex: 1;
    overflow-y: auto;
    padding: 20px;
    display: grid;
    grid-template-columns: repeat(auto-fill, minmax(250px, 1fr));
    gap: 15px;
    &::-webkit-scrollbar {
      width: 5px;
    }
    &::-webkit-scrollbar-thumb {
      background: #5D5D5D;
    }
    .contact {
      height: 15%;
      display: flex;
      align-items: center;
      background-color: #222034;
      padding: 10px;
      border-radius: 8px;
      cursor: pointer;
      transition: transform 0.2s ease-in-out;
      &:hover {
        transform: translateY(-5px);
      }
      .avatar {
        img {
          border-radius: 50%;
          width: 50px;
          height: 50px;
        }
      }
      .username {
        margin-left: 15px;
        padding: 15px 0;
        
        h3, p {
          color: #CCC;
          margin: 0px 10px 0 0;
          display: inline-block;
        }
        span {
          color: #919191;
          font-size: 0.8rem;


        }
      }
    }
    .selected {
      background-color: #554E8F;
    }
  }

  .current-user {
    background-color: #222034;
    padding: 20px;
    display: flex;
    justify-content: center;
    align-items: center;
    .avatar img {
      border-radius: 50%;
      width: 50px;
      height: 50px;
    }
    .username h2 {
      margin-left: 15px;
      color: #FFF;
      font-size: 20px;
    }
  }

  @media (max-width: 768px) {
    .contacts {
      grid-template-columns: repeat(auto-fill, minmax(200px, 1fr));
    }
    .brand img {
      height: 35px;
    }
    .brand h3 {
      font-size: 20px;
    }
    .current-user .avatar img {
      width: 60px;
      height: 60px;
    }
    .current-user .username h2 {
      font-size: 16px;
    }
  }
`;
