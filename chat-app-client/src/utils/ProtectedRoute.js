import React from "react";
import { Navigate } from "react-router-dom";
import { getCurrentUserLocal } from "../utils/LocalStorage";

const ProtectedRoute = ({ children }) => {
  const user = getCurrentUserLocal();

  if (!user) {
    return <Navigate to="/login" />;
  }

  return children;
};

export default ProtectedRoute;