import { useMutation } from 'react-query';
import axios from 'axios';
import { message } from 'antd'; // Import message từ antd
import { updateGroupImageRoute } from "../utils/APIRoutes";

const postMessageFile = async (stompClient, userData) => {
    await axios.post(updateGroupImageRoute + `/${userData?.id}`, userData?.url);
    return true;
};

const useUploadMessageFile = () => {
    return useMutation(
        (stompClient, data) => postMessageFile(stompClient, data),
        {
            onSuccess: (data) => {
                message.success("Cập nhật thành công");
            },
            onError: (error) => {
                message.error("Cập nhật thất bại");
            },
            onSettled: (data, error) => {
                // Không cần xử lý sau khi mutation kết thúc
            }
        }
    );
};

export { useUploadMessageFile };
