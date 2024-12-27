import React from 'react';
import { Card, CardContent, Typography, CardActionArea } from '@mui/material';
import { Note } from '../types';
import { Link as RouterLink } from 'react-router-dom';
import { useTranslation } from 'react-i18next';

interface NoteCardProps {
  note: Note;
}

const NoteCard: React.FC<NoteCardProps> = ({ note }) => {
  const { t } = useTranslation();

  return (
    <Card sx={{ mb: 2, boxShadow: 2 }}>
      <CardActionArea component={RouterLink} to={`/notes/${note.id}`}>
        <CardContent>
          <Typography variant="h6" gutterBottom>
            {note.name || t('noteCard.hiddenNote')}
          </Typography>
          <Typography variant="body2" color="textSecondary">
            {new Date(note.createdAt).toLocaleDateString()}
          </Typography>
          <Typography variant="body2" color="textSecondary">
            {t(`noteCard.category.${note.category}`)} - {note.fileType}
          </Typography>
          <Typography variant="body2" color="textSecondary">
            {note.visible ? t('noteCard.visible') : t('noteCard.hidden')}
          </Typography>
          <Typography variant="body2" color="textSecondary">
            {t('noteCard.averageScore')}: {note.avgScore.toFixed(2)}
          </Typography>
        </CardContent>
      </CardActionArea>
    </Card>
  );
};

export default NoteCard;
