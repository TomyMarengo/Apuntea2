// src/components/ReviewCard.tsx

import React from 'react';
import { Card, CardContent, Typography, CardActionArea } from '@mui/material';
import { Review } from '../types';
import { Link as RouterLink } from 'react-router-dom';
import { useGetNoteQuery } from '../store/slices/notesApiSlice';
import { useTranslation } from 'react-i18next';

interface ReviewCardProps {
  review: Review;
}

const ReviewCard: React.FC<ReviewCardProps> = ({ review }) => {
  const { t } = useTranslation();
  const { data: note } = useGetNoteQuery({ url: review.noteUrl });

  return (
    <Card sx={{ mb: 2, boxShadow: 2 }}>
      <CardActionArea
        component={RouterLink}
        to={`/reviews/${review.userId + review.noteId}`}
      >
        <CardContent>
          <Typography variant="h6" gutterBottom>
            {note?.name || t('reviewCard.hiddenNote')}
          </Typography>
          <Typography variant="body2" color="textSecondary">
            {t('reviewCard.score')}: {review.score}
          </Typography>
          <Typography variant="body2" color="textSecondary">
            {review.content}
          </Typography>
        </CardContent>
      </CardActionArea>
    </Card>
  );
};

export default ReviewCard;
