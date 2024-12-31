// src/components/CreateNoteFab.tsx

import React, { useState, useRef } from 'react';
import {
  Box,
  Fab,
  Tooltip,
  Typography,
  TextField,
  Button,
  Select,
  MenuItem,
  FormControl,
  InputLabel,
  Switch,
  FormControlLabel,
  Paper,
  ClickAwayListener,
  CircularProgress,
} from '@mui/material';
import NoteAddIcon from '@mui/icons-material/NoteAdd';
import { useTranslation } from 'react-i18next';
import { toast } from 'react-toastify';
import { useCreateNoteMutation } from '../store/slices/notesApiSlice';
import { z } from 'zod';
import { useForm, Controller } from 'react-hook-form';
import { zodResolver } from '@hookform/resolvers/zod';
import { NoteCategory } from '../types';

// Define the Zod schema for form validation
const noteSchema = z.object({
  noteName: z
    .string()
    .min(1, 'missingFields')
    .regex(
      /^(?!([ ,\-_.]+)$)[a-zA-Z0-9áéíóúÁÉÍÓÚñÑüÜ .,\\-_]+$/,
      'invalidName',
    ),
  category: z.enum(['THEORY', 'PRACTICE', 'EXAM', 'OTHER'], {
    errorMap: () => ({ message: 'invalidCategory' }),
  }),
  visible: z.boolean(),
  file: z
    .any()
    .refine((file) => file instanceof File, 'invalidFile')
    .optional(),
});

type NoteFormData = z.infer<typeof noteSchema>;

interface CreateNoteFabProps {
  parentId: string;
}

const CreateNoteFab: React.FC<CreateNoteFabProps> = ({ parentId }) => {
  const { t } = useTranslation('createNoteFab');

  const [createNote, { isLoading }] = useCreateNoteMutation();

  // Tracks if the form is expanded
  const [expanded, setExpanded] = useState(false);

  // Fields for creating a note
  const [noteName, setNoteName] = useState('');
  const [visible, setVisible] = useState(true);
  const [category, setCategory] = useState<NoteCategory>(NoteCategory.THEORY);
  const [file, setFile] = useState<File | null>(null);

  // Reference to the wrapper for ClickAwayListener
  const containerRef = useRef<HTMLDivElement | null>(null);

  // Initialize react-hook-form with Zod resolver
  const {
    control,
    handleSubmit,
    reset,
    formState: { errors },
  } = useForm<NoteFormData>({
    resolver: zodResolver(noteSchema),
    defaultValues: {
      noteName: '',
      category: NoteCategory.THEORY,
      visible: true,
      file: undefined,
    },
  });

  // Toggles expansion upon FAB click
  const handleFabClick = () => {
    setExpanded((prev) => !prev);
  };

  // If user clicks away outside the box, close if expanded
  const handleClickAway = (_event: MouseEvent | TouchEvent) => {
    if (expanded) {
      setExpanded(false);
    }
  };

  // Handle form submission
  const onSubmit = async (data: NoteFormData) => {
    if (!data.file) {
      toast.error(t('missingFields'));
      return;
    }

    try {
      const result = await createNote({
        name: noteName,
        parentId,
        visible,
        file: file!,
        category,
      }).unwrap();

      if (result) {
        toast.success(t('noteCreated'));
        reset();
        setExpanded(false);
      } else {
        toast.error(t('noteCreationFailed'));
      }
    } catch (error: any) {
      toast.error(error?.data?.[0]?.message || t('noteCreationFailed'));
      console.error('Failed to create note:', error);
    }
  };

  return (
    <ClickAwayListener onClickAway={handleClickAway}>
      <Box ref={containerRef}>
        {/* Floating Action Button (always visible) */}
        {!expanded && (
          <Tooltip title={t('createNewNote')} placement="left">
            <Fab
              color="primary"
              onClick={handleFabClick}
              sx={{
                cursor: 'pointer',
              }}
            >
              <NoteAddIcon
                sx={{
                  color: 'background.paper',
                }}
              />
            </Fab>
          </Tooltip>
        )}

        {/* Expanded Form */}
        {expanded && (
          <Paper
            sx={{
              p: 2,
              width: 300,
              borderRadius: 2,
              boxShadow: 4,
              display: 'flex',
              flexDirection: 'column',
              gap: 2,
            }}
            elevation={8}
          >
            <Typography variant="subtitle1" sx={{ mb: 1 }}>
              {t('createNewNote')}
            </Typography>

            {/* Note Name */}
            <Controller
              name="noteName"
              control={control}
              render={({ field }) => (
                <TextField
                  {...field}
                  size="small"
                  label={t('noteName')}
                  error={!!errors.noteName}
                  helperText={
                    errors.noteName ? t(errors.noteName.message as string) : ''
                  }
                  fullWidth
                />
              )}
            />

            {/* Category */}
            <FormControl fullWidth size="small">
              <InputLabel>{t('category')}</InputLabel>
              <Controller
                name="category"
                control={control}
                render={({ field }) => (
                  <Select
                    {...field}
                    label={t('category')}
                    onChange={(e) => field.onChange(e.target.value)}
                    MenuProps={{
                      disablePortal: true,
                    }}
                  >
                    <MenuItem value={NoteCategory.THEORY}>
                      {t('theory')}
                    </MenuItem>
                    <MenuItem value={NoteCategory.PRACTICE}>
                      {t('practice')}
                    </MenuItem>
                    <MenuItem value={NoteCategory.EXAM}>{t('exam')}</MenuItem>
                    <MenuItem value={NoteCategory.OTHER}>{t('other')}</MenuItem>
                  </Select>
                )}
              />
            </FormControl>

            {/* Visibility Switch & File Upload */}
            <Box
              sx={{
                display: 'flex',
                flexDirection: 'column',
                gap: 1,
              }}
            >
              <FormControlLabel
                control={
                  <Controller
                    name="visible"
                    control={control}
                    render={({ field }) => (
                      <Switch
                        {...field}
                        checked={field.value}
                        onChange={(e) => field.onChange(e.target.checked)}
                      />
                    )}
                  />
                }
                label={visible ? t('visible') : t('hidden')}
              />

              <Button
                variant="outlined"
                component="label"
                size="small"
                sx={{
                  textTransform: 'none',
                  flexShrink: 0,
                  maxWidth: '130px',
                  overflow: 'hidden',
                  textOverflow: 'ellipsis',
                  whiteSpace: 'nowrap',
                  display: 'block',
                  textAlign: 'left',
                }}
              >
                {file ? file.name : t('chooseFile')}
                <input
                  type="file"
                  hidden
                  onChange={(e) => {
                    if (e.target.files && e.target.files[0]) {
                      setFile(e.target.files[0]);
                    }
                  }}
                />
              </Button>
              {errors.file && (
                <Typography variant="caption" color="error">
                  {t(errors.file.message as string)}
                </Typography>
              )}
            </Box>

            {/* Submit Button */}
            <Box
              sx={{
                display: 'flex',
                justifyContent: 'flex-end',
                mt: 1,
              }}
            >
              {isLoading ? (
                <CircularProgress size={30} />
              ) : (
                <Button
                  variant="contained"
                  size="small"
                  onClick={handleSubmit(onSubmit)}
                  sx={{ textTransform: 'none' }}
                >
                  {t('submit')}
                </Button>
              )}
            </Box>
          </Paper>
        )}
      </Box>
    </ClickAwayListener>
  );
};

export default CreateNoteFab;
