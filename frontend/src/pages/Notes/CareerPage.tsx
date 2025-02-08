// src/pages/Notes/CareerPage.tsx

import { Box, CircularProgress } from '@mui/material';
import { Helmet } from 'react-helmet-async';
import { useTranslation } from 'react-i18next';
import { useSelector } from 'react-redux';

import { selectCurrentUser } from '../../store/slices/authSlice';
import UserNotes from '../Users/UserNotes';

export default function CareerPage() {
  const { t } = useTranslation('careerPage');

  // Current logged user
  const user = useSelector(selectCurrentUser);

  return (
    <>
      <Helmet>
        <title>{t('titlePage')}</title>
      </Helmet>
      {!user?.career ? (
        <Box display="flex" justifyContent="center" alignItems="center">
          <CircularProgress />
        </Box>
      ) : (
        <UserNotes career={user?.career} />
      )}
    </>
  );
}
