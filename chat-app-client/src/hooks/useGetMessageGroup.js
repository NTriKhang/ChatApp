import { useQuery } from 'react-query';
import axios from 'axios';
import { getMessageGroupRoute } from "../utils/APIRoutes"; // Import route lấy dữ liệu người dùng và cập nhật
import { getConnectStateLocal, setConnectStateLocal } from '../utils/LocalStorage';

const endpoint = async (id) => {
    const connectStateString = getConnectStateLocal();
    const response = await axios.get(getMessageGroupRoute(id, connectStateString));
    setConnectStateLocal(true)
    return response.data;
};

const useGetMessageGroup = (id) => {
    return useQuery(['message-group-list', id], () => endpoint(id));

};

export { useGetMessageGroup };
