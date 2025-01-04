// src/components/CreateDirectoryFab.tsx

import { zodResolver } from '@hookform/resolvers/zod';
import CreateNewFolderIcon from '@mui/icons-material/CreateNewFolder';
import FolderIcon from '@mui/icons-material/Folder';
import {
  Box,
  Fab,
  Tooltip,
  Typography,
  TextField,
  Button,
  FormControlLabel,
  Switch,
  Paper,
  ClickAwayListener,
  CircularProgress,
  ToggleButton,
  ToggleButtonGroup,
} from '@mui/material';
import React, { useState, useRef } from 'react';
import { useForm, Controller } from 'react-hook-form';
import { useTranslation } from 'react-i18next';
import { toast } from 'react-toastify';
import { z } from 'zod';

import { useCreateDirectoryMutation } from '../store/slices/directoriesApiSlice';
import { FolderIconColor } from '../types';

interface CreateDirectoryFabProps {
  parentId: string;
}

const CreateDirectoryFab: React.FC<CreateDirectoryFabProps> = ({
  parentId,
}) => {
  const { t } = useTranslation('createDirectoryFab');
  const [createDirectory, { isLoading }] = useCreateDirectoryMutation();

  const [expanded, setExpanded] = useState(false);

  // Reference to the wrapper for ClickAwayListener
  const containerRef = useRef<HTMLDivElement | null>(null);

  const directorySchema = z.object({
    name: z
      .string()
      .nonempty({ message: t('validation.nameNotEmpty') })
      .min(2, { message: t('validation.nameMinLength') })
      .max(50, { message: t('validation.nameMaxLength') })
      .regex(/^(?!([ ,\-_.]+)$)[a-zA-Z0-9áéíóúÁÉÍÓÚñÑüÜ .,\\-_]+$/, {
        message: t('validation.nameInvalid'),
      }),
    iconColor: z.string().regex(/^#(?:BBBBBB|16A765|4986E7|CD35A6)$/, {
      message: t('validation.colorInvalid'),
    }),
    visible: z.boolean().default(true),
  });

  type DirectoryFormData = z.infer<typeof directorySchema>;

  const {
    control,
    handleSubmit,
    reset,
    formState: { errors },
  } = useForm<DirectoryFormData>({
    resolver: zodResolver(directorySchema),
    defaultValues: {
      name: '',
      iconColor: '#BBBBBB',
      visible: true,
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
        iconColor: '#BBBBBB',
        visible: true,
      });
    }
  };

  const onSubmit = async (data: DirectoryFormData) => {
    try {
      const result = await createDirectory({
        name: data.name,
        iconColor: data.iconColor,
        visible: data.visible,
        parentId,
      }).unwrap();
      if (result.success) {
        toast.success(t('directoryCreated'));
        reset();
        setExpanded(false);
      } else {
        toast.error(
          t('directoryCreationFailed', {
            errorMessage:
              result.messages && result.messages.length > 0
                ? `: ${result.messages[0]}`
                : '',
          }),
        );
      }
    } catch (error: any) {
      toast.error(t('directoryCreationFailed', { errorMessage: '' }));
      console.error('Failed to create directory:', error);
    }
  };

  return (
    <ClickAwayListener onClickAway={handleClickAway}>
      <Box ref={containerRef}>
        {!expanded && (
          <Tooltip title={t('createNewDirectory')} placement="left">
            <Fab
              color="primary"
              onClick={handleFabClick}
              sx={{
                cursor: 'pointer',
              }}
            >
              <CreateNewFolderIcon sx={{ color: 'background.paper' }} />
            </Fab>
          </Tooltip>
        )}

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
              {t('createNewDirectory')}
            </Typography>

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

            {/* Icon Color Selection */}
            <Box>
              <Typography variant="subtitle2" sx={{ mb: 1 }}>
                {t('iconColor')}
              </Typography>
              <Controller
                name="iconColor"
                control={control}
                render={({ field }) => (
                  <ToggleButtonGroup
                    exclusive
                    value={field.value}
                    onChange={(_, value) => {
                      if (value !== null) {
                        field.onChange(value);
                      }
                    }}
                    sx={{ display: 'flex', gap: 1 }}
                  >
                    {Object.entries(FolderIconColor).map(([_, value]) => (
                      <ToggleButton
                        key={value}
                        value={value}
                        disableRipple
                        sx={{
                          padding: 0,
                          minWidth: 30,
                          width: 30,
                          height: 30,
                          border: 'none',
                          borderRadius: '50%',
                          backgroundColor: 'transparent',
                          transition: 'transform 0.2s ease-in-out',
                          boxShadow: 'none',
                          '&:hover': {
                            transform: 'scale(1.1)',
                            backgroundColor: 'transparent',
                          },
                          '&.Mui-selected': {
                            transform: 'scale(1.2)',
                            backgroundColor: 'transparent',
                          },
                          '&.Mui-selected:hover': {
                            transform: 'scale(1.3)',
                            backgroundColor: 'transparent',
                          },
                        }}
                      >
                        <FolderIcon
                          sx={{
                            fontSize: 30,
                            color: value,
                          }}
                        />
                      </ToggleButton>
                    ))}
                  </ToggleButtonGroup>
                )}
              />
              {errors.iconColor && (
                <Typography variant="caption" color="error">
                  {t(errors.iconColor.message as string)}
                </Typography>
              )}
            </Box>

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

            <Box sx={{ display: 'flex', justifyContent: 'flex-end', mt: 1 }}>
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

export default CreateDirectoryFab;
