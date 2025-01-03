// src/pages/Profile/dialogs/EditProfileDialog.tsx

import { zodResolver } from '@hookform/resolvers/zod';
import {
  Close as CloseIcon,
  PhotoCamera,
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
  Avatar,
  IconButton,
  Box,
  FormControl,
  InputLabel,
  Select,
  MenuItem,
  Typography,
  InputAdornment,
} from '@mui/material';
import React, { useEffect, useState } from 'react';
import { useForm, Controller } from 'react-hook-form';
import { useTranslation } from 'react-i18next';
import { toast } from 'react-toastify';
import { z } from 'zod';

import { useGetCareersQuery } from '../../../store/slices/institutionsApiSlice';
import {
  useUpdateUserMutation,
  useUpdatePictureMutation,
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

  const [preview, setPreview] = useState<string | null>(null);
  const [showCurrentPassword, setShowCurrentPassword] = useState(false);
  const [showNewPassword, setShowNewPassword] = useState(false);
  const [showConfirmNewPassword, setShowConfirmNewPassword] = useState(false);

  const [updateUser, { isLoading: isUpdatingUser }] = useUpdateUserMutation();
  const [updatePicture, { isLoading: isUpdatingPicture }] =
    useUpdatePictureMutation();

  const { data: careers, isLoading: isLoadingCareers } = useGetCareersQuery(
    { institutionId: user.institution?.id },
    { skip: !user.institution?.id },
  );

  const editProfileSchema = z
    .object({
      firstName: z
        .string()
        .regex(
          /^([a-zA-ZáéíóúÁÉÍÓÚñÑüÜ]+[ ]?)*$/,
          t('validation.firstNameInvalid'),
        )
        .max(20, t('validation.firstNameMax'))
        .optional(),
      lastName: z
        .string()
        .regex(
          /^([a-zA-ZáéíóúÁÉÍÓÚñÑüÜ]+[ ]?)*$/,
          t('validation.lastNameInvalid'),
        )
        .max(20, t('validation.lastNameMax'))
        .optional(),
      username: z
        .string()
        .regex(
          /^[a-zA-Z0-9.!#$%&'*+/=?^_`{|}~-]+$/,
          t('validation.usernameInvalid'),
        )
        .max(30, t('validation.usernameMax'))
        .optional(),
      email: z.string().email(t('validation.emailInvalid')).optional(),
      careerId: z.string().uuid(t('validation.careerIdInvalid')).optional(),
      currentPassword: z
        .string()
        .regex(
          /^(?=.*[a-z])(?=.*[A-Z])(?=.*[0-9]).+$/,
          t('validation.passwordInvalid'),
        )
        .min(4, t('validation.passwordMinLength'))
        .max(50, t('validation.passwordMaxLength'))
        .optional(),
      newPassword: z
        .string()
        .regex(
          /^(?=.*[a-z])(?=.*[A-Z])(?=.*[0-9]).+$/,
          t('validation.passwordInvalid'),
        )
        .min(4, t('validation.passwordMinLength'))
        .max(50, t('validation.passwordMaxLength'))
        .optional(),
      confirmNewPassword: z
        .string()
        .regex(
          /^(?=.*[a-z])(?=.*[A-Z])(?=.*[0-9]).+$/,
          t('validation.passwordInvalid'),
        )
        .min(4, t('validation.passwordMinLength'))
        .max(50, t('validation.passwordMaxLength'))
        .optional(),
      profilePicture: z
        .instanceof(FileList)
        .optional()
        .refine(
          (files) => {
            if (!files) return true;
            const file = files[0];
            if (!file) return true;
            const validTypes = ['image/jpeg', 'image/png', 'image/jpg'];
            const isValidType = validTypes.includes(file.type);
            const isValidSize = file.size <= 5 * 1024 * 1024; // 5MB
            return isValidType && isValidSize;
          },
          { message: t('validation.profilePicture') },
        ),
    })
    // Validation 1: currentPassword !== newPassword
    .refine(
      (data) => {
        if (data.newPassword && data.currentPassword) {
          return data.newPassword !== data.currentPassword;
        }
        return true;
      },
      {
        message: t('validation.newPasswordShouldBeDifferent'),
        path: ['newPassword'],
      },
    )
    // Validation 2: newPassword === confirmNewPassword
    .refine(
      (data) => {
        if (data.newPassword || data.confirmNewPassword) {
          return data.newPassword === data.confirmNewPassword;
        }
        return true;
      },
      {
        message: t('validation.passwordsDoNotMatch'),
        path: ['confirmNewPassword'],
      },
    )
    // Validation 3: currentPassword === undefined => newPassword === undefined
    .refine(
      (data) => {
        if (!data.currentPassword) {
          return !data.newPassword;
        }
        return true;
      },
      {
        message: t('validation.newPasswordRequired'),
        path: ['newPassword'],
      },
    );

  const {
    control,
    handleSubmit,
    reset,
    watch,
    formState: { errors, dirtyFields },
  } = useForm<z.infer<typeof editProfileSchema>>({
    resolver: zodResolver(editProfileSchema),
    defaultValues: {
      firstName: user.firstName,
      lastName: user.lastName,
      username: user.username,
      email: user.email,
      careerId: user.career?.id,
      currentPassword: undefined,
      newPassword: undefined,
      confirmNewPassword: undefined,
      profilePicture: undefined,
    },
  });

  const watchedProfilePicture = watch('profilePicture');

  // Reset form when the dialog opens
  useEffect(() => {
    if (open) {
      reset({
        firstName: user.firstName,
        lastName: user.lastName,
        username: user.username,
        email: user.email,
        careerId: user.career?.id,
        currentPassword: undefined,
        newPassword: undefined,
        confirmNewPassword: undefined,
        profilePicture: undefined,
      });
      setPreview(null);
      setShowCurrentPassword(false);
      setShowNewPassword(false);
      setShowConfirmNewPassword(false);
    }
  }, [open, user, reset]);

  // Update the image preview
  useEffect(() => {
    if (watchedProfilePicture && watchedProfilePicture.length > 0) {
      const file = watchedProfilePicture[0];
      const objectUrl = URL.createObjectURL(file);
      setPreview(objectUrl);

      return () => {
        URL.revokeObjectURL(objectUrl);
      };
    } else {
      setPreview(null);
    }
  }, [watchedProfilePicture]);

  const onSubmit = async (data: z.infer<typeof editProfileSchema>) => {
    try {
      // Create an object with only the modified fields
      const updatedFields: UpdateUserArgs = { userId: user.id };

      if (dirtyFields.firstName) updatedFields.firstName = data.firstName;
      if (dirtyFields.lastName) updatedFields.lastName = data.lastName;
      if (dirtyFields.username) updatedFields.username = data.username;
      if (dirtyFields.email) updatedFields.email = data.email;
      if (dirtyFields.careerId) updatedFields.careerId = data.careerId;

      if (
        dirtyFields.newPassword &&
        dirtyFields.confirmNewPassword &&
        dirtyFields.currentPassword
      ) {
        updatedFields.password = data.newPassword;
        updatedFields.oldPassword = data.currentPassword;
      }

      // Send only the modified fields if there are any
      if (Object.keys(updatedFields).length > 1) {
        // userId is always present
        await updateUser(updatedFields).unwrap();
      }

      // Handle profile picture update if it was modified
      if (
        dirtyFields.profilePicture &&
        data.profilePicture &&
        data.profilePicture.length > 0
      ) {
        await updatePicture({
          profilePicture: data.profilePicture[0],
        }).unwrap();
      }

      // Show success messages
      if (updatedFields.password) {
        toast.success(t('messages.passwordChangedSuccessfully'));
      } else {
        toast.success(t('messages.profileUpdatedSuccessfully'));
      }

      onUpdateSuccess();
      handleClose();
    } catch (error: any) {
      console.error('Failed to update profile:', error);

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
        <Box
          sx={{
            display: 'flex',
            alignItems: 'center',
            mb: 3,
            flexDirection: 'column',
          }}
        >
          <Avatar
            src={preview || user.profilePictureUrl || ''}
            alt={user.username}
            sx={{ width: 100, height: 100, mb: 2 }}
          />
          <Controller
            name="profilePicture"
            control={control}
            render={({ field }) => (
              <label htmlFor="profile-picture-upload">
                <input
                  accept="image/*"
                  id="profile-picture-upload"
                  type="file"
                  hidden
                  onChange={(e) => {
                    field.onChange(e.target.files);
                  }}
                />
                <Box sx={{ display: 'flex', alignItems: 'center' }}>
                  <IconButton
                    color="primary"
                    aria-label={t('buttons.uploadPicture')}
                    component="span"
                  >
                    <PhotoCamera />
                  </IconButton>
                  {field.value && field.value.length > 0 && (
                    <Typography variant="body2">
                      {field.value[0].name}
                    </Typography>
                  )}
                </Box>
              </label>
            )}
          />
          {errors.profilePicture && (
            <Typography color="error" variant="body2">
              {errors.profilePicture.message}
            </Typography>
          )}
        </Box>
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
            name="email"
            control={control}
            render={({ field }) => (
              <TextField
                {...field}
                label={t('labels.email')}
                variant="outlined"
                fullWidth
                margin="normal"
                error={!!errors.email}
                helperText={errors.email?.message}
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
        <Box sx={{ mt: 4 }}>
          <Typography variant="h6" gutterBottom>
            {t('titles.changePassword')}
          </Typography>
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
        <Button
          onClick={handleClose}
          disabled={isUpdatingUser || isUpdatingPicture}
        >
          {t('buttons.cancel')}
        </Button>
        <Button
          onClick={handleSubmit(onSubmit)}
          variant="contained"
          disabled={isUpdatingUser || isUpdatingPicture}
        >
          {isUpdatingUser || isUpdatingPicture
            ? t('buttons.saving')
            : t('buttons.saveChanges')}
        </Button>
      </DialogActions>
    </Dialog>
  );
};

export default EditProfileDialog;
