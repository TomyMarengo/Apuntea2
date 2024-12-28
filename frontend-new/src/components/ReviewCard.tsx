// src/components/ReviewCard.tsx

import React from 'react';
import {
  Box,
  Typography,
  Rating,
  CardActionArea,
  CircularProgress,
} from '@mui/material';
import { Review } from '../types';
import { Link as RouterLink } from 'react-router-dom';
import { useGetNoteQuery } from '../store/slices/notesApiSlice';
import { useGetUserQuery } from '../store/slices/usersApiSlice';
import { useTranslation } from 'react-i18next';

interface ReviewCardProps {
  review: Review;
}

const ReviewCard: React.FC<ReviewCardProps> = ({ review }) => {
  const { t } = useTranslation();

  const {
    data: note,
    isLoading: noteLoading,
    error: noteError,
  } = useGetNoteQuery({ url: review.noteUrl });

  const {
    data: user,
    isLoading: userLoading,
    error: userError,
  } = useGetUserQuery({ url: review.userUrl });

  const reviewUrl = `/reviews/${review.noteId}_${review.userId}`;

  return (
    <Box sx={{ mb: 2, boxShadow: 2, borderRadius: 1 }}>
      <CardActionArea component={RouterLink} to={reviewUrl}>
        <Box
          sx={{
            p: 2,
            border: 1,
            borderColor: 'divider',
            borderRadius: 1,
            '&:hover': {
              backgroundColor: 'action.hover',
            },
          }}
        >
          {/* Encabezado: Nombre del usuario y puntuación */}
          <Box
            sx={{
              display: 'flex',
              justifyContent: 'space-between',
              alignItems: 'center',
            }}
          >
            <Typography variant="subtitle2">
              {userLoading ? (
                <CircularProgress size={16} />
              ) : userError ? (
                t('reviewCard.errorLoadingUser')
              ) : (
                user?.username || user?.email || t('reviewCard.unknownUser')
              )}
            </Typography>
            <Rating value={review.score} readOnly size="small" />
          </Box>

          {/* Contenido de la reseña */}
          <Typography variant="body2" sx={{ mt: 1 }}>
            {review.content}
          </Typography>

          {/* Nombre de la nota */}
          <Typography
            variant="caption"
            color="textSecondary"
            sx={{ mt: 1, display: 'block' }}
          >
            {noteLoading ? (
              <CircularProgress size={14} />
            ) : noteError ? (
              t('reviewCard.errorLoadingNote')
            ) : (
              note?.name || t('reviewCard.hiddenNote')
            )}
          </Typography>
        </Box>
      </CardActionArea>
    </Box>
  );
};

export default ReviewCard;
