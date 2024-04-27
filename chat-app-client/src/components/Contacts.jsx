import React, { useState, useEffect } from "react";
import {
  Modal,
  Image,
  Form,
  Input,
  Checkbox,
  Button,
  DatePicker,
  Select,
  Space,
} from "antd";
import styled from "styled-components";
import Logo from "../assets/logo.svg";

import {
  getConnectStateLocal,
  getCurrentUserLocal,
  setConnectStateLocal,
  setCurrentUserLocal,
} from "../utils/LocalStorage";
import axios from "axios";
import moment from "moment";
import SearchBar from "./SearchBar";
import userByTag from "../hooks/userByTag";
import { useUpdateUser } from "../hooks/useUpdateUser";
import { useUploadImageUser } from "../hooks/useUploadImageUser";
import { useUploadBackgroundImageUser } from "../hooks/useUploadBackgroundImageUser";
import { UploadImage } from "./upload/UploadImage";
import { useGetUserByTag } from "../hooks/useGetUserByTag";
import { AddGroup, AddGroupModal } from "./modal/AddGroupModal";
import { UpdateUserModal } from "./modal/UpdateUserModal";

export default function Contacts({ changeChat, messageGroup, currentChat, stompClient, changeSelectedSearch}) {
  const [isModalOpen, setIsModalOpen] = useState(false);
  const [isModalOpenAvatar, setIsModalOpenAvatar] = useState(false);
  const [isModalOpenBackground, setIsModalOpenBackground] = useState(false);
  const [isModalUpdateOpen, setIsModalUpdateOpen] = useState(false);
  const [isModalAddGroupOpen, setIsModalAddGroupOpen] = useState(false);

  const [titleChat, setTitleChat] = useState("");
  const currentUser = getCurrentUserLocal();

  const [currentUserImage, setCurrentUserImage] = useState("");
  const [currentSelected, setCurrentSelected] = useState(null);

  const { mutateAsync: uploadImage } = useUploadImageUser();
  const { mutateAsync: uploadBackground } = useUploadBackgroundImageUser();
  
  const [searchTerm, setSearchTerm] = useState("");
  const { users, loading } = userByTag(searchTerm);
  const [showUserInfo, setShowUserInfo] = useState(true);

  // gọi hàm "refetch" phía trên để call lại api

  const changeCurrentChat = (contact, index) => {
    changeChat(index, contact);
    setTitleChat({ contact, index });
  };
  const onSelectedUserSearch = (user) => {
    changeSelectedSearch(user)
  }
  useEffect(() => {
    if (currentChat !== null) {
      changeChat(0, {
        ...titleChat?.contact,
        Message_group_name:
          messageGroup?.[titleChat?.index]?.Message_group_name,
      });
    }
  }, [titleChat, messageGroup]);

  const handleCancel = () => {
    setIsModalOpen(false);
  };

  const handleEdit = () => {
    setIsModalUpdateOpen(true);
  };

  const onUpdateImage = async (values) => {
    if (values?.url) {
      const res = await uploadImage({
        id: currentUser._id,
        url: {
          imageUrl: values?.url,
        },
      });
      if (!res) return;
    }
  };

  const onUpdateBackground = async (values) => {
    if (values?.url) {
      const res = await uploadBackground({
        id: currentUser._id,
        url: {
          imageUrl: values?.url,
        },
      });
      if (!res) return;
    }
  };

  const handleSearch = (term) => {
    setShowUserInfo(term.length !== 0);
    setSearchTerm(term);
  };

  return (
    <>
      <Container>
        <div className="brand bg flex justify-between w-full">
          <div className="flex">
            <img src={Logo} alt="logo" />
            <h3>App chat</h3>
          </div>
          <div
            className="cursor-pointer"
            onClick={() => setIsModalAddGroupOpen(true)}
          >
            <span class="material-symbols-outlined text-white">group_add</span>
          </div>
        </div>
        <SearchBar onSearch={handleSearch}/>{" "}
        {/* Insert the SearchBar component here */}
        {showUserInfo && !loading && (
          <UserInfoBox>
            <ul>
              {users.map((user) => (
                <li key={user._id} onClick={() => onSelectedUserSearch(user)}>
                  <div className="avatar">
                    <img src={user.Image_path} alt={user.Display_name} />
                  </div>
                  <div className="username">{user.Tag}</div>
                </li>
              ))}
            </ul>
          </UserInfoBox>
        )}
        <div className="contacts">
          {messageGroup?.map((contact, index) => (
            <div
              key={contact.MessageGroupId}
              className={`contact ${
                index === currentSelected ? "selected" : ""
              }`}
              onClick={() => changeCurrentChat(contact, index)}
            >
              <div className="avatar">
                {contact.Message_group_image ? (
                  <img src={contact.Message_group_image} alt="" />
                ) : (
                  <img
                    src="https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcSUdO2qhODLgmxWPYWgpV9P4BOqAGx5-LNM0A&usqp=CAU"
                    alt="Defaut Image"
                  />
                )}
              </div>
              <div className="username">
                <h3>{contact.username}</h3>
                <h3>{contact.Message_group_name}</h3>
                {contact.last_message ? (
                  <p>
                    {contact.last_message.length >= 26
                      ? contact.last_message.slice(0, 21) + "..."
                      : contact.last_message}
                  </p>
                ) : (
                  <p>Chưa có tin nhắn cuối</p>
                )}
                {contact.is_read ? <span>✅</span> : <span>❌</span>}
              </div>
            </div>
          ))}
        </div>
        <div className="current-user ">
          <div className="flex justify-between w-full">
            <div className="flex flex-column items-center">
              <div className="avatar">
                {currentUserImage ? (
                  <img src={currentUser.Image_path} alt="avatar" />
                ) : (
                  <img src={currentUser.Image_path} alt="avatar" />
                )}
              </div>
              <div className="username">
                <h2 className="text-white">{currentUser?.Display_name}</h2>
              </div>
            </div>
            <div className="flex flex-column items-center cursor-pointer">
              <span
                className="material-symbols-outlined text-white"
                onClick={() => setIsModalOpen(true)}
              >
                settings
              </span>
              <Modal
                title={
                  <div>
                    <span className="me-5">Thông tin người dùng</span>
                    <span
                      class="material-symbols-outlined cursor-pointer "
                      onClick={handleEdit}
                    >
                      edit
                    </span>
                  </div>
                }
                open={isModalOpen}
                cancelText="Thoát"
                okButtonProps={{ hidden: true }}
                onCancel={handleCancel}
              >
                <div className="bg-white overflow-hidden shadow rounded-lg border">
                  <div
                    className="px-4 py-5 sm:px-6"
                    style={{
                      backgroundImage: `url(${currentUser.Background_image_path})`,
                      backgroundRepeat: "no-repeat",
                      backgroundSize: "cover",
                    }}
                  >
                    <Image
                      width={60}
                      height={60}
                      style={{ objectFit: "cover" }}
                      src={currentUser.Image_path}
                    />
                    <span
                      class="material-symbols-outlined cursor-pointer "
                      onClick={() => setIsModalOpenAvatar(true)}
                    >
                      edit
                    </span>
                    <Modal
                      title="Thay đổi avatar"
                      open={isModalOpenAvatar}
                      cancelText="Lưu"
                      width="180px"
                      okButtonProps={{ hidden: true }}
                      cancelButtonProps={{ hidden: true }}
                      onCancel={() => setIsModalOpenAvatar(false)}
                    >
                      <UploadImage onChangeImage={onUpdateImage} />
                    </Modal>
                    <h3 className="text-lg leading-6 font-medium text-white">
                      {currentUser.Display_name}
                    </h3>
                    <p className="mt-1 max-w-2xl text-sm text-white">
                      {currentUser.Email}
                    </p>
                    <span
                      class="material-symbols-outlined cursor-pointer "
                      onClick={() => setIsModalOpenBackground(true)}
                    >
                      edit
                    </span>
                    <Modal
                      title="Thay đổi background"
                      open={isModalOpenBackground}
                      cancelText="Lưu"
                      width="180px"
                      okButtonProps={{ hidden: true }}
                      cancelButtonProps={{ hidden: true }}
                      onCancel={() => setIsModalOpenBackground(false)}
                    >
                      <UploadImage onChangeImage={onUpdateBackground} />
                    </Modal>
                  </div>

                  <div className="border-t border-gray-200 px-4 py-5 sm:p-0">
                    <dl className="sm:divide-y sm:divide-gray-200">
                      <div className="py-3 sm:py-5 sm:grid sm:grid-cols-3 sm:gap-4 sm:px-6">
                        <dt className="text-sm font-medium text-gray-500">
                          Tên
                        </dt>
                        <dd className="mt-1 text-sm text-gray-900 sm:mt-0 sm:col-span-2">
                          {currentUser.Display_name}
                        </dd>
                      </div>
                      <div className="py-3 sm:py-5 sm:grid sm:grid-cols-3 sm:gap-4 sm:px-6">
                        <dt className="text-sm font-medium text-gray-500">
                          Email
                        </dt>
                        <dd className="mt-1 text-sm text-gray-900 sm:mt-0 sm:col-span-2">
                          {currentUser.Email}
                        </dd>
                      </div>
                      <div className="py-3 sm:py-5 sm:grid sm:grid-cols-3 sm:gap-4 sm:px-6">
                        <dt className="text-sm font-medium text-gray-500">
                          Ngày sinh
                        </dt>
                        <dd className="mt-1 text-sm text-gray-900 sm:mt-0 sm:col-span-2">
                          {moment(currentUser.Birth).format("DD/MM/YYYY")}
                        </dd>
                      </div>
                      <div className="py-3 sm:py-5 sm:grid sm:grid-cols-3 sm:gap-4 sm:px-6">
                        <dt className="text-sm font-medium text-gray-500">
                          Tag
                        </dt>
                        <dd className="mt-1 text-sm text-gray-900 sm:mt-0 sm:col-span-2">
                          {currentUser.Tag}
                        </dd>
                      </div>
                    </dl>
                  </div>
                </div>
              </Modal>
            </div>
          </div>
        </div>
      </Container>
      <UpdateUserModal
        isShow={isModalUpdateOpen}
        onCancel={() => setIsModalUpdateOpen(false)}
        currentUser={currentUser}
      />
      <AddGroupModal
        stompClient={stompClient}
        isShow={isModalAddGroupOpen}
        onCancel={() => setIsModalAddGroupOpen(false)}
      />
    </>
  );
}


