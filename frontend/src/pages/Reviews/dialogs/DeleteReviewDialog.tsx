// src/pages/Reviews/dialogs/DeleteReviewDialog.tsx

import { zodResolver } from '@hookform/resolvers/zod';
import {
  Dialog,
  DialogTitle,
  DialogContent,
  DialogActions,
  Button,
  TextField,
  Typography,
} from '@mui/material';
import React from 'react';
import { useForm } from 'react-hook-form';
import { useTranslation } from 'react-i18next';
import { toast } from 'react-toastify';
import { z } from 'zod';

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

  const reasonSchema = z.object({
    reason: z
      .string()
      .max(255, { message: t('validation.reasonMaxLength') })
      .optional(),
  });

  // React Hook Form with Zod Resolver
  const {
    register,
    handleSubmit,
    formState: { errors },
  } = useForm({
    resolver: zodResolver(reasonSchema),
    defaultValues: { reason: '' },
  });

  const handleConfirm = async (data: { reason: string }) => {
    try {
      const result = await deleteReview({
        noteId: review.noteId,
        userId: review.userId,
        reason: shouldShowReason ? data.reason : undefined,
      }).unwrap();
      if (result.success) {
        toast.success(t('deleteSuccess'));
        onClose();
        if (onDeleteSuccess) onDeleteSuccess(review);
      } else {
        toast.error(
          t('deleteError', {
            errorMessage:
              result.messages && result.messages.length > 0
                ? `: ${result.messages[0]}`
                : '',
          }),
        );
      }
    } catch (error) {
      console.error('Failed to delete review:', error);
      toast.error(t('deleteError', { errorMessage: '' }));
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
            {...register('reason')}
            error={!!errors.reason}
            helperText={errors.reason ? t(errors.reason.message!) : ''}
            fullWidth
            margin="normal"
          />
        )}
      </DialogContent>
      <DialogActions>
        <Button onClick={onClose}>{t('cancel')}</Button>
        <Button
          onClick={handleSubmit(handleConfirm)}
          variant="contained"
          color="error"
        >
          {t('delete')}
        </Button>
      </DialogActions>
    </Dialog>
  );
};

export default DeleteReviewDialog;
