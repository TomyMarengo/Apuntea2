// src/pages/User/UserPage.tsx

import { Box, Typography, CircularProgress } from '@mui/material';
import React, { useEffect } from 'react';
import { Helmet } from 'react-helmet-async';
import { useTranslation } from 'react-i18next';
import { useSelector } from 'react-redux';
import { useParams, useNavigate } from 'react-router-dom';

import UserNotes from './UserNotes';
import UserProfileCard from './UserProfileCard';
import { selectCurrentUser } from '../../store/slices/authSlice';
import {
  useGetInstitutionQuery,
  useGetCareerQuery,
} from '../../store/slices/institutionsApiSlice';
import { useGetUserQuery } from '../../store/slices/usersApiSlice';
import { User } from '../../types';

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
      <Box sx={{ p: 4, maxWidth: 1200, margin: '0 auto' }}>
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
          <Box sx={{ maxWidth: 1200, margin: '0 auto' }}>
            <Typography variant="h4" gutterBottom>
              {t('profile', {
                username: targetUser?.username || targetUser?.email,
              })}
            </Typography>
            {/* User Profile */}
            <UserProfileCard user={targetUserWithDetails} />

            {/* User Notes */}
            <UserNotes user={targetUserWithDetails} />
          </Box>
        )}
      </Box>
    </>
  );
};

export default UserPage;
