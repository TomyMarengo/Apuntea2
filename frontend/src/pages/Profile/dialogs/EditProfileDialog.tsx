// src/pages/Profile/dialogs/EditProfileDialog.tsx

import { zodResolver } from '@hookform/resolvers/zod';
import { Close as CloseIcon, PhotoCamera } from '@mui/icons-material';
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

  // State for preview of the profile picture
  const [preview, setPreview] = useState<string | null>(null);

  // RTK mutations
  const [updateUser, { isLoading: isUpdatingUser }] = useUpdateUserMutation();
  const [updatePicture, { isLoading: isUpdatingPicture }] =
    useUpdatePictureMutation();

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
    email: z.string().email(t('validation.emailInvalid')).optional(),
    careerId: z.string().uuid(t('validation.careerIdInvalid')).optional(),
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
  });

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
        profilePicture: undefined,
      });
      setPreview(null);
    }
  }, [open, user]);

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

  /**
   * Handle form submission for editing user info (no password logic here).
   */
  const onSubmit = async (data: z.infer<typeof editProfileSchema>) => {
    let isUserUpdateSuccessful = true;
    let isPictureUpdateSuccessful = true;

    try {
      // Create an object with only the modified fields
      const updatedFields: UpdateUserArgs = { userId: user.id };

      if (dirtyFields.firstName) updatedFields.firstName = data.firstName;
      if (dirtyFields.lastName) updatedFields.lastName = data.lastName;
      if (dirtyFields.username) updatedFields.username = data.username;
      if (dirtyFields.email) updatedFields.email = data.email;
      if (dirtyFields.careerId) updatedFields.careerId = data.careerId;

      // Update user info if any fields are dirty
      if (Object.keys(updatedFields).length > 1) {
        const result = await updateUser(updatedFields).unwrap();
        if (!result.success) {
          isUserUpdateSuccessful = false;
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

      // Update profile picture if it's dirty
      if (
        dirtyFields.profilePicture &&
        data.profilePicture &&
        data.profilePicture.length > 0
      ) {
        const pictureResult = await updatePicture({
          profilePicture: data.profilePicture[0],
        }).unwrap();

        if (!pictureResult.success) {
          isPictureUpdateSuccessful = false;
          toast.error(t('messages.failedToUpdatePicture'));
        }
      }

      // Show success messages based on what was successful
      if (isUserUpdateSuccessful && isPictureUpdateSuccessful) {
        toast.success(t('messages.profileUpdatedSuccessfully'));
        onUpdateSuccess();
        handleClose();
      } else {
        if (isUserUpdateSuccessful) {
          toast.success(t('messages.profileUpdatedSuccessfully'));
        }
        if (isPictureUpdateSuccessful) {
          toast.success(t('messages.pictureUpdatedSuccessfully'));
        }
      }
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
        {/* Profile Picture Section */}
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
      </DialogContent>

      {/* Dialog Actions */}
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
