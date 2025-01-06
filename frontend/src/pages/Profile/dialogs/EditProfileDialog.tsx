import { zodResolver } from '@hookform/resolvers/zod';
import { Close as CloseIcon } from '@mui/icons-material';
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
  Typography,
  Box,
  IconButton,
} from '@mui/material';
import React, { useEffect } from 'react';
import { useForm, Controller } from 'react-hook-form';
import { useTranslation } from 'react-i18next';
import { toast } from 'react-toastify';
import { z } from 'zod';

import { useGetCareersQuery } from '../../../store/slices/institutionsApiSlice';
import {
  useUpdateUserMutation,
  UpdateUserArgs,
} from '../../../store/slices/usersApiSlice';
import { Career, User } from '../../../types';

interface EditProfileDialogProps {
  open: boolean;
  handleClose: () => void;
  user: User;
  onUpdateSuccess: () => void;
}

const EditProfileDialog: React.FC<EditProfileDialogProps> = ({
  open,
  handleClose,
  user,
  onUpdateSuccess,
}) => {
  const { t } = useTranslation('editProfileDialog');

  // RTK mutations
  const [updateUser, { isLoading: isUpdatingUser }] = useUpdateUserMutation();

  // Fetch careers
  const { data: careers, isLoading: isLoadingCareers } = useGetCareersQuery(
    { institutionId: user.institution?.id },
    { skip: !user.institution?.id },
  );

  // Schema for editing profile (no password fields)
  const editProfileSchema = z.object({
    firstName: z
      .string()
      .regex(
        /^([a-zA-ZáéíóúÁÉÍÓÚñÑüÜ]+[ ]?)*$/,
        t('validation.firstNameInvalid'),
      )
      .max(20, t('validation.firstNameMaxLength'))
      .optional(),
    lastName: z
      .string()
      .regex(
        /^([a-zA-ZáéíóúÁÉÍÓÚñÑüÜ]+[ ]?)*$/,
        t('validation.lastNameInvalid'),
      )
      .max(20, t('validation.lastNameMaxLength'))
      .optional(),
    username: z
      .string()
      .regex(
        /^[a-zA-Z0-9.!#$%&'*+/=?^_`{|}~-]+$/,
        t('validation.usernameInvalid'),
      )
      .max(30, t('validation.usernameMaxLength'))
      .optional(),
    careerId: z.string().uuid(t('validation.careerIdInvalid')).optional(),
  });

  const {
    control,
    handleSubmit,
    reset,
    formState: { errors, dirtyFields },
  } = useForm<z.infer<typeof editProfileSchema>>({
    resolver: zodResolver(editProfileSchema),
    defaultValues: {
      firstName: user.firstName,
      lastName: user.lastName,
      username: user.username,
      careerId: user.career?.id,
    },
  });

  // Reset form when the dialog opens
  useEffect(() => {
    if (open) {
      reset({
        firstName: user.firstName,
        lastName: user.lastName,
        username: user.username,
        careerId: user.career?.id,
      });
    }
  }, [open, user]);

  /**
   * Handle form submission for editing user info (no password logic here).
   */
  const onSubmit = async (data: z.infer<typeof editProfileSchema>) => {
    try {
      // Create an object with only the modified fields
      const updatedFields: UpdateUserArgs = { userId: user.id };

      if (dirtyFields.firstName) updatedFields.firstName = data.firstName;
      if (dirtyFields.lastName) updatedFields.lastName = data.lastName;
      if (dirtyFields.username) updatedFields.username = data.username;
      if (dirtyFields.careerId) updatedFields.careerId = data.careerId;

      // Update user info if any fields are dirty
      if (Object.keys(updatedFields).length > 1) {
        const result = await updateUser(updatedFields).unwrap();
        if (result.success) {
          toast.success(t('messages.profileUpdatedSuccessfully'));
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
      }
    } catch (error: any) {
      console.error('Failed to update profile:', error);

      if (error.data?.[0]?.message) {
        toast.error(error.data[0].message);
      } else {
        toast.error(t('messages.failedToUpdateProfile', { errorMessage: '' }));
      }
    }
  };

  return (
    <Dialog open={open} onClose={handleClose} fullWidth maxWidth="sm">
      <DialogTitle>
        {t('titles.editProfile')}
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
        {/* User Info Form */}
        <Box component="form" noValidate autoComplete="off">
          <Controller
            name="firstName"
            control={control}
            render={({ field }) => (
              <TextField
                {...field}
                label={t('labels.firstName')}
                variant="outlined"
                fullWidth
                margin="normal"
                error={!!errors.firstName}
                helperText={errors.firstName?.message}
              />
            )}
          />
          <Controller
            name="lastName"
            control={control}
            render={({ field }) => (
              <TextField
                {...field}
                label={t('labels.lastName')}
                variant="outlined"
                fullWidth
                margin="normal"
                error={!!errors.lastName}
                helperText={errors.lastName?.message}
              />
            )}
          />
          <Controller
            name="username"
            control={control}
            render={({ field }) => (
              <TextField
                {...field}
                label={t('labels.username')}
                variant="outlined"
                fullWidth
                margin="normal"
                error={!!errors.username}
                helperText={errors.username?.message}
              />
            )}
          />
          <Controller
            name="careerId"
            control={control}
            render={({ field }) => (
              <FormControl
                fullWidth
                margin="normal"
                variant="outlined"
                error={!!errors.careerId}
              >
                <InputLabel>{t('labels.career')}</InputLabel>
                <Select
                  {...field}
                  label={t('labels.career')}
                  disabled={isLoadingCareers}
                >
                  <MenuItem value="">
                    <em>{t('placeholders.selectCareer')}</em>
                  </MenuItem>
                  {careers?.map((career: Career) => (
                    <MenuItem key={career.id} value={career.id}>
                      {career.name}
                    </MenuItem>
                  ))}
                </Select>
                {errors.careerId && (
                  <Typography color="error" variant="body2">
                    {errors.careerId.message}
                  </Typography>
                )}
              </FormControl>
            )}
          />
        </Box>
      </DialogContent>

      {/* Dialog Actions */}
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

export default EditProfileDialog;
