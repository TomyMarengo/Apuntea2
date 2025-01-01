// src/pages/Notes/dialogs/DeleteNoteDialog.tsx

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
import { Note } from '../../../types';
import { useDeleteNoteMutation } from '../../../store/slices/notesApiSlice';
import { toast } from 'react-toastify';
import { useNavigate } from 'react-router-dom';

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
  const [reason, setReason] = useState('');
  const navigate = useNavigate();

  // Handle confirm delete action
  const handleConfirm = async () => {
    try {
      const result = await deleteNote({
        noteId: note.id,
        reason: shouldShowReason ? reason : undefined,
      }).unwrap();
      if (result) {
        toast.success(t('deleteSuccess'));
        onClose();
        if (navigateBack) navigate('/', { replace: true });
      } else {
        toast.error(t('deleteError'));
      }
    } catch (err) {
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

export default DeleteNoteDialog;
