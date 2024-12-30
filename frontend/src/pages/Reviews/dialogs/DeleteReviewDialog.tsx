import React, { useState } from 'react';
import {
  Dialog,
  DialogTitle,
  DialogContent,
  DialogActions,
  Button,
  TextField,
  Typography,
} from '@mui/material';
import { useTranslation } from 'react-i18next';
import { useDeleteReviewMutation } from '../../../store/slices/reviewsApiSlice';
import { toast } from 'react-toastify';

import { Review } from '../../../types';

interface DeleteReviewDialogProps {
  open: boolean;
  onClose: () => void;
  review: Review;
  shouldShowReason: boolean;
}

const DeleteReviewDialog: React.FC<DeleteReviewDialogProps> = ({
  open,
  onClose,
  review,
  shouldShowReason,
}) => {
  const { t } = useTranslation();
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
        toast.success(t('reviewCard.deleteSuccess'));
        onClose();
      } else {
        toast.error(t('reviewCard.deleteError'));
      }
    } catch (err) {
      toast.error(t('reviewCard.deleteError'));
    }
  };

  return (
    <Dialog open={open} onClose={onClose} fullWidth maxWidth="xs">
      <DialogTitle>{t('reviewCard.deleteReviewTitle')}</DialogTitle>
      <DialogContent>
        <Typography>{t('reviewCard.deleteConfirmMessage')}</Typography>
        {shouldShowReason && (
          <TextField
            label={t('reviewCard.reason')}
            value={reason}
            onChange={(e) => setReason(e.target.value)}
            fullWidth
            margin="normal"
          />
        )}
      </DialogContent>
      <DialogActions>
        <Button onClick={onClose}>{t('reviewCard.cancel')}</Button>
        <Button onClick={handleConfirm} variant="contained" color="error">
          {t('reviewCard.delete')}
        </Button>
      </DialogActions>
    </Dialog>
  );
};

export default DeleteReviewDialog;
