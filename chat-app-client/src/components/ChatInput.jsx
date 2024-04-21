import React, { useState } from "react";
import { BsEmojiSmileFill } from "react-icons/bs";
import { IoMdSend } from "react-icons/io";
import styled from "styled-components";
import Picker from "emoji-picker-react";
import { FiImage } from "react-icons/fi";
import { getCurrentUserLocal } from "../utils/LocalStorage";

export default function ChatInput({ handleSendMsg, stompClient, currentChat }) {
  const [msg, setMsg] = useState("");
  const [showEmojiPicker, setShowEmojiPicker] = useState(false);
  const handleEmojiPickerhideShow = () => {
    setShowEmojiPicker(!showEmojiPicker);
  };

  const handleEmojiClick = (event, emojiObject) => {
    let message = msg;
    message += emojiObject.emoji;
    setMsg(message);
  };

  const sendChat = (event) => {
    event.preventDefault();

    // console.log(stompClient)
    // console.log(currentChat.MessageGroupId)
    var currentUser = getCurrentUserLocal();
    console.log(currentChat)
    if (msg.length > 0) {
      if (currentChat.Message_group_type === 'Group') {
        let messageTextDto = {
          Content: msg,
          Message_group_id: currentChat.MessageGroupId,
          Sender_user: {
            user_id: currentUser._id,
            user_name: currentUser.Display_name
          }
        }
        stompClient.send("/app/sendMessage", {}, JSON.stringify(messageTextDto))
        //console.log(messageTextDto)
      }
      else {
        let messageTextIndDto = {
          Content: msg,
          SenderName: currentUser.Display_name,
          SenderId: currentUser._id,
          MsgGroupSenderId: currentChat.MessageGroupId,
          ReceiverId: currentChat.ReceiverId
        }
        stompClient.send("/app/sendIndMessage", {}, JSON.stringify(messageTextIndDto))
      }
      setMsg("");
    }
  };

  const handleImageUploadChange = (event) => {
    const file = event.target.files[0];
    if (file) {
      console.log(file.size);
      setMsg(file.name);
    }
  };
  const handleFileUploadClick = () => {
    const imageUpload = document.getElementById('imageUpload');
    if (imageUpload) {
      imageUpload.click();
    }
  };

  return (
    <Container>
      <div className="button-container">
        <div className="emoji">
          <BsEmojiSmileFill onClick={handleEmojiPickerhideShow} />
          {showEmojiPicker && <Picker onEmojiClick={handleEmojiClick} />}
        </div>
        <div className="upload-image">
          <input type="file" id="imageUpload" onChange={handleImageUploadChange} />
          <FiImage onClick={handleFileUploadClick} />
        </div>
      </div>
      <div className="input-container">
        <form onSubmit={(event) => sendChat(event)}>
          <input
            type="text"
            placeholder="type your message here"
            onChange={(e) => setMsg(e.target.value)}
            value={msg}
          />
          <button type="submit">
            <IoMdSend />
          </button>
        </form>
      </div>
    </Container>
  );
}

const Container = styled.div`
  display: grid;
  align-items: center;
  grid-template-columns: 10% 90%;
  background-color: #333; 
  position: relative; 
  padding: 0.5rem; 
  border-radius: 2rem; 
  box-shadow: 0 2px 5px rgba(0, 0, 0, 0.2); 
  @media screen and (min-width: 720px) and (max-width: 1080px) {
    padding: 0 1rem;
    gap: 1rem;
  }
  .button-container {
    display: flex;
    align-items: center;
    justify-content: space-between;
    color: white;
    .emoji {
      position: relative;
      svg {
        cursor: pointer;
        font-size: 2rem; 
        margin: 0 0.2rem; 
        color: #ffc107;
      }
      .emoji-picker-react {
        position: absolute;
        top: -350px;
        background-color: #080420;
        box-shadow: 0 5px 10px #9a86f3;
        border-color: #9a86f3;
        .emoji-scroll-wrapper::-webkit-scrollbar {
          background-color: #080420;
          width: 5px;
          &-thumb {
            background-color: #9a86f3;
          }
        }
        .emoji-categories {
          button {
            filter: contrast(0);
          }
        }
        .emoji-search {
          background-color: transparent;
          border-color: #9a86f3;
        }
        .emoji-group:before {
          background-color: #080420;
        }  
      }
    }
    .upload-image {
      position: relative;
      
      input[type="file"] {
        display: none;
      }
      
      svg {
        cursor: pointer;
        font-size: 2rem; 
        margin: 0 0.2rem; 
        color: #fff;
        vertical-align: middle;
      }
    }
  }
  .input-container {
    width: 100%;
    border-radius: 2rem;
    display: flex;
    align-items: center;
    justify-content: space-between;
    overflow: hidden;
    gap: 1rem;
    background-color: #fff;
    form {
      width: 100%;
      display: flex !important;
      align-items: center;
      justify-content: space-between;
    }
    input {
      flex: 1;
      width: 90%;
      height: 3rem;
      color: black;
      padding-left:1rem;
      border: none;
      font-size: 1.2rem;

      &::selection {
        background-color: #9a86f3;
      }
      &:focus {
        outline: none;
      }
    }
    button {
      border-radius: 50px; 
      width: 3rem;
      height: 3rem;
      padding: 0.5rem;
      display: flex;
      box-shadow: 0 2px 5px rgba(0,0,0,0.2);
      justify-content: center;
      align-items: center;
      background-color: #9a86f3;
      border: none;
      @media screen and (min-width: 720px) and (max-width: 1080px) {
        padding: 0.3rem 1rem;
        svg {
          font-size: 1rem;
        }
      }
      svg {
        font-size: 1.5rem;
        color: white;
        cursor: pointer;
      }
      &:focus {
        outline: none; 
      }
      &:hover {
        background-color: #a99ee3; 
      }
    }
  }
`;