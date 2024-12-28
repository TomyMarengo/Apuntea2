// src/pages/NotePage/dialogs/DeleteNoteDialog.tsx

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

interface DeleteNoteDialogProps {
  open: boolean;
  onClose: () => void;
  note: Note;
}

const DeleteNoteDialog: React.FC<DeleteNoteDialogProps> = ({
  open,
  onClose,
  note,
}) => {
  const { t } = useTranslation();
  const [deleteNote] = useDeleteNoteMutation();
  const [reason, setReason] = useState('');

  const handleConfirm = async () => {
    try {
      const result = await deleteNote({
        noteId: note.id,
        reason,
      }).unwrap();
      if (result) {
        toast.success(t('notePage.deleteSuccess'));
        onClose();
        // Possibly navigate away, e.g. to /notes
      }
    } catch (err) {
      toast.error(t('notePage.deleteError'));
    }
  };

  return (
    <Dialog open={open} onClose={onClose} fullWidth maxWidth="xs">
      <DialogTitle>{t('notePage.deleteNoteTitle')}</DialogTitle>
      <DialogContent>
        <Typography>{t('notePage.deleteConfirmMessage')}</Typography>
        <TextField
          label={t('notePage.reason')}
          value={reason}
          onChange={(e) => setReason(e.target.value)}
          fullWidth
          margin="normal"
        />
      </DialogContent>
      <DialogActions>
        <Button onClick={onClose}>{t('notePage.cancel')}</Button>
        <Button onClick={handleConfirm} variant="contained" color="error">
          {t('notePage.delete')}
        </Button>
      </DialogActions>
    </Dialog>
  );
};

export default DeleteNoteDialog;
