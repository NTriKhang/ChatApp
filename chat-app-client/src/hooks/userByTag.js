import { useState, useEffect } from 'react';
import axios from 'axios';
import { getUsersByTag } from '../utils/APIRoutes';

const UserByTag = (tag) => {
  const [users, setUsers] = useState([]);
  const [loading, setLoading] = useState(true);

  useEffect(() => {
    const fetchUsers = async () => {
      try {
        const response = await axios.get(getUsersByTag(tag));
        setUsers(response.data);
        setLoading(false);
      } catch (error) {
        console.error('Error fetching users:', error);
        setLoading(false);
      }
    };

    fetchUsers();
  }, [tag]);

  return { users, loading };
};

export default UserByTag;
