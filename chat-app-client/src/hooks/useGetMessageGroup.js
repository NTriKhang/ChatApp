import { useQuery } from 'react-query';
import axios from 'axios';
import { updateUserMessageGroupRoute } from "../utils/APIRoutes"; // Import route lấy dữ liệu người dùng và cập nhật
import { getConnectStateLocal } from '../utils/LocalStorage';

const endpoint = async (id) => {
    const connectStateString = getConnectStateLocal();
    const response = await axios.get(updateUserMessageGroupRoute(id, connectStateString));
    return response.data;
};

const useGetMessageGroup = (id) => {
    return useQuery(['message-group-list', id], () => endpoint(id));

};

export { useGetMessageGroup };
