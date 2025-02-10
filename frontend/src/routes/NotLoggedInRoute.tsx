// src/routes/NotLoggedInRoute.tsx

import React, { JSX } from 'react';
import { useSelector } from 'react-redux';
import { Navigate } from 'react-router-dom';

import { selectCurrentUser } from '../store/slices/authSlice';

const NotLoggedInRoute: React.FC<{ children: JSX.Element }> = ({
  children,
}) => {
  const user = useSelector(selectCurrentUser);
  const isLoggedIn = !!user;

  return !isLoggedIn ? children : <Navigate to="/" replace />;
};

export default NotLoggedInRoute;
