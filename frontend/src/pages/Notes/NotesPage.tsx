// src/pages/Notes/NotesPage.tsx

import { Box, CircularProgress } from '@mui/material';
import { Helmet } from 'react-helmet-async';
import { useTranslation } from 'react-i18next';
import { useSelector } from 'react-redux';

import { selectCurrentUser } from '../../store/slices/authSlice';
import UserNotes from '../Users/UserNotes';

export default function NotesPage() {
  const { t } = useTranslation('notesPage');

  // Current logged user
  const user = useSelector(selectCurrentUser);
  const userId = user?.id;
  const careerId = user?.career?.id;
  const institutionId = user?.institution?.id;

  return (
    <>
      <Helmet>
        <title>{t('titlePage')}</title>
      </Helmet>
      {!userId || !careerId || !institutionId ? (
        <Box display="flex" justifyContent="center" alignItems="center">
          <CircularProgress />
        </Box>
      ) : (
        <UserNotes
          userId={userId}
          careerId={careerId}
          institutionId={institutionId}
        />
      )}
    </>
  );
}
