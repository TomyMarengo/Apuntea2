// src/pages/Directories/dialogs/EditDirectoryDialog.tsx

import { zodResolver } from '@hookform/resolvers/zod';
import FolderIcon from '@mui/icons-material/Folder';
import {
  Dialog,
  DialogTitle,
  DialogContent,
  DialogActions,
  Button,
  TextField,
  FormControlLabel,
  Switch,
  Box,
  Typography,
  ToggleButton,
  ToggleButtonGroup,
} from '@mui/material';
import React, { useEffect } from 'react';
import { useForm, Controller } from 'react-hook-form';
import { useTranslation } from 'react-i18next';
import { toast } from 'react-toastify';
import { z } from 'zod';

import { useUpdateDirectoryMutation } from '../../../store/slices/directoriesApiSlice';
import { Directory } from '../../../types';
import { FolderIconColor } from '../../../types';

interface DirectoryPageProps {
  open: boolean;
  onClose: () => void;
  directory: Directory;
  showNameOnly?: boolean;
}

const EditDirectoryDialog: React.FC<DirectoryPageProps> = ({
  open,
  onClose,
  directory,
  showNameOnly = false,
}) => {
  const { t } = useTranslation('editDirectoryDialog');
  const [updateDirectory] = useUpdateDirectoryMutation();

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
      name: directory.name,
      iconColor: `#${directory.iconColor}`,
      visible: directory.visible,
    },
  });

  useEffect(() => {
    reset({
      name: directory.name,
      iconColor: `#${directory.iconColor}`,
      visible: directory.visible,
    });
  }, [directory, reset]);

  // Handle form submission
  const onSubmit = async (data: DirectoryFormData) => {
    try {
      const result = await updateDirectory({
        directoryId: directory.id,
        name: data.name,
        visible: data.visible,
        iconColor: data.iconColor,
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
      console.error('Failed to edit directory:', error);
      toast.error(t('editFailed'));
    }
  };

  // Handle closing the dialog and resetting form state
  const handleClose = () => {
    reset({
      name: directory.name,
      iconColor: `#${directory.iconColor}`,
      visible: directory.visible,
    });
    onClose();
  };

  return (
    <Dialog open={open} onClose={handleClose} fullWidth maxWidth="xs">
      <DialogTitle>{t('editDirectoryTitle')}</DialogTitle>
      <DialogContent>
        {/* Directory Name Field */}
        <Controller
          name="name"
          control={control}
          render={({ field }) => (
            <TextField
              {...field}
              margin="normal"
              fullWidth
              label={t('name')}
              error={!!errors.name}
              helperText={errors.name ? t(errors.name.message as string) : ''}
            />
          )}
        />

        {/* Icon Color Selector */}
        {!showNameOnly && (
          <>
            <Box sx={{ mb: 1, display: 'flex', flexDirection: 'column' }}>
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
          </>
        )}
      </DialogContent>
      <DialogActions>
        <Button onClick={handleClose} color="primary">
          {t('cancel')}
        </Button>
        <Button onClick={handleSubmit(onSubmit)} variant="contained">
          {t('save')}
        </Button>
      </DialogActions>
    </Dialog>
  );
};

export default EditDirectoryDialog;
