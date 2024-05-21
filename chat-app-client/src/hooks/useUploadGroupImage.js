import { useMutation } from 'react-query';
import axios from 'axios';
import { message } from 'antd'; // Import message từ antd
import { updateGroupImageRoute } from "../utils/APIRoutes";

const updateGroupImage = async (userData) => {
    await axios.post(updateGroupImageRoute + `/${userData?.id}`, userData?.url);
    return true;
};

const useUploadGroupImage = () => {
    return useMutation(
        (data) => updateGroupImage(data),
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

export { useUploadGroupImage };
