import React, { useState, useEffect } from "react";
import axios from "axios";
import styled from "styled-components";
import { useNavigate, Link } from "react-router-dom";
import Logo from "../assets/logo.svg";
import { ToastContainer, toast } from "react-toastify";
import "react-toastify/dist/ReactToastify.css";
import { registerRoute } from "../utils/APIRoutes";
import { setCurrentUserLocal, getCurrentUserLocal } from "../utils/LocalStorage"

export default function Register() {
  const navigate = useNavigate();
  const toastOptions = {
    position: "bottom-right",
    autoClose: 8000,
    pauseOnHover: true,
    draggable: true,
    theme: "dark",
  };
  const [values, setValues] = useState({
    Display_Name: "",
    Email: "",
    Tag: "",
    Birth: "",
    Password: "",
    confirmPassword: "",
  });

  useEffect(() => {
    if (getCurrentUserLocal()) {
      setCurrentUserLocal({})
    }
  }, []);

  const handleChange = (event) => {
    setValues({ ...values, [event.target.name]: event.target.value });
  };

  const handleValidation = () => {
    const { Password, confirmPassword, Display_Name, Email, Birth, Tag } = values;
    if (Password !== confirmPassword) {
      toast.error(
        "Mật khẩu và nhắc lại mật khẩu không giống nhau",
        toastOptions
      );
      return false;
    } else if (Display_Name.length < 3) {
      toast.error(
        "Tên người dùng phải lớn hơn 3 ký tự.",
        toastOptions
      );
      return false;
    } else if (Password.length < 8) {
      toast.error(
        "Mật khẩu phải bằng hoặc lớn hơn 8 ký tự.",
        toastOptions
      );
      return false;
    } else if (Email === "") {
      toast.error("Email Bắt buộc", toastOptions);
      return false;
    }

    return true;
  };

  const handleSubmit = async (event) => {
    event.preventDefault();
    if (handleValidation()) {
      const { Email, Display_Name, Password, Birth, Tag } = values;
      const { data, status, statusText } = await axios.post(registerRoute, {
        Display_Name,
        Email,
        Password,
        Birth,
        Tag
      });
      
      if (status === 200) {
        setCurrentUserLocal(data);
        navigate("/");
       
      } else {
        toast.error(statusText, toastOptions);
      }
    }
  };

  return (
    <>
      <FormContainer>
        <form action="" onSubmit={(event) => handleSubmit(event)}>
          <div className="brand">
            <img src={Logo} alt="logo" />
            <h1>App chat</h1>
          </div>
          <input
            type="text"
            placeholder="Username"
            name="Display_Name"
            onChange={(e) => handleChange(e)}
          />
          <input
            type="email"
            placeholder="Email"
            name="Email"
            onChange={(e) => handleChange(e)}
          />
          <input
            type="text"
            placeholder="tag"
            name="Tag"
            onChange={(e) => handleChange(e)}
          />
          <input
            type="date"
            placeholder="Birth"
            name="Birth"
            onChange={(e) => handleChange(e)}
          />
          <input
            type="password"
            placeholder="Password"
            name="Password"
            onChange={(e) => handleChange(e)}
          />
          <input
            type="password"
            placeholder="Confirm Password"
            name="confirmPassword"
            onChange={(e) => handleChange(e)}
          />
          <button type="submit">Create User</button>
          <span>
            bạn đã có tài khoản ? <Link to="/login">Login.</Link>
          </span>
        </form>
      </FormContainer>
      <ToastContainer />
    </>
  );
}

const FormContainer = styled.div`
  height: 100vh;
  width: 100vw;
  display: flex;
  flex-direction: column;
  justify-content: center;
  gap: 1rem;
  align-items: center;
  background-color: #fff;
  .brand {
    display: flex;
    align-items: center;
    gap: 1rem;
    justify-content: center;
    img {
      height: 5rem;
    }
    h1 {
      text-transform: uppercase;
    }
  }

  form {
    display: flex;
    flex-direction: column;
    gap: 2rem;
    background-color: #f8f9fa;
    border-radius: 2rem;
    padding: 3rem 5rem;
  }
  input {
    background-color: transparent;
    padding: 1rem;
    border: 0.1rem solid #4e0eff;
    border-radius: 0.4rem;
    width: 100%;
    font-size: 1rem;
    &:focus {
      border: 0.1rem solid #997af0;
      outline: none;
    }
  }
  button {
    background-color: #4e0eff;
    color: white;
    padding: 1rem 2rem;
    border: none;
    font-weight: bold;
    cursor: pointer;
    border-radius: 0.4rem;
    font-size: 1rem;
    text-transform: uppercase;
    &:hover {
      background-color: #4e0eff;
    }
  }
  span {
    text-transform: uppercase;
    a {
      color: #4e0eff;
      text-decoration: none;
      font-weight: bold;
    }
  }
`;
