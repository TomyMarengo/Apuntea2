// src/components/ReviewCard.tsx

import DeleteIcon from '@mui/icons-material/Delete';
import {
  Box,
  Typography,
  Rating,
  Avatar,
  Link,
  IconButton,
} from '@mui/material';
import dayjs from 'dayjs';
import 'dayjs/locale/en';
import 'dayjs/locale/es';
import localizedFormat from 'dayjs/plugin/localizedFormat';
import React, { useState } from 'react';
import { useTranslation } from 'react-i18next';
import { useSelector } from 'react-redux';
import { Link as RouterLink } from 'react-router-dom';

import DeleteReviewDialog from '../pages/Reviews/dialogs/DeleteReviewDialog';
import {
  selectCurrentToken,
  selectCurrentUser,
} from '../store/slices/authSlice';
import { useGetNoteQuery } from '../store/slices/notesApiSlice';
import { useGetUserQuery } from '../store/slices/usersApiSlice';
import { Review } from '../types';

dayjs.extend(localizedFormat);

interface ReviewCardProps {
  review: Review;
  noteId?: string;
  shouldDelete?: boolean;
  onDeleteSuccess?: (review: Review) => void;
}

const ReviewCard: React.FC<ReviewCardProps> = ({
  review,
  noteId,
  shouldDelete = false,
  onDeleteSuccess,
}) => {
  const { t, i18n } = useTranslation('reviewCard');
  const user = useSelector(selectCurrentUser);

  const { data: userData } = useGetUserQuery(
    { url: review.userUrl },
    { skip: !review.userUrl },
  );

  const { data: noteData } = useGetNoteQuery(
    { url: review.noteUrl },
    { skip: !review.noteUrl },
  );

  const [openDelete, setOpenDeleteModal] = useState(false);

  const handleCloseDelete = () => {
    setOpenDeleteModal(false);
  };

  const handleDeleteClick = () => {
    setOpenDeleteModal(true);
  };

  dayjs.locale(i18n.language);

  const formattedDate = dayjs(review.createdAt).format(t('dateFormat'));

  const token = useSelector(selectCurrentToken);

  const isAdmin = token?.payload?.authorities?.includes('ROLE_ADMIN') ?? false;

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
      <Box
        sx={{
          display: 'flex',
          justifyContent: 'space-between',
          alignItems: 'center',
        }}
      >
        <Box
          sx={{
            display: 'flex',
            alignItems: 'center',
            gap: 1,
            flexWrap: 'wrap',
          }}
        >
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
            <Typography variant="subtitle2">{t('you')}</Typography>
          )}
          <Rating value={review.score} readOnly size="small" />
        </Box>

        {isAdmin && shouldDelete && (
          <IconButton onClick={handleDeleteClick}>
            <DeleteIcon />
          </IconButton>
        )}
      </Box>

      <Typography
        variant="body2"
        sx={{
          mb: 1,
          mt: 2,
          overflowWrap: 'break-word',
        }}
      >
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
            {t('hiddenNote')}
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
      <DeleteReviewDialog
        open={openDelete}
        onClose={handleCloseDelete}
        review={review}
        shouldShowReason={true}
        onDeleteSuccess={onDeleteSuccess}
      />
    </Box>
  );
};

export default ReviewCard;
