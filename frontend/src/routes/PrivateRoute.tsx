// src/routes/PrivateRoute.tsx

import React from 'react';
import { useSelector } from 'react-redux';
import { Navigate } from 'react-router-dom';
import { selectCurrentUser } from '../store/slices/authSlice';

const PrivateRoute: React.FC<{ children: JSX.Element }> = ({ children }) => {
  const user = useSelector(selectCurrentUser);
  const isLoggedIn = !!user;

  return isLoggedIn ? children : <Navigate to="/login" replace />;
};

export default PrivateRoute;
