// src/pages/Reviews/dialogs/DeleteReviewDialog.tsx

import {
  Dialog,
  DialogTitle,
  DialogContent,
  DialogActions,
  Button,
  TextField,
  Typography,
} from '@mui/material';
import React, { useState } from 'react';
import { useTranslation } from 'react-i18next';
import { toast } from 'react-toastify';

import { useDeleteReviewMutation } from '../../../store/slices/reviewsApiSlice';
import { Review } from '../../../types';

interface DeleteReviewDialogProps {
  open: boolean;
  onClose: () => void;
  review: Review;
  shouldShowReason: boolean;
  onDeleteSuccess?: (review: Review) => void;
}

const DeleteReviewDialog: React.FC<DeleteReviewDialogProps> = ({
  open,
  onClose,
  review,
  shouldShowReason,
  onDeleteSuccess,
}) => {
  const { t } = useTranslation('deleteReviewDialog');
  const [deleteReview] = useDeleteReviewMutation();
  const [reason, setReason] = useState('');

  const handleConfirm = async () => {
    try {
      const result = await deleteReview({
        noteId: review.noteId,
        userId: review.userId,
        reason: shouldShowReason ? reason : undefined,
      }).unwrap();
      if (result) {
        toast.success(t('deleteSuccess'));
        onClose();
        if (onDeleteSuccess) onDeleteSuccess(review);
      } else {
        toast.error(t('deleteError'));
      }
    } catch (error) {
      console.error('Failed to delete review:', error);
      toast.error(t('deleteError'));
    }
  };

  return (
    <Dialog open={open} onClose={onClose} fullWidth maxWidth="xs">
      <DialogTitle>{t('deleteReviewTitle')}</DialogTitle>
      <DialogContent>
        <Typography>{t('deleteConfirmMessage')}</Typography>
        {shouldShowReason && (
          <TextField
            label={t('reason')}
            value={reason}
            onChange={(e) => setReason(e.target.value)}
            fullWidth
            margin="normal"
          />
        )}
      </DialogContent>
      <DialogActions>
        <Button onClick={onClose}>{t('cancel')}</Button>
        <Button onClick={handleConfirm} variant="contained" color="error">
          {t('delete')}
        </Button>
      </DialogActions>
    </Dialog>
  );
};

export default DeleteReviewDialog;
