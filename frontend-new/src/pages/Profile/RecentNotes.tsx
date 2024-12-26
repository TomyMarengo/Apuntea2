// src/pages/Profile/RecentNotes.tsx

import React from 'react';
import {
  Box,
  Typography,
  List,
  ListItem,
  ListItemText,
  Button,
  CircularProgress,
} from '@mui/material';
import { useTranslation } from 'react-i18next';
import { Link as RouterLink } from 'react-router-dom';
import { useGetLatestNotesQuery } from '../../store/slices/notesApiSlice';
import { Note } from '../../types';

interface RecentNotesProps {
  userId: string;
}

const RecentNotes: React.FC<RecentNotesProps> = ({ userId }) => {
  const { t } = useTranslation();

  const { data, isLoading, error } = useGetLatestNotesQuery(
    { userId },
    {
      // Adjust the API endpoint if necessary
    },
  );

  if (isLoading) {
    return (
      <Typography variant="body1">
        <CircularProgress size={24} /> {t('loading')}
      </Typography>
    );
  }

  if (error) {
    return (
      <Typography variant="body1" color="error">
        {t('errorFetchingNotes')}
      </Typography>
    );
  }

  return (
    <Box>
      <Box sx={{ display: 'flex', justifyContent: 'space-between', mb: 2 }}>
        <Typography variant="h6">{t('recentNotes')}</Typography>
        <Button
          component={RouterLink}
          to="/profile/notes"
          variant="outlined"
          size="small"
        >
          {t('viewMore')}
        </Button>
      </Box>
      {data && data.length > 0 ? (
        <List>
          {data.slice(0, 5).map((note: Note) => (
            <ListItem key={note.id} disablePadding>
              <ListItemText
                primary={note.name}
                secondary={new Date(note.createdAt).toLocaleDateString()}
              />
            </ListItem>
          ))}
        </List>
      ) : (
        <Typography>{t('noRecentNotes')}</Typography>
      )}
    </Box>
  );
};

export default RecentNotes;
