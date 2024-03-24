import React, { useState, useEffect } from "react";
import styled from "styled-components";
import Logo from "../assets/logo.svg";
import {  getCurrentUserLocal } from "../utils/LocalStorage"

export default function Contacts({  changeChat }) {
  const currentUser = getCurrentUserLocal();
  const [contacts, setContacts] = useState([]);
  const [currentUserImage, setCurrentUserImage] = useState('');
  const [currentSelected, setCurrentSelected] = useState(null);



  useEffect(() => {
    const fetchUserGroups = async () => {
      try {
        const response = await fetch(`http://localhost:8080/api/v1/message_group/${currentUser._id}`);
        if (!response.ok) {
          throw new Error('Network response was not ok');
        }
        const data = await response.json();
        setContacts(data); 
      } catch (error) {
        console.error('There was a problem with fetching user groups:', error);
      }
    };

    fetchUserGroups();
  }, []); 

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
                key={contact._id}
                className={`contact ${index === currentSelected ? "selected" : ""}`}
                onClick={() => changeCurrentChat(index, contact)}
              >
                <div className="avatar">
                  {
                    contact.avatar?
                    <img src={`data:image/svg+xml;base64,${contact.avatarImage}`} alt="" />:
                    <img src="https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcSUdO2qhODLgmxWPYWgpV9P4BOqAGx5-LNM0A&usqp=CAU" alt="Defaut Image" />
                  }
                  
                </div>
                <div className="username">
                  <h3>{contact.username}</h3>
                  <h2>{contact.Message_group_name}</h2>
                  {
                    contact.last_message?
                    <p>{contact.last_message }</p> :
                    <p>Chưa có tin nhắn</p>
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
  height: 100vh;
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
        padding-bottom: 15px;
        h3, p {
          color: #CCC;
          margin: 0 10px 0 0;
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
// const Container = styled.div`
//   display: grid;
//   grid-template-rows: 10% 75% 15%;
//   overflow: hidden;
//   background-color: #080420;
//   .brand {
//     display: flex;
//     align-items: center;
//     gap: 1rem;
//     justify-content: center;
//     img {
//       height: 2rem;
//     }
//     h3 {
//       color: white;
//       text-transform: uppercase;
//     }
//   }
//   .contacts {
//     display: flex;
//     flex-direction: column;
//     align-items: center;
//     overflow: auto;
//     gap: 0.8rem;
//     &::-webkit-scrollbar {
//       width: 0.2rem;
//       &-thumb {
//         background-color: #ffffff39;
//         width: 0.1rem;
//         border-radius: 1rem;
//       }
//     }
//     .contact {
//       background-color: #ffffff34;
//       min-height: 5rem;
//       cursor: pointer;
//       width: 90%;
//       border-radius: 0.2rem;
//       padding: 0.4rem;
//       display: flex;
//       gap: 1rem;
//       align-items: center;
//       transition: 0.5s ease-in-out;
//       .avatar {
//         img {
//           height: 3rem;
//         }
//       }
//       .username {
//         h3 {
//           color: white;
//         }
        
//       }
//     }
//     .selected {
//       background-color: #9a86f3;
//     }
//   }

//   .current-user {
//     background-color: #0d0d30;
//     display: flex;
//     justify-content: center;
//     align-items: center;
//     gap: 2rem;
//     .avatar {
//       img {
//         height: 4rem;
//         max-inline-size: 100%;
//       }
//     }
//     .username {
//       h2 {
//         color: white;
//       }
//     }
//     @media screen and (min-width: 720px) and (max-width: 1080px) {
//       gap: 0.5rem;
//       .username {
//         h2 {
//           font-size: 1rem;
//         }
//       }
//     }
//   }
// `;
