// src/components/ReviewCard.tsx

import React from 'react';
import { Box, Typography, Rating, Avatar, Link } from '@mui/material';
import { useSelector } from 'react-redux';
import { selectCurrentUser } from '../store/slices/authSlice';
import { Review } from '../types';
import { Link as RouterLink } from 'react-router-dom';
import { useGetUserQuery } from '../store/slices/usersApiSlice';
import { useGetNoteQuery } from '../store/slices/notesApiSlice';
import { useTranslation } from 'react-i18next';
import dayjs from 'dayjs';
import localizedFormat from 'dayjs/plugin/localizedFormat';

dayjs.extend(localizedFormat);

interface ReviewCardProps {
  review: Review;
  noteId?: string;
}

const ReviewCard: React.FC<ReviewCardProps> = ({ review, noteId }) => {
  const { t } = useTranslation();
  const user = useSelector(selectCurrentUser);

  // Fetch user data if needed
  const { data: userData } = useGetUserQuery(
    { url: review.userUrl },
    { skip: !review.userUrl },
  );

  // Fetch note data if needed
  const { data: noteData } = useGetNoteQuery(
    { url: review.noteUrl },
    { skip: !review.noteUrl },
  );

  // Format the createdAt date
  const formattedDate = dayjs(review.createdAt).format('LL'); // Example: December 29, 2024

  return (
    <Box
      sx={{
        mb: 2,
        p: 2,
        border: 1,
        borderColor: 'divider',
        borderRadius: 1,
        backgroundColor: 'background.paper',
      }}
    >
      <Box sx={{ display: 'flex', alignItems: 'center', gap: 1, mb: 1 }}>
        <Avatar src={userData?.profilePictureUrl || ''} />
        {userData?.id !== user?.id ? (
          <Link
            component={RouterLink}
            to={`/users/${review.userId}`}
            underline="hover"
          >
            <Typography variant="subtitle2">
              {userData?.username || `User ${review.userId}`}
            </Typography>
          </Link>
        ) : (
          <Typography variant="subtitle2">{t('reviewCard.you')}</Typography>
        )}
        <Rating value={review.score} readOnly size="small" />
      </Box>
      <Typography variant="body2" sx={{ mb: 1 }}>
        {review.content}
      </Typography>
      <Box
        sx={{
          display: 'flex',
          justifyContent: 'space-between',
          alignItems: 'center',
        }}
      >
        {noteData && noteId !== noteData.id && (
          <Link
            component={RouterLink}
            to={`/notes/${review.noteId}`}
            underline="hover"
          >
            <Typography variant="caption" color="primary">
              {noteData.name}
            </Typography>
          </Link>
        )}
        {!noteData && (
          <Typography variant="caption" color="text.secondary">
            {t('reviewCard.hiddenNote')}
          </Typography>
        )}
        <Typography
          variant="caption"
          color="text.secondary"
          sx={{ ml: 'auto' }}
        >
          {formattedDate}
        </Typography>
      </Box>
    </Box>
  );
};

export default ReviewCard;
