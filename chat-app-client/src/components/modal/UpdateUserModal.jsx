import { Button, DatePicker, Form, Input, Modal, Select, Space } from "antd";
import { useState } from "react";
import { setCurrentUserLocal } from "../../utils/LocalStorage";
import moment from "moment";
import { useUpdateUser } from "../../hooks/useUpdateUser";

const UpdateUserModal = ({ isShow, onCancel, currentUser }) => {
  const { mutateAsync: updateUser } = useUpdateUser();

  const initialValues = {
    DisplayName: currentUser.Display_name,
    Email: currentUser.Email,
    Tag: currentUser.Tag,
    Id: currentUser._id,
    Birth: moment(currentUser.Birth, "YYYY-MM-DD"),
  };

  const onFinish = async (values) => {
    const res = await updateUser({
      ...values,
      Id: currentUser._id,
      Birth: moment(values.Birth),
    });

    if (!res) return;

    setCurrentUserLocal({
      ...currentUser,
      Display_name: values.DisplayName,
      Email: values.Email,
      Tag: values.Tag,
      Birth: values.Birth,
    });
    onCancel();
  };

  const onFinishFailed = (errorInfo) => {
    console.log("Failed:", errorInfo);
  };

  return (
    <Modal
      title="cập nhật thông tin"
      open={isShow}
      cancelText="Lưu"
      okButtonProps={{ hidden: true }}
      cancelButtonProps={{ hidden: true }}
      onCancel={onCancel}
    >
      <div className="bg-white overflow-hidden rounded-lg mt-4">
        <Form
          initialValues={initialValues}
          name="basic"
          onFinish={onFinish}
          labelCol={{ span: 4 }}
          onFinishFailed={onFinishFailed}
          autoComplete="off"
        >
          <Form.Item
            label="Tên"
            name="DisplayName"
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
            label="Email"
            name="Email"
            rules={[
              {
                required: true,
                message: "Vui lòng nhập Email!",
              },
            ]}
          >
            <Input />
          </Form.Item>

          <Form.Item
            label="Tag"
            name="Tag"
            rules={[
              {
                required: true,
                message: "Vui lòng nhập Tag!",
              },
            ]}
          >
            <Input />
          </Form.Item>

          <Form.Item
            label="Ngày sinh"
            name="Birth"
            rules={[
              {
                required: true,
                message: "Vui lòng nhập ngày sinh!",
              },
            ]}
          >
            <DatePicker />
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
      </div>
    </Modal>
  );
};

export { UpdateUserModal };
