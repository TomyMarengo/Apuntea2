// src/pages/User/UserPage.tsx

import React, { useEffect } from 'react';
import { useParams, useNavigate } from 'react-router-dom';
import { Box, Typography, CircularProgress } from '@mui/material';
import { useSelector } from 'react-redux';
import { selectCurrentUser } from '../../store/slices/authSlice';
import { useGetUserQuery } from '../../store/slices/usersApiSlice';
import {
  useGetInstitutionQuery,
  useGetCareerQuery,
} from '../../store/slices/institutionsApiSlice';
import UserProfileCard from './UserProfileCard';
import UserNotes from './UserNotes';
import { useTranslation } from 'react-i18next';
import { User } from '../../types';
import { Helmet } from 'react-helmet-async';

const UserPage: React.FC = () => {
  const { userId } = useParams<{ userId: string }>();
  const navigate = useNavigate();
  const currentUser = useSelector(selectCurrentUser);
  const { t } = useTranslation();

  // Redirect to /profile if the userId is the logged-in user's ID
  useEffect(() => {
    if (userId && currentUser && userId === currentUser.id) {
      navigate('/profile', { replace: true });
    }
  }, [userId, currentUser, navigate]);

  // Fetch target user data
  const {
    data: targetUser,
    isLoading,
    isError,
    error,
  } = useGetUserQuery({ userId }, { skip: !userId });

  // Fetch institution data
  const institutionUrl = targetUser?.institutionUrl;
  const { data: institutionData } = useGetInstitutionQuery(
    { url: institutionUrl },
    { skip: !targetUser?.institutionUrl },
  );

  // Fetch career data
  const careerUrl = targetUser?.careerUrl;
  const { data: careerData } = useGetCareerQuery(
    { url: careerUrl },
    { skip: !targetUser?.careerUrl },
  );

  // Add career, institution and targetUser to user object
  const targetUserWithDetails = {
    ...targetUser,
    career: careerData,
    institution: institutionData,
  } as User;

  let pageTitle = t('userPage.title', { username: targetUser?.username || '' });
  if (isLoading) {
    pageTitle = t('userPage.loading');
  } else if (isError) {
    pageTitle = t('userPage.errorFetchingUser', { error: String(error) });
  } else if (!targetUser) {
    pageTitle = t('userPage.userNotFound');
  }

  return (
    <>
      <Helmet>
        <title>{pageTitle}</title>
      </Helmet>
      <Box sx={{ p: 2 }}>
        {/* Loading State */}
        {isLoading && (
          <Box sx={{ display: 'flex', justifyContent: 'center' }}>
            <CircularProgress />
          </Box>
        )}

        {/* Error State */}
        {isError && (
          <Typography variant="h6" color="error">
            {t('userPage.errorFetching', { error: String(error) })}
          </Typography>
        )}

        {/* User Not Found */}
        {!isLoading && !isError && !targetUser && (
          <Typography variant="h6">{t('userPage.userNotFound')}</Typography>
        )}

        {/* Main Content */}
        {!isLoading && !isError && targetUser && (
          <>
            {/* User Profile */}
            <UserProfileCard user={targetUserWithDetails} />

            {/* User Notes */}
            <UserNotes user={targetUserWithDetails} />
          </>
        )}
      </Box>
    </>
  );
};

export default UserPage;
