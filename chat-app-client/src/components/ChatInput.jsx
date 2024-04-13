import React, { useState } from "react";
import { BsEmojiSmileFill } from "react-icons/bs";
import { IoMdSend } from "react-icons/io";
import styled from "styled-components";
import Picker from "emoji-picker-react";
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
    
    if (msg.length > 0) {
      //handleSendMsg(msg);
      let messageTextDto = {
        Content : msg,
        Message_group_id: currentChat.MessageGroupId,
        Sender_user: {
          user_id: currentUser._id,
          user_name: currentUser.Display_name
        }
      }
      stompClient.send("/app/sendMessage", {}, JSON.stringify(messageTextDto))
      //console.log(messageTextDto)
      setMsg("");
    }
    else{
      let messageTextIndDto = {
        Content: "test private message",
        SenderName: "ivy quoc123",
        SenderId: "65dfd0041e074622e7cd00b7",
        ReceiverId: "65dfd0041e074622e7cd00b8"
      }
      stompClient.send("/app/sendIndMessage", {}, JSON.stringify(messageTextIndDto))
    }
  };

  return (
    <Container>
      <div className="button-container">
        <div className="emoji">
          <BsEmojiSmileFill onClick={handleEmojiPickerhideShow} />
          {showEmojiPicker && <Picker onEmojiClick={handleEmojiClick} />}
        </div>
      </div>
      <form className="input-container" onSubmit={(event) => sendChat(event)}>
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
    </Container>
  );
}

const Container = styled.div`
  display: grid;
  align-items: center;
  grid-template-columns: 5% 95%;
  background-color: #333; 
  position: relative; 
  padding-right: 4rem;
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
    color: white;
    gap: 1rem;
    .emoji {
      position: relative;
      svg {
        cursor: pointer;
        font-size: 2rem; 
        margin: 0 0.5rem; 
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
  }
  .input-container {
    width: 100%;
    border-radius: 2rem;
    display: flex;
    align-items: center;
    overflow: hidden;
    gap: 2rem;
    background-color: #fff;
    input {
      flex: 1;
      width: 90%;
      height: 2.5rem;
      color: black;
      padding-left:1rem;
      border: none;
      margin-left: 0.5rem;
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
      width: 4rem;
      height: 3rem;
      right: -2rem;
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


