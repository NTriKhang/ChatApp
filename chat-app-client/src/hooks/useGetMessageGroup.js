import { useQuery } from 'react-query';
import axios from 'axios';
import { getUserRoute } from "../utils/APIRoutes"; // Import route lấy dữ liệu người dùng và cập nhật

const endpoint = async (id) => {
    const response = await axios.get(getUserRoute);
    return response.data;
};

const useGetMessageGroup = (id) => {
    return useQuery(['message-group', id], endpoint(id));
};

export { useGetMessageGroup };
