// src/components/ReviewCard.tsx

import React from 'react';
import { Card, CardContent, Typography, CardActionArea } from '@mui/material';
import { Review } from '../types';
import { Link as RouterLink } from 'react-router-dom';

interface ReviewCardProps {
  review: Review;
}

const ReviewCard: React.FC<ReviewCardProps> = ({ review }) => {
  return (
    <Card sx={{ mb: 2, boxShadow: 2 }}>
      <CardActionArea component={RouterLink} to={`/reviews/${review.id}`}>
        <CardContent>
          <Typography variant="h6" gutterBottom>
            Review for Note ID: {review.noteId}
          </Typography>
          <Typography variant="body2" color="textSecondary">
            Score: {review.score}
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
