import { Button, Form, Image, Input, Modal, Select, Space } from "antd";
import { useState, useEffect } from "react";
import { useGetUserByTag } from "../../hooks/useGetUserByTag";
import { debounce } from "lodash";
import { getCurrentUserLocal } from "../../utils/LocalStorage";

const AddGroupModal = ({ isShow, onCancel, stompClient }) => {
  const [search, setSearch] = useState("");
  const [listOptions, setListOptions] = useState([]);

  const handleChange = (value) => {
    console.log(`selected ${value}`);
  };

  const { data: userTag } = useGetUserByTag(search);

  useEffect(() => {
    if (userTag) {
      const updatedOptions = userTag.map((tag) => ({
        label: tag.Tag,
        value: tag._id,
        image: tag.Image_path,
      }));
      setListOptions(updatedOptions);
    } else {
      setListOptions([])
    }
  }, [userTag, search]);

  const debouncedSearch = debounce((value) => {
    setSearch(value);
  }, 300);

  function count(arr) {
    let count = 0;
    for (let i = 0; i < arr.length; i++) {
        count++;
    }
    return count;
}
  const onFinishAddGroup = async (values) => {
    if(values.userList.length < 2){
      alert('at least two users chosen')
    }
    else{
      let createGroupRequest = {
        userCreatedId: getCurrentUserLocal()['_id'],
        groupName: values.groupName,
        userList: values.userList
      }
      stompClient.send("/app/CreateGroup", {}, JSON.stringify(createGroupRequest))
      oncancel()
    }

  };

  const onFinishFailed = (errorInfo) => {
    console.log("Failed:", errorInfo);
  };

  return (
    <Modal
      title="Tạo Group mới"
      visible={isShow}
      onCancel={onCancel}
      footer={null}
    >
      <div className="bg-white overflow-hidden rounded-lg mt-4">
        <Form
          name="basic"
          onFinish={onFinishAddGroup}
          labelCol={{ span: 4 }}
          onFinishFailed={onFinishFailed}
          autoComplete="off"
        >
          <Form.Item
            name="groupName"
            rules={[
              {
                required: true,
                message: "Vui lòng nhập tên group!",
              },
            ]}
          >
            <Input placeholder="Nhập tên group" />
          </Form.Item>

          <Form.Item
            name="userList"
            label={<div>Add user</div>}
            rules={[
              {
                required: true,
                message: "Vui lòng chọn user",
              },
            ]}
          >
            <Select
              mode="multiple"
              onSearch={(value) => debouncedSearch(value)}
              onChange={handleChange}
              optionLabelProp="label"
              value={search}
              filterOption={false}
            >
              {listOptions.map((option) => (
                <Select.Option
                  key={option.value}
                  value={option.value}
                  label={option.label}
                >
                  <Space>
                    <Image
                      src={option.image}
                      width={20}
                      height={20}
                      preview={false}
                    />
                    {option.label}
                  </Space>
                </Select.Option>
              ))}
            </Select>
          </Form.Item>

          <Form.Item className="flex justify-center">
            <Button htmlType="submit">Tạo</Button>
          </Form.Item>
        </Form>
      </div>
    </Modal>
  );
};

export { AddGroupModal };
