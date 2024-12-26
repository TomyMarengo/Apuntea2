// src/pages/Profile/EditProfileDialog.tsx

import React, { useState, useEffect } from 'react';
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
} from '@mui/material';
import { Close as CloseIcon, PhotoCamera } from '@mui/icons-material';
import { useTranslation } from 'react-i18next';
import {
  useUpdateUserMutation,
  useUpdatePictureMutation,
} from '../../store/slices/usersApiSlice';
import { Career } from '../../types';
import { useGetCareersQuery } from '../../store/slices/institutionsApiSlice';

interface EditProfileDialogProps {
  open: boolean;
  handleClose: () => void;
  user: any; // Ideally, use the User type
  onUpdateSuccess: () => void;
}

const EditProfileDialog: React.FC<EditProfileDialogProps> = ({
  open,
  handleClose,
  user,
  onUpdateSuccess,
}) => {
  const { t } = useTranslation();

  const [firstName, setFirstName] = useState(user.firstName || '');
  const [lastName, setLastName] = useState(user.lastName || '');
  const [username, setUsername] = useState(user.username || '');
  const [email, setEmail] = useState(user.email || '');
  const [careerId, setCareerId] = useState(user.career?.id || '');
  const [profilePicture, setProfilePicture] = useState<File | null>(null);

  const [updateUser, { isLoading: isUpdatingUser }] = useUpdateUserMutation();
  const [updatePicture, { isLoading: isUpdatingPicture }] =
    useUpdatePictureMutation();

  const { data: careers, isLoading: isLoadingCareers } = useGetCareersQuery(
    { institutionId: user.institution?.id },
    { skip: !user.institution?.id },
  );

  useEffect(() => {
    if (open) {
      setFirstName(user.firstName || '');
      setLastName(user.lastName || '');
      setUsername(user.username || '');
      setEmail(user.email || '');
      setCareerId(user.career?.id || '');
      setProfilePicture(null);
    }
  }, [open, user]);

  const handleSave = async () => {
    try {
      // Update user info
      await updateUser({
        userId: user.id,
        firstName,
        lastName,
        username,
        email,
        careerId: careerId || undefined, // Avoid sending empty string
      }).unwrap();

      // Update profile picture if changed
      if (profilePicture) {
        await updatePicture({
          url: `/users/${user.id}/profile-picture`, // Adjust the endpoint as necessary
          profilePicture,
        }).unwrap();
      }

      onUpdateSuccess(); // Refetch user data
      handleClose();
    } catch (error: any) {
      // Handle error (e.g., show a notification)
      console.error('Failed to update profile:', error);
      // Optionally, display error messages to the user
    }
  };

  const handleProfilePictureChange = (
    e: React.ChangeEvent<HTMLInputElement>,
  ) => {
    if (e.target.files && e.target.files[0]) {
      setProfilePicture(e.target.files[0]);
    }
  };

  return (
    <Dialog open={open} onClose={handleClose} fullWidth maxWidth="sm">
      <DialogTitle>
        {t('editProfile')}
        <IconButton
          aria-label={t('close')}
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
            src={user.profilePictureUrl || ''}
            alt={user.username}
            sx={{ width: 100, height: 100, mb: 2 }}
          />
          <label htmlFor="profile-picture-upload">
            <input
              accept="image/*"
              id="profile-picture-upload"
              type="file"
              hidden
              onChange={handleProfilePictureChange}
            />
            <IconButton
              color="primary"
              aria-label="upload picture"
              component="span"
            >
              <PhotoCamera />
            </IconButton>
            {profilePicture && (
              <Typography variant="body2">{profilePicture.name}</Typography>
            )}
          </label>
        </Box>
        <Box component="form" noValidate autoComplete="off">
          <TextField
            label={t('firstName')}
            variant="outlined"
            fullWidth
            margin="normal"
            value={firstName}
            onChange={(e) => setFirstName(e.target.value)}
          />
          <TextField
            label={t('lastName')}
            variant="outlined"
            fullWidth
            margin="normal"
            value={lastName}
            onChange={(e) => setLastName(e.target.value)}
          />
          <TextField
            label={t('username')}
            variant="outlined"
            fullWidth
            margin="normal"
            value={username}
            onChange={(e) => setUsername(e.target.value)}
          />
          <TextField
            label={t('email')}
            variant="outlined"
            fullWidth
            margin="normal"
            value={email}
            onChange={(e) => setEmail(e.target.value)}
          />
          <FormControl fullWidth margin="normal" variant="outlined">
            <InputLabel>{t('career')}</InputLabel>
            <Select
              label={t('career')}
              value={careerId}
              onChange={(e) => setCareerId(e.target.value)}
              disabled={isLoadingCareers}
            >
              <MenuItem value="">
                <em>{t('selectCareer')}</em>
              </MenuItem>
              {careers?.map((career: Career) => (
                <MenuItem key={career.id} value={career.id}>
                  {career.name}
                </MenuItem>
              ))}
            </Select>
          </FormControl>
        </Box>
      </DialogContent>
      <DialogActions>
        <Button
          onClick={handleClose}
          disabled={isUpdatingUser || isUpdatingPicture}
        >
          {t('cancel')}
        </Button>
        <Button
          onClick={handleSave}
          variant="contained"
          disabled={isUpdatingUser || isUpdatingPicture}
        >
          {isUpdatingUser || isUpdatingPicture ? t('saving') : t('saveChanges')}
        </Button>
      </DialogActions>
    </Dialog>
  );
};

export default EditProfileDialog;
