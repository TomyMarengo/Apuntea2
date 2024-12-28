// src/pages/NotePage/dialogs/EditNoteDialog.tsx

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
  const { t } = useTranslation();
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

  const handleSave = async () => {
    try {
      const result = await updateNote({
        noteId: note.id,
        name,
        visible,
        category,
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
        {/* Name */}
        <TextField
          label={t('notePage.name')}
          value={name}
          onChange={(e) => setName(e.target.value)}
          fullWidth
          margin="normal"
        />

        {/* Visible */}
        <FormControlLabel
          control={
            <Switch
              checked={visible}
              onChange={(e) => setVisible(e.target.checked)}
              color="primary"
            />
          }
          label={t('notePage.visible')}
        />

        {/* Category */}
        <FormControl fullWidth margin="normal">
          <InputLabel>{t('createNoteFab.category')}</InputLabel>
          <Select
            value={category}
            label={t('createNoteFab.category')}
            onChange={(e) => setCategory(e.target.value as NoteCategory)}
          >
            {Object.values(NoteCategory).map((cat) => (
              <MenuItem key={cat} value={cat}>
                {cat}
              </MenuItem>
            ))}
          </Select>
        </FormControl>
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
