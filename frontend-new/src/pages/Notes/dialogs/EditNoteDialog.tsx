// src/pages/NotePage/dialogs/EditNoteDialog.tsx

import React, { useState } from 'react';
import {
  Dialog,
  DialogTitle,
  DialogContent,
  DialogActions,
  Button,
  TextField,
} from '@mui/material';
import { useTranslation } from 'react-i18next';
import { Note } from '../../../types';
import { useUpdateNoteMutation } from '../../../store/slices/notesApiSlice';
import { toast } from 'react-toastify';

interface EditNoteDialogProps {
  open: boolean;
  onClose: () => void;
  note: Note;
}

const EditNoteDialog: React.FC<EditNoteDialogProps> = ({
  open,
  onClose,
  note,
}) => {
  const { t } = useTranslation();
  const [updateNote] = useUpdateNoteMutation();

  const [name, setName] = useState(note.name);

  const handleSave = async () => {
    try {
      const result = await updateNote({
        noteId: note.id,
        name,
      }).unwrap();
      if (result) {
        toast.success(t('notePage.editSuccess'));
        onClose();
      }
    } catch (err) {
      toast.error(t('notePage.editError'));
    }
  };

  return (
    <Dialog open={open} onClose={onClose} fullWidth maxWidth="xs">
      <DialogTitle>{t('notePage.editNoteTitle')}</DialogTitle>
      <DialogContent>
        <TextField
          label={t('notePage.name')}
          value={name}
          onChange={(e) => setName(e.target.value)}
          fullWidth
          margin="normal"
        />
      </DialogContent>
      <DialogActions>
        <Button onClick={onClose}>{t('notePage.cancel')}</Button>
        <Button onClick={handleSave} variant="contained">
          {t('notePage.save')}
        </Button>
      </DialogActions>
    </Dialog>
  );
};

export default EditNoteDialog;
