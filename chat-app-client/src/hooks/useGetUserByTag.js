import { useQuery } from 'react-query';
import axios from 'axios';
import { getUserByTagRoute } from "../utils/APIRoutes"; // Import route lấy dữ liệu người dùng và cập nhật

const endpoint = async (tag) => {
    //const response = await axios.get(getUserByTagRoute(tag));
    //return response.data;
};

const useGetUserByTag = (tag) => {
    return useQuery(['user-by-tag', tag], () => endpoint(tag));

};

export { useGetUserByTag };
