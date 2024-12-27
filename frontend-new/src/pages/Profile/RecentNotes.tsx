// src/pages/Profile/RecentNotes.tsx

import React from 'react';
import { Box, Typography, Button, CircularProgress } from '@mui/material';
import { useTranslation } from 'react-i18next';
import { Link as RouterLink } from 'react-router-dom';
import { useGetLatestNotesQuery } from '../../store/slices/notesApiSlice';
import { Note } from '../../types';
import NoteCard from '../../components/NoteCard';

interface RecentNotesProps {
  userId?: string;
}

const RecentNotes: React.FC<RecentNotesProps> = ({ userId }) => {
  const { t } = useTranslation();

  const { data, isLoading, error } = useGetLatestNotesQuery({ userId });

  if (isLoading) {
    return (
      <Box sx={{ display: 'flex', alignItems: 'center' }}>
        <CircularProgress size={24} />{' '}
        <Typography sx={{ ml: 2 }}>{t('recentNotes.loading')}</Typography>
      </Box>
    );
  }

  if (error) {
    return (
      <Typography variant="body1" color="error">
        {t('recentNotes.errorFetchingNotes')}
      </Typography>
    );
  }

  return (
    <Box>
      <Box sx={{ display: 'flex', justifyContent: 'space-between', mb: 2 }}>
        <Typography variant="h6">{t('recentNotes.recentNotes')}</Typography>
        <Button
          component={RouterLink}
          to="/notes"
          variant="outlined"
          size="small"
        >
          {t('recentNotes.viewMore')}
        </Button>
      </Box>
      {data && data.length > 0 ? (
        <Box>
          {data.slice(0, 5).map((note: Note) => (
            <NoteCard key={note.id} note={note} />
          ))}
        </Box>
      ) : (
        <Typography>{t('recentNotes.noRecentNotes')}</Typography>
      )}
    </Box>
  );
};

export default RecentNotes;