const Container = styled.div`
  position: relative;
  display: flex;
  flex-direction: column;
  background-color: #0f0c29; /* Deep blue background */

  .brand {
    display: flex;
    align-items: center;
    padding: 20px;
    background-color: #302b63;
    border-left: 1px solid black;
    img {
      height: 40px;
    }
    h3 {
      margin-left: 10px;
      color: #eaeaea;
      font-size: 24px;
    }
  }

  .contacts {
    overflow-y: auto;
    padding: 20px;
    display: grid;
    grid-template-columns: repeat(auto-fill, minmax(250px, 1fr));
    gap: 15px;
    max-height: 70vh;
    margin-bottom: 10px;
    &::-webkit-scrollbar {
      width: 5px;
    }
    &::-webkit-scrollbar-thumb {
      background: #5d5d5d;
    }
    .contact {
      flex-grow: 1;
      display: flex;
      align-items: center;
      background-color: #222034;
      padding: 10px;
      border-radius: 8px;
      cursor: pointer;
      transition: transform 0.2s ease-in-out;
      margin-bottom: 10px;
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

        h3,
        p {
          color: #ccc;
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
      background-color: #554e8f;
    }
  }

  .current-user {
    position: absolute;
    bottom: 0;
    left: 0; /* Đảm bảo phần tử nằm ở mép trái của màn hình */
    width: 100%; /* Phủ toàn bộ chiều rộng của màn hình */
    background-color: #222034;
    padding: 20px;
    display: flex;
    justify-content: center;
    align-items: center;
    z-index: 1;
    .avatar img {
      border-radius: 50%;
      width: 50px;
      height: 50px;
      max-width: 200px !important;
    }
    .username h2 {
      margin-left: 15px;
      color: #fff;
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
const UserInfoBox = styled.div`
  position: absolute;
  top: 130px;
  left: 10px;
  width: 85%;
  background-color: white;
  border: 1px solid #ccc;
  border-radius: 5px;
  z-index: 999;
  box-shadow: 0px 2px 5px rgba(0, 0, 0, 0.1);
  

  .avatar {
    width: 40px;
    height: 40px;
    border-radius: 50%;
    overflow: hidden;
    margin-right: 10px;

    img {
      width: 100%;
      height: 100%;
      object-fit: cover;
    }
  }

  ul {
    list-style-type: none;
    padding: 0;
    margin: 0;
  }

  li {
    display: flex;
    align-items: center;
    padding: 10px;
    border-bottom: 1px solid #ccc;

    &:last-child {
      border-bottom: none;
    }
  }
`;

