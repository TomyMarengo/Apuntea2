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
import { useNavigate } from 'react-router-dom';
import { toast } from 'react-toastify';
import { z } from 'zod';

import { useDeleteNoteMutation } from '../../../store/slices/notesApiSlice';
import { Note } from '../../../types';

// Define Zod schema for reason validation
const reasonSchema = z.object({
  reason: z.string().max(255, { message: 'reasonMaxLength' }).optional(),
});

interface DeleteNoteDialogProps {
  open: boolean;
  onClose: () => void;
  note: Note;
  shouldShowReason: boolean;
  navigateBack?: boolean;
}

const DeleteNoteDialog: React.FC<DeleteNoteDialogProps> = ({
  open,
  onClose,
  note,
  shouldShowReason,
  navigateBack = false,
}) => {
  const { t } = useTranslation('deleteNoteDialog');
  const [deleteNote] = useDeleteNoteMutation();

  const {
    register,
    handleSubmit,
    formState: { errors },
  } = useForm({
    resolver: zodResolver(reasonSchema),
    defaultValues: { reason: '' },
  });
  const navigate = useNavigate();

  // Handle confirm delete action
  const onSubmit = async (data: { reason: string }) => {
    try {
      const result = await deleteNote({
        noteId: note.id,
        reason: shouldShowReason ? data.reason : undefined,
      }).unwrap();
      if (result.success) {
        toast.success(t('deleteSuccess'));
        onClose();
        if (navigateBack) {
          navigate(-1);
        }
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
      console.error('Failed to delete note:', err);
      toast.error(t('deleteError'));
    }
  };

  return (
    <Dialog open={open} onClose={onClose} fullWidth maxWidth="xs">
      <DialogTitle>{t('deleteNoteTitle')}</DialogTitle>
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

export default DeleteNoteDialog;
