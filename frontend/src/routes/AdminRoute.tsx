// src/routes/AdminRoute.tsx

import React, { JSX } from 'react';
import { useSelector } from 'react-redux';
import { Navigate } from 'react-router-dom';

import { selectCurrentToken } from '../store/slices/authSlice';

const AdminRoute: React.FC<{ children: JSX.Element }> = ({ children }) => {
  const token = useSelector(selectCurrentToken);
  const isAdmin = token?.payload?.authorities.includes('ROLE_ADMIN');

  return isAdmin ? children : <Navigate to="/" replace />;
};

export default AdminRoute;
