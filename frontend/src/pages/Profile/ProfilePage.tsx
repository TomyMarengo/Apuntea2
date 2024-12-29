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
import { RootState } from '../../store/store';

const ProfilePage: React.FC = () => {
  const { t } = useTranslation();
  const userId = useSelector((state: RootState) => selectCurrentUserId(state));

  const {
    data: user,
    isLoading,
    error,
    refetch,
  } = useGetLoggedUserQuery({ userId }, { skip: !userId });

  if (isLoading) {
    return (
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
    );
  }

  if (error) {
    return (
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
    );
  }

  if (!user) {
    return (
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
    );
  }

  return (
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
  );
};

export default ProfilePage;
