import React, { useState } from "react";
import styled from "styled-components";
import { Form, Input } from "antd";
import { useUpdateMessageGroup } from "../hooks/useUpdateMessageGroup";

function UpdateNameMG({ handleClose, groupId, updateGroupName }) {
  const { mutateAsync: updateMessageGroup } = useUpdateMessageGroup();

  const onFinish = async (values) => {
    const data = {
      ...values,
      _id: groupId,
    };
    const res = await updateMessageGroup(data);

    if (!res) return;

    handleClose();
    updateGroupName(data);
  };

  const onFinishFailed = (errorInfo) => {
    console.log("Failed:", errorInfo);
  };

  return (
    <DialogOverlay>
      <DialogContainer>
        <DialogTitle>Chỉnh sửa tên nhóm</DialogTitle>
        <DialogContent>
          <InputWrapper>
            <label htmlFor="newGroupName">Tên nhóm mới:</label>
            <Form
              name="basic"
              onFinish={onFinish}
              labelCol={{ span: 4 }}
              onFinishFailed={onFinishFailed}
              autoComplete="off"
            >
              <Form.Item
                name="Message_group_name"
                rules={[
                  {
                    required: true,
                    message: "Vui lòng nhập tên!",
                  },
                ]}
              >
                <Input />
              </Form.Item>

              <Form.Item
                wrapperCol={{
                  offset: 8,
                  span: 16,
                }}
              >
                <Button htmlType="submit">Cập nhật</Button>
              </Form.Item>
            </Form>
          </InputWrapper>
        </DialogContent>
      </DialogContainer>
    </DialogOverlay>
  );
}

export default UpdateNameMG;

const DialogOverlay = styled.div`
  position: fixed;
  top: 0;
  left: 0;
  width: 100%;
  height: 100%;
  background-color: rgba(0, 0, 0, 0.5); /* Màu đen làm mờ */
  display: flex;
  justify-content: center;
  align-items: center;
  z-index: 999;
`;

const DialogContainer = styled.div`
  background-color: #0f0c29;
  color: #fff;
  border-radius: 8px;
  padding: 20px;
  width: 400px;
`;

const DialogTitle = styled.div`
  font-size: 20px;
  font-weight: bold;
  margin-bottom: 10px;
  color: #fff;
`;

const DialogContent = styled.div`
  margin-bottom: 20px;
`;

const InputWrapper = styled.div`
    margin-bottom: 10px;

    label {
        display: block;
        margin-bottom: 5px;
    }

    input {
        width: 100%;
        padding: 8px;
        border: 1px solid #ccc;
        color: 'dark'
        border-radius: 5px;
    }
`;

const DialogActions = styled.div`
  display: flex;
  justify-content: flex-end;
`;

const Button = styled.button`
  margin-left: 10px;
  padding: 8px 16px;
  border: none;
  border-radius: 5px;
  background-color: #007bff; /* Màu nền xanh */
  color: #fff;
  cursor: pointer;

  &:hover {
    background-color: #0056b3; /* Màu nền xanh nhạt khi hover */
  }
`;
