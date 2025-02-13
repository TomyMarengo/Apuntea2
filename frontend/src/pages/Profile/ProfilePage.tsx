// src/pages/Profile/ProfilePage.tsx

import { Box, CircularProgress, Typography, Divider } from '@mui/material';
import React from 'react';
import { Helmet } from 'react-helmet-async';
import { useTranslation } from 'react-i18next';
import { useSelector } from 'react-redux';

import ProfileCard from './ProfileCard';
import RecentNotes from './RecentNotes';
import RecentReviews from './RecentReviews';
import { selectCurrentUserId } from '../../store/slices/authSlice';
import { useGetLoggedUserQuery } from '../../store/slices/usersApiSlice';

const ProfilePage: React.FC = () => {
  const { t } = useTranslation('profilePage');
  const userId = useSelector(selectCurrentUserId);

  const {
    data: user,
    isLoading,
    isError,
    refetch,
  } = useGetLoggedUserQuery({ userId }, { skip: !userId });

  let pageTitle = t('titlePage');
  if (isLoading) {
    pageTitle = t('loading');
  } else if (isError) {
    pageTitle = t('errorFetchingUser');
  } else if (!user) {
    pageTitle = t('noUserFound');
  }

  return (
    <>
      <Helmet>
        <title>{pageTitle}</title>
      </Helmet>
      <Box sx={{ p: 4, maxWidth: 1200, margin: '0 auto' }}>
        {isLoading ? (
          <Box
            sx={{
              display: 'flex',
              justifyContent: 'center',
              alignItems: 'center',
              minHeight: '80vh',
            }}
          >
            <CircularProgress />
          </Box>
        ) : isError ? (
          <Box
            sx={{
              display: 'flex',
              justifyContent: 'center',
              alignItems: 'center',
              minHeight: '80vh',
            }}
          >
            <Typography variant="h6" color="error">
              {t('errorFetchingUser')}
            </Typography>
          </Box>
        ) : !user ? (
          <Box
            sx={{
              display: 'flex',
              justifyContent: 'center',
              alignItems: 'center',
              minHeight: '80vh',
            }}
          >
            <Typography variant="h6">{t('noUserFound')}</Typography>
          </Box>
        ) : (
          <Box sx={{ maxWidth: 1200, margin: '0 auto' }}>
            <Typography variant="h4" gutterBottom>
              {t('profile')}
            </Typography>

            <ProfileCard user={user} onUpdateSuccess={refetch} />

            <Divider sx={{ my: 4 }} />

            <RecentNotes userId={user.id} />

            <Divider sx={{ my: 4 }} />

            <RecentReviews targetId={user.id} />
          </Box>
        )}
      </Box>
    </>
  );
};

export default ProfilePage;
