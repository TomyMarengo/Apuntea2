// src/pages/Notes/dialogs/EditNoteDialog.tsx

import { zodResolver } from '@hookform/resolvers/zod';
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
import React, { useEffect } from 'react';
import { useForm, Controller } from 'react-hook-form';
import { useTranslation } from 'react-i18next';
import { toast } from 'react-toastify';
import { z } from 'zod';

import { useUpdateNoteMutation } from '../../../store/slices/notesApiSlice';
import { Note, NoteCategory } from '../../../types';

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
  const { t } = useTranslation('editNoteDialog');
  const [updateNote] = useUpdateNoteMutation();

  const noteSchema = z.object({
    name: z
      .string()
      .nonempty({ message: t('validation.nameNotEmpty') })
      .min(2, { message: t('validation.nameMinLength') })
      .max(50, { message: t('validation.nameMaxLength') })
      .regex(/^(?!([ ,\-_.]+)$)[a-zA-Z0-9áéíóúÁÉÍÓÚñÑüÜ .,\\-_]+$/, {
        message: t('validation.nameInvalid'),
      }),
    category: z.string().regex(/^(THEORY|PRACTICE|EXAM|OTHER)$/, {
      message: t('validation.categoryInvalid'),
    }),
    visible: z.boolean().default(true),
  });

  type NoteFormData = z.infer<typeof noteSchema>;

  const {
    control,
    handleSubmit,
    reset,
    formState: { errors },
  } = useForm<NoteFormData>({
    resolver: zodResolver(noteSchema),
    defaultValues: {
      name: note.name,
      visible: note.visible,
      category: note.category,
    },
  });

  // Update form values when the dialog opens
  useEffect(() => {
    if (open) {
      reset({
        name: note.name,
        visible: note.visible,
        category: note.category,
      });
    }
  }, [open, note, reset]);

  // Handle save action
  const onSubmit = async (data: NoteFormData) => {
    try {
      const result = await updateNote({
        // noteId: note.id,
        url: note.selfUrl,
        name: data.name,
        visible: data.visible,
        category: data.category as NoteCategory,
      }).unwrap();

      if (result.success) {
        toast.success(t('editSuccess'));
        onClose();
      } else {
        toast.error(
          t('editFailed', {
            errorMessage:
              result.messages && result.messages.length > 0
                ? `: ${result.messages[0]}`
                : '',
          }),
        );
      }
    } catch (error) {
      console.error('Failed to edit note:', error);
      toast.error(t('editFailed', { errorMessage: '' }));
    }
  };

  return (
    <Dialog open={open} onClose={onClose} fullWidth maxWidth="xs">
      <DialogTitle>{t('editNoteTitle')}</DialogTitle>
      <DialogContent>
        {/* Name Field */}
        <Controller
          name="name"
          control={control}
          render={({ field }) => (
            <TextField
              {...field}
              label={t('name')}
              fullWidth
              margin="normal"
              error={!!errors.name}
              helperText={errors.name ? t(errors.name.message as string) : ''}
            />
          )}
        />

        {/* Category Select */}
        <FormControl fullWidth margin="normal">
          <InputLabel>{t('category')}</InputLabel>
          <Controller
            name="category"
            control={control}
            render={({ field }) => (
              <Select
                {...field}
                label={t('category')}
                error={!!errors.category}
                onChange={(e) => field.onChange(e.target.value)}
              >
                {Object.values(NoteCategory).map((cat) => (
                  <MenuItem key={cat} value={cat}>
                    {t(`categories.${cat.toLowerCase()}`)}
                  </MenuItem>
                ))}
              </Select>
            )}
          />
        </FormControl>

        {/* Visibility Switch */}
        <Controller
          name="visible"
          control={control}
          render={({ field }) => (
            <FormControlLabel
              control={
                <Switch
                  {...field}
                  checked={field.value}
                  onChange={(e) => field.onChange(e.target.checked)}
                />
              }
              label={field.value ? t('visible') : t('hidden')}
            />
          )}
        />
      </DialogContent>
      <DialogActions>
        <Button onClick={onClose}>{t('cancel')}</Button>
        <Button onClick={handleSubmit(onSubmit)} variant="contained">
          {t('save')}
        </Button>
      </DialogActions>
    </Dialog>
  );
};

export default EditNoteDialog;
