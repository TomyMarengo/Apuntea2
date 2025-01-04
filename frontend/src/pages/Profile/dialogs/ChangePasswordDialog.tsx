// src/pages/Profile/dialogs/ChangePasswordDialog.tsx

import { zodResolver } from '@hookform/resolvers/zod';
import {
  Close as CloseIcon,
  Visibility,
  VisibilityOff,
} from '@mui/icons-material';
import {
  Dialog,
  DialogTitle,
  DialogContent,
  DialogActions,
  Button,
  TextField,
  IconButton,
  Box,
  InputAdornment,
} from '@mui/material';
import React, { useEffect, useState } from 'react';
import { useForm, Controller } from 'react-hook-form';
import { useTranslation } from 'react-i18next';
import { toast } from 'react-toastify';
import { z } from 'zod';

import {
  useUpdateUserMutation,
  UpdateUserArgs,
} from '../../../store/slices/usersApiSlice';
import { User } from '../../../types';

interface ChangePasswordDialogProps {
  open: boolean;
  handleClose: () => void;
  user: User;
  onUpdateSuccess: () => void;
}

const ChangePasswordDialog: React.FC<ChangePasswordDialogProps> = ({
  open,
  handleClose,
  user,
  onUpdateSuccess,
}) => {
  const { t } = useTranslation('changePasswordDialog');

  // Local state to show/hide passwords
  const [showCurrentPassword, setShowCurrentPassword] = useState(false);
  const [showNewPassword, setShowNewPassword] = useState(false);
  const [showConfirmNewPassword, setShowConfirmNewPassword] = useState(false);

  // RTK mutation
  const [updateUser, { isLoading: isUpdatingUser }] = useUpdateUserMutation();

  // Schema for password change
  const passwordSchema = z
    .object({
      currentPassword: z
        .string()
        .regex(
          /^(?=.*[a-z])(?=.*[A-Z])(?=.*[0-9]).+$/,
          t('validation.passwordInvalid'),
        )
        .min(4, t('validation.passwordMinLength'))
        .max(50, t('validation.passwordMaxLength')),
      newPassword: z
        .string()
        .regex(
          /^(?=.*[a-z])(?=.*[A-Z])(?=.*[0-9]).+$/,
          t('validation.passwordInvalid'),
        )
        .min(4, t('validation.passwordMinLength'))
        .max(50, t('validation.passwordMaxLength')),
      confirmNewPassword: z
        .string()
        .regex(
          /^(?=.*[a-z])(?=.*[A-Z])(?=.*[0-9]).+$/,
          t('validation.passwordInvalid'),
        )
        .min(4, t('validation.passwordMinLength'))
        .max(50, t('validation.passwordMaxLength')),
    })
    // Validation 1: newPassword !== currentPassword
    .refine((data) => data.newPassword !== data.currentPassword, {
      message: t('validation.newPasswordShouldBeDifferent'),
      path: ['newPassword'],
    })
    // Validation 2: newPassword === confirmNewPassword
    .refine((data) => data.newPassword === data.confirmNewPassword, {
      message: t('validation.passwordsDoNotMatch'),
      path: ['confirmNewPassword'],
    });

  const {
    control,
    handleSubmit,
    reset,
    formState: { errors },
  } = useForm<z.infer<typeof passwordSchema>>({
    resolver: zodResolver(passwordSchema),
    defaultValues: {
      currentPassword: '',
      newPassword: '',
      confirmNewPassword: '',
    },
  });

  // Reset form on dialog open
  useEffect(() => {
    if (open) {
      reset({
        currentPassword: '',
        newPassword: '',
        confirmNewPassword: '',
      });
      setShowCurrentPassword(false);
      setShowNewPassword(false);
      setShowConfirmNewPassword(false);
    }
  }, [open]);

  /**
   * Handle password update submission.
   */
  const onSubmit = async (data: z.infer<typeof passwordSchema>) => {
    try {
      const updateArgs: UpdateUserArgs = {
        userId: user.id,
        password: data.newPassword,
        oldPassword: data.currentPassword,
      };

      // Update user password
      const result = await updateUser(updateArgs).unwrap();
      console.log('Password updated:', result);
      if (result?.success) {
        toast.success(t('messages.passwordChangedSuccessfully'));
        onUpdateSuccess();
        handleClose();
      } else {
        toast.error(
          t('messages.failedToUpdateProfile', {
            errorMessage:
              result.messages && result.messages.length > 0
                ? `: ${result.messages[0]}`
                : '',
          }),
        );
      }
    } catch (error: any) {
      console.error('Failed to update password:', error);

      if (error.data?.[0]?.message) {
        toast.error(error.data[0].message);
      } else {
        toast.error(t('messages.failedToUpdateProfile'));
      }
    }
  };

  return (
    <Dialog open={open} onClose={handleClose} fullWidth maxWidth="sm">
      <DialogTitle>
        {t('titles.changePassword')}
        <IconButton
          aria-label={t('buttons.close')}
          onClick={handleClose}
          sx={{
            position: 'absolute',
            right: 8,
            top: 8,
          }}
        >
          <CloseIcon />
        </IconButton>
      </DialogTitle>

      <DialogContent dividers>
        <Box component="form" noValidate autoComplete="off">
          <Controller
            name="currentPassword"
            control={control}
            render={({ field }) => (
              <TextField
                {...field}
                label={t('labels.currentPassword')}
                variant="outlined"
                type={showCurrentPassword ? 'text' : 'password'}
                fullWidth
                margin="normal"
                error={!!errors.currentPassword}
                helperText={errors.currentPassword?.message}
                InputProps={{
                  endAdornment: (
                    <InputAdornment position="end">
                      <IconButton
                        onClick={() =>
                          setShowCurrentPassword(!showCurrentPassword)
                        }
                        edge="end"
                        tabIndex={-1}
                      >
                        {showCurrentPassword ? (
                          <VisibilityOff />
                        ) : (
                          <Visibility />
                        )}
                      </IconButton>
                    </InputAdornment>
                  ),
                }}
              />
            )}
          />

          <Controller
            name="newPassword"
            control={control}
            render={({ field }) => (
              <TextField
                {...field}
                label={t('labels.newPassword')}
                variant="outlined"
                type={showNewPassword ? 'text' : 'password'}
                fullWidth
                margin="normal"
                error={!!errors.newPassword}
                helperText={errors.newPassword?.message}
                InputProps={{
                  endAdornment: (
                    <InputAdornment position="end">
                      <IconButton
                        onClick={() => setShowNewPassword(!showNewPassword)}
                        edge="end"
                        tabIndex={-1}
                      >
                        {showNewPassword ? <VisibilityOff /> : <Visibility />}
                      </IconButton>
                    </InputAdornment>
                  ),
                }}
              />
            )}
          />

          <Controller
            name="confirmNewPassword"
            control={control}
            render={({ field }) => (
              <TextField
                {...field}
                label={t('labels.confirmNewPassword')}
                variant="outlined"
                type={showConfirmNewPassword ? 'text' : 'password'}
                fullWidth
                margin="normal"
                error={!!errors.confirmNewPassword}
                helperText={errors.confirmNewPassword?.message}
                InputProps={{
                  endAdornment: (
                    <InputAdornment position="end">
                      <IconButton
                        onClick={() =>
                          setShowConfirmNewPassword(!showConfirmNewPassword)
                        }
                        edge="end"
                        tabIndex={-1}
                      >
                        {showConfirmNewPassword ? (
                          <VisibilityOff />
                        ) : (
                          <Visibility />
                        )}
                      </IconButton>
                    </InputAdornment>
                  ),
                }}
              />
            )}
          />
        </Box>
      </DialogContent>

      <DialogActions>
        <Button onClick={handleClose} disabled={isUpdatingUser}>
          {t('buttons.cancel')}
        </Button>
        <Button
          onClick={handleSubmit(onSubmit)}
          variant="contained"
          disabled={isUpdatingUser}
        >
          {isUpdatingUser ? t('buttons.saving') : t('buttons.saveChanges')}
        </Button>
      </DialogActions>
    </Dialog>
  );
};

export default ChangePasswordDialog;
