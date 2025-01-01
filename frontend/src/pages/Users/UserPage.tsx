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
  const { t } = useTranslation('userPage');

  useEffect(() => {
    if (userId && currentUser && userId === currentUser.id) {
      navigate('/profile', { replace: true });
    }
  }, [userId, currentUser, navigate]);

  const {
    data: targetUser,
    isLoading,
    isError,
    error,
  } = useGetUserQuery({ userId }, { skip: !userId });

  const institutionUrl = targetUser?.institutionUrl;
  const { data: institutionData } = useGetInstitutionQuery(
    { url: institutionUrl },
    { skip: !targetUser?.institutionUrl },
  );

  const careerUrl = targetUser?.careerUrl;
  const { data: careerData } = useGetCareerQuery(
    { url: careerUrl },
    { skip: !targetUser?.careerUrl },
  );

  const targetUserWithDetails = {
    ...targetUser,
    career: careerData,
    institution: institutionData,
  } as User;

  let pageTitle = t('titlePage', {
    username: targetUser?.username || '',
  });
  if (isLoading) {
    pageTitle = t('loading');
  } else if (isError) {
    pageTitle = t('errorFetchingUser', { error: String(error) });
  } else if (!targetUser) {
    pageTitle = t('userNotFound');
  }

  return (
    <>
      <Helmet>
        <title>{pageTitle}</title>
      </Helmet>
      <Box sx={{ p: 2 }}>
        {isLoading && (
          <Box sx={{ display: 'flex', justifyContent: 'center' }}>
            <CircularProgress />
          </Box>
        )}

        {isError && (
          <Typography variant="h6" color="error">
            {t('errorFetchingUser', { error: String(error) })}
          </Typography>
        )}

        {!isLoading && !isError && !targetUser && (
          <Typography variant="h6">{t('userNotFound')}</Typography>
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
