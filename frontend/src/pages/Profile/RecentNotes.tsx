// src/pages/Profile/RecentNotes.tsx

import { Box, Typography, Button, CircularProgress } from '@mui/material';
import React from 'react';
import { useTranslation } from 'react-i18next';
import { Link as RouterLink } from 'react-router-dom';

import NoteCard from '../../components/NoteCard';
import { useGetLatestNotesQuery } from '../../store/slices/notesApiSlice';
import { Note } from '../../types';

interface RecentNotesProps {
  userId?: string;
}

const RecentNotes: React.FC<RecentNotesProps> = ({ userId }) => {
  const { t } = useTranslation('recentNotes');

  const { data, isLoading, error } = useGetLatestNotesQuery({ userId });

  if (isLoading) {
    return (
      <Box sx={{ display: 'flex', alignItems: 'center' }}>
        <CircularProgress size={24} />{' '}
        <Typography sx={{ ml: 2 }}>{t('loading')}</Typography>
      </Box>
    );
  }

  if (error) {
    return (
      <Typography variant="body1" color="error">
        {t('errorFetchingNotes')}
      </Typography>
    );
  }

  const hasNotes = data && data.length > 0;

  return (
    <Box>
      <Box sx={{ display: 'flex', justifyContent: 'space-between', mb: 2 }}>
        <Typography variant="h6">{t('recentNotes')}</Typography>
        {hasNotes && (
          <Button
            component={RouterLink}
            to="/notes"
            variant="outlined"
            size="small"
          >
            {t('viewMore')}
          </Button>
        )}
      </Box>
      {hasNotes ? (
        <Box>
          {data.slice(0, 5).map((note: Note) => (
            <NoteCard key={note.id} note={note} />
          ))}
        </Box>
      ) : (
        <Typography>{t('noRecentNotes')}</Typography>
      )}
    </Box>
  );
};

export default RecentNotes;
