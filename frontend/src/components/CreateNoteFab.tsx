// src/components/CreateNoteFab.tsx
import { zodResolver } from '@hookform/resolvers/zod';
import NoteAddIcon from '@mui/icons-material/NoteAdd';
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
import React, { useState, useRef } from 'react';
import { useForm, Controller } from 'react-hook-form';
import { useTranslation } from 'react-i18next';
import { toast } from 'react-toastify';
import { z } from 'zod';

import { useCreateNoteMutation } from '../store/slices/notesApiSlice';
import { NoteCategory } from '../types';

// Define the Zod schema for form validation
const noteSchema = z.object({
  name: z
    .string()
    .nonempty({ message: 'notEmpty' })
    .min(2, { message: 'minLength' })
    .max(50, { message: 'maxLength' })
    .regex(/^(?!([ ,\-_.]+)$)[a-zA-Z0-9áéíóúÁÉÍÓÚñÑüÜ .,\\-_]+$/, {
      message: 'invalidName',
    }),
  category: z.string().regex(/^(THEORY|PRACTICE|EXAM|OTHER)$/, {
    message: 'invalidCategory',
  }),
  visible: z.boolean().default(true),
  file: z
    .instanceof(File)
    .refine((file) => file.size <= 100 * 1024 * 1024, {
      message: 'fileTooLarge',
    })
    .refine((file) => /\.(jpg|jpeg|png|pdf|mp4|mp3)$/i.test(file.name), {
      message: 'invalidFileType',
    })
    .refine((file) => file !== undefined && file !== null, {
      message: 'fileNotEmpty',
    })
    .optional(),
});

type NoteFormData = z.infer<typeof noteSchema>;

interface CreateNoteFabProps {
  parentId: string;
}

const CreateNoteFab: React.FC<CreateNoteFabProps> = ({ parentId }) => {
  const { t } = useTranslation('createNoteFab');

  const [createNote, { isLoading }] = useCreateNoteMutation();
  const [expanded, setExpanded] = useState(false);
  const containerRef = useRef<HTMLDivElement | null>(null);

  const {
    control,
    handleSubmit,
    reset,
    formState: { errors },
  } = useForm<NoteFormData>({
    resolver: zodResolver(noteSchema),
    defaultValues: {
      name: '',
      category: NoteCategory.THEORY,
      visible: true,
      file: undefined,
    },
  });

  const handleFabClick = () => {
    setExpanded((prev) => !prev);
  };

  const handleClickAway = () => {
    if (expanded) {
      setExpanded(false);
      reset({
        name: '',
        category: NoteCategory.THEORY,
        visible: true,
        file: undefined,
      });
    }
  };

  const onSubmit = async (data: NoteFormData) => {
    if (!data.file) {
      toast.error(t('missingFields'));
      return;
    }

    try {
      const result = await createNote({
        name: data.name,
        parentId,
        visible: data.visible,
        file: data.file,
        category: data.category as NoteCategory,
      }).unwrap();

      if (result.success) {
        toast.success(t('noteCreated'));
        reset({
          name: '',
          category: NoteCategory.THEORY,
          visible: true,
          file: undefined,
        });
        setExpanded(false);
      } else {
        toast.error(
          t('noteCreationFailed', {
            errorMessage:
              result.messages && result.messages.length > 0
                ? `: ${result.messages[0]}`
                : '',
          }),
        );
      }
    } catch (error: any) {
      toast.error(t('noteCreationFailed'));
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
              <NoteAddIcon sx={{ color: 'background.paper' }} />
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
              name="name"
              control={control}
              render={({ field }) => (
                <TextField
                  {...field}
                  size="small"
                  label={t('name')}
                  error={!!errors.name}
                  helperText={
                    errors.name ? t(errors.name.message as string) : ''
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
                    {Object.values(NoteCategory).map((category) => (
                      <MenuItem key={category} value={category}>
                        {t(category.toLowerCase())}
                      </MenuItem>
                    ))}
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
                    label={t(field.value ? 'visible' : 'hidden')}
                  />
                )}
              />

              <Controller
                name="file"
                control={control}
                render={({ field }) => (
                  <Box>
                    <Button
                      variant="outlined"
                      component="label"
                      size="small"
                      sx={{
                        textTransform: 'none',
                        flexShrink: 0,
                        minWidth: '100px',
                        maxWidth: '100px',
                        overflow: 'hidden',
                        textOverflow: 'ellipsis',
                        whiteSpace: 'nowrap',
                        display: 'block',
                        textAlign: field.value ? 'left' : 'center',
                      }}
                    >
                      {field.value ? field.value.name : t('chooseFile')}
                      <input
                        type="file"
                        hidden
                        onChange={(e) => {
                          if (e.target.files && e.target.files[0]) {
                            field.onChange(e.target.files[0]);
                          }
                        }}
                      />
                    </Button>
                    {errors.file && (
                      <Typography color="error" variant="caption">
                        {t(errors.file.message as string)}
                      </Typography>
                    )}
                  </Box>
                )}
              />
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
