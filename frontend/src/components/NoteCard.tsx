// src/components/NoteCard.tsx

import { Card, CardContent, Typography, CardActionArea } from '@mui/material';
import React from 'react';
import { useTranslation } from 'react-i18next';
import { Link as RouterLink } from 'react-router-dom';

import { Note } from '../types';

interface NoteCardProps {
  note: Note;
}

const NoteCard: React.FC<NoteCardProps> = ({ note }) => {
  const { t } = useTranslation('noteCard');

  return (
    <Card sx={{ mb: 2, boxShadow: 2 }}>
      <CardActionArea component={RouterLink} to={`/notes/${note.id}`}>
        <CardContent>
          <Typography variant="h6" gutterBottom>
            {note.name || t('hiddenNote')}
          </Typography>
          <Typography variant="body2" color="textSecondary">
            {new Date(note.createdAt).toLocaleDateString()}
          </Typography>
          <Typography variant="body2" color="textSecondary">
            {t(`category.${note.category?.toLowerCase()}`)} - {note.fileType}
          </Typography>
          <Typography variant="body2" color="textSecondary">
            {note.visible ? t('visible') : t('hidden')}
          </Typography>
          <Typography variant="body2" color="textSecondary">
            {t('averageScore')}: {note.avgScore.toFixed(2)}
          </Typography>
        </CardContent>
      </CardActionArea>
    </Card>
  );
};

export default NoteCard;
