import { useQuery } from 'react-query';
import axios from 'axios';
import { getMessageByIdRoute } from "../utils/APIRoutes"; // Import route lấy dữ liệu người dùng và cập nhật

const endpoint = async (query) => {
    const response = await axios.get(getMessageByIdRoute(query));
    return response.data;
};

const useGetMessageById = (query) => {
    return useQuery([query], () => endpoint(query));

};

export { useGetMessageById };
