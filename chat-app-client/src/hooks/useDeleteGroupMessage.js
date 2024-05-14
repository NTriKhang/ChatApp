import { useMutation } from 'react-query';
import axios from 'axios';
import { message } from 'antd'; // Import message từ antd
import { deleteMessageGroupRoute } from "../utils/APIRoutes";

const deleteUser = async (payload) => {
    await axios.delete(deleteMessageGroupRoute, payload);
    return true;
};

const useDeleteGroupMessage = () => {
    return useMutation(
        (payload) => deleteUser(payload),
        {
            onSuccess: () => {
                message.success("Xóa người dùng thành công");
            },
            onError: () => {
                message.error("Xóa người dùng thất bại");
            },
        }
    );
};

export { useDeleteGroupMessage };
