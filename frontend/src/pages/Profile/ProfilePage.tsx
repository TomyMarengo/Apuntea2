// src/pages/Profile/ProfilePage.tsx

import React from 'react';
import { Box, CircularProgress, Typography, Divider } from '@mui/material';
import { useTranslation } from 'react-i18next';
import ProfileCard from './ProfileCard';
import RecentNotes from './RecentNotes';
import RecentReviews from './RecentReviews';
import { useGetLoggedUserQuery } from '../../store/slices/usersApiSlice';
import { selectCurrentUserId } from '../../store/slices/authSlice';
import { useSelector } from 'react-redux';
import { Helmet } from 'react-helmet-async';

const ProfilePage: React.FC = () => {
  const { t } = useTranslation();
  const userId = useSelector(selectCurrentUserId);

  const {
    data: user,
    isLoading,
    isError,
    refetch,
  } = useGetLoggedUserQuery({ userId }, { skip: !userId });

  let pageTitle = t('profilePage.titlePage');
  if (isLoading) {
    pageTitle = t('profilePage.loading');
  } else if (isError) {
    pageTitle = t('profilePage.errorFetchingUser');
  } else if (!user) {
    pageTitle = t('profilePage.noUserFound');
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
              {t('profilePage.errorFetchingUser')}
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
            <Typography variant="h6">{t('profilePage.noUserFound')}</Typography>
          </Box>
        ) : (
          <Box sx={{ p: 4, maxWidth: 1200, margin: '0 auto' }}>
            <Typography variant="h4" gutterBottom>
              {t('profilePage.profile')}
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
