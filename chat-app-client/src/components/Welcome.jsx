import React, { useState, useEffect } from "react";
import styled from "styled-components";
import Robot from "../assets/robot.gif";
import {  getCurrentUserLocal } from "../utils/LocalStorage"
export default function Welcome() {
  const currentUser = getCurrentUserLocal();
  return (
    <Container>
      <img src={Robot} alt="" />
      <h1>
        Welcome, <span>{currentUser?.Display_name}!</span>
      </h1>
      <h3>Please select a chat to Start messaging.</h3>
    </Container>
  );
}

const Container = styled.div`
  display: flex;
  justify-content: center;
  align-items: center;
  color: #0f0c29;
  flex-direction: column;
  img {
    height: 20rem;
  }
  span {
    color: #4e0eff;
  }
`;
