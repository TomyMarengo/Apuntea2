// src/pages/Directories/dialogs/DeleteDirectoryDialog.tsx

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

import { useDeleteDirectoryMutation } from '../../../store/slices/directoriesApiSlice';
import { Directory } from '../../../types';

interface DeleteDirectoryDialogProps {
  open: boolean;
  onClose: () => void;
  directory: Directory;
  shouldShowReason: boolean;
}

const DeleteDirectoryDialog: React.FC<DeleteDirectoryDialogProps> = ({
  open,
  onClose,
  directory,
  shouldShowReason,
}) => {
  const { t } = useTranslation('deleteDirectoryDialog');
  const [deleteDirectory] = useDeleteDirectoryMutation();

  const reasonSchema = z.object({
    reason: z
      .string()
      .max(255, { message: t('validation.reasonMaxLength') })
      .optional(),
  });

  const {
    register,
    handleSubmit,
    formState: { errors },
  } = useForm({
    resolver: zodResolver(reasonSchema),
    defaultValues: { reason: '' },
  });

  const onSubmit = async (data: { reason: string }) => {
    try {
      const result = await deleteDirectory({
        directoryId: directory.id,
        reason: shouldShowReason ? data.reason : undefined,
      }).unwrap();

      if (result.success) {
        toast.success(t('deleteSuccess'));
        onClose();
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
    } catch (err) {
      console.error('Failed to delete directory:', err);
      toast.error(t('deleteError'));
    }
  };

  return (
    <Dialog open={open} onClose={onClose} fullWidth maxWidth="xs">
      <DialogTitle>{t('deleteDirectoryTitle')}</DialogTitle>
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
          onClick={handleSubmit(onSubmit)}
          variant="contained"
          color="error"
        >
          {t('delete')}
        </Button>
      </DialogActions>
    </Dialog>
  );
};

export default DeleteDirectoryDialog;
