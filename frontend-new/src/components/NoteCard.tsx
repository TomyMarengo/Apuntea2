// src/components/NoteCard.tsx

import React from 'react';
import { Card, CardContent, Typography, CardActionArea } from '@mui/material';
import { Note } from '../types';
import { Link as RouterLink } from 'react-router-dom';

interface NoteCardProps {
  note: Note;
}

const NoteCard: React.FC<NoteCardProps> = ({ note }) => {
  return (
    <Card sx={{ mb: 2, boxShadow: 2 }}>
      <CardActionArea component={RouterLink} to={`/notes/${note.id}`}>
        <CardContent>
          <Typography variant="h6" gutterBottom>
            {note.name}
          </Typography>
          <Typography variant="body2" color="textSecondary">
            {new Date(note.createdAt).toLocaleDateString()}
          </Typography>
          <Typography variant="body2" color="textSecondary">
            {note.category} - {note.fileType}
          </Typography>
          <Typography variant="body2" color="textSecondary">
            {note.visible ? 'Visible' : 'Hidden'}
          </Typography>
          <Typography variant="body2" color="textSecondary">
            Average Score: {note.avgScore.toFixed(2)}
          </Typography>
        </CardContent>
      </CardActionArea>
    </Card>
  );
};

export default NoteCard;
