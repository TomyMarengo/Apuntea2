// src/pages/Notes/dialogs/EditNoteDialog.tsx

import React, { useState, useEffect } from 'react';
import {
  Dialog,
  DialogTitle,
  DialogContent,
  DialogActions,
  Button,
  TextField,
  FormControl,
  InputLabel,
  Select,
  MenuItem,
  Switch,
  FormControlLabel,
} from '@mui/material';
import { useTranslation } from 'react-i18next';
import { Note, NoteCategory } from '../../../types';
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
  // Initialize translation with the 'editNoteDialog' namespace
  const { t } = useTranslation('editNoteDialog');
  const [updateNote] = useUpdateNoteMutation();

  const [name, setName] = useState(note.name);
  const [visible, setVisible] = useState(note.visible);
  const [category, setCategory] = useState(note.category);

  useEffect(() => {
    if (open) {
      setName(note.name);
      setVisible(note.visible);
      setCategory(note.category);
    }
  }, [open, note]);

  // Handle save action
  const handleSave = async () => {
    try {
      const result = await updateNote({
        noteId: note.id,
        name,
        visible,
        category,
      }).unwrap();
      if (result) {
        toast.success(t('editSuccess'));
        onClose();
      }
    } catch (err) {
      toast.error(t('editError'));
    }
  };

  return (
    <Dialog open={open} onClose={onClose} fullWidth maxWidth="xs">
      <DialogTitle>{t('editNoteTitle')}</DialogTitle>
      <DialogContent>
        {/* Name Field */}
        <TextField
          label={t('name')}
          value={name}
          onChange={(e) => setName(e.target.value)}
          fullWidth
          margin="normal"
        />

        {/* Visible Switch */}
        <FormControlLabel
          control={
            <Switch
              checked={visible}
              onChange={(e) => setVisible(e.target.checked)}
              color="primary"
            />
          }
          label={t('visible')}
        />

        {/* Category Select */}
        <FormControl fullWidth margin="normal">
          <InputLabel>{t('category')}</InputLabel>
          <Select
            value={category}
            label={t('category')}
            onChange={(e) => setCategory(e.target.value as NoteCategory)}
          >
            {Object.values(NoteCategory).map((cat) => (
              <MenuItem key={cat} value={cat}>
                {t(`categories.${cat}`)}
              </MenuItem>
            ))}
          </Select>
        </FormControl>
      </DialogContent>
      <DialogActions>
        <Button onClick={onClose}>{t('cancel')}</Button>
        <Button onClick={handleSave} variant="contained">
          {t('save')}
        </Button>
      </DialogActions>
    </Dialog>
  );
};

export default EditNoteDialog;
