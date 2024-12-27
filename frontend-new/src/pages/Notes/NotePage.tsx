// src/pages/Notes/NotePage.tsx

import { useParams } from 'react-router-dom';
import { useGetNoteQuery } from '../../store/slices/notesApiSlice';
import { useState } from 'react';
import {
  Typography,
  Button,
  Box,
  CircularProgress,
  Alert,
} from '@mui/material';
import { useTranslation } from 'react-i18next';

export default function NotePage() {
  const { id } = useParams<{ id: string }>();
  const { t } = useTranslation();
  const { data: note, isLoading, isError } = useGetNoteQuery({ noteId: id });

  const [copySuccess, setCopySuccess] = useState<string>('');

  const handleCopy = () => {
    if (note?.selfUrl) {
      navigator.clipboard.writeText(window.location.origin + note.selfUrl);
      setCopySuccess(t('notePage.copySuccess'));
      setTimeout(() => setCopySuccess(''), 2000);
    }
  };

  if (isLoading) {
    return (
      <Box display="flex" justifyContent="center" mt={5}>
        <CircularProgress />
      </Box>
    );
  }

  if (isError || !note) {
    return (
      <Box display="flex" justifyContent="center" mt={5}>
        <Alert severity="error">{t('notePage.errorFetchingNote')}</Alert>
      </Box>
    );
  }

  return (
    <Box p={3}>
      <Typography variant="h4" gutterBottom>
        {t('notePage.noteLink.title')}
      </Typography>
      <Typography variant="body1" gutterBottom>
        {t('notePage.noteLink.description')}
      </Typography>
      <Box display="flex" alignItems="center" mt={2}>
        <Typography variant="body2" sx={{ mr: 2 }}>
          {window.location.origin + note.selfUrl}
        </Typography>
        <Button variant="contained" onClick={handleCopy}>
          {t('notePage.copy')}
        </Button>
      </Box>
      {copySuccess && (
        <Alert severity="success" sx={{ mt: 2 }}>
          {copySuccess}
        </Alert>
      )}
    </Box>
  );
}
