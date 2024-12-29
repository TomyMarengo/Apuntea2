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
  Typography,
  InputAdornment,
} from '@mui/material';
import {
  Close as CloseIcon,
  PhotoCamera,
  Visibility,
  VisibilityOff,
} from '@mui/icons-material';
import { useTranslation } from 'react-i18next';
import {
  useUpdateUserMutation,
  useUpdatePictureMutation,
} from '../../store/slices/usersApiSlice';
import { Career, User } from '../../types';
import { useGetCareersQuery } from '../../store/slices/institutionsApiSlice';

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
  const { t } = useTranslation();

  const [firstName, setFirstName] = useState(user.firstName || '');
  const [lastName, setLastName] = useState(user.lastName || '');
  const [username, setUsername] = useState(user.username || '');
  const [email, setEmail] = useState(user.email || '');
  const [careerId, setCareerId] = useState(user.career?.id || '');
  const [profilePicture, setProfilePicture] = useState<File | null>(null);

  // States for passwords
  const [currentPassword, setCurrentPassword] = useState('');
  const [newPassword, setNewPassword] = useState('');
  const [confirmNewPassword, setConfirmNewPassword] = useState('');

  // States to toggle password visibility
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

  useEffect(() => {
    if (open) {
      setFirstName(user.firstName || '');
      setLastName(user.lastName || '');
      setUsername(user.username || '');
      setEmail(user.email || '');
      setCareerId(user.career?.id || '');
      setProfilePicture(null);
      setCurrentPassword('');
      setNewPassword('');
      setConfirmNewPassword('');
      setShowCurrentPassword(false);
      setShowNewPassword(false);
      setShowConfirmNewPassword(false);
    }
  }, [open, user]);

  const handleSave = async () => {
    try {
      // Validate if the password is being changed
      if (newPassword && newPassword !== confirmNewPassword) {
        alert(t('editProfileDialog.passwordsDoNotMatch'));
        return;
      }

      // Update user information
      await updateUser({
        userId: user.id,
        firstName,
        lastName,
        username,
        email,
        careerId: careerId || undefined,
        ...(newPassword &&
          currentPassword && {
            password: newPassword,
            oldPassword: currentPassword,
          }),
      }).unwrap();

      // Update profile picture if changed
      if (profilePicture) {
        await updatePicture({
          profilePicture,
        }).unwrap();
      }

      onUpdateSuccess(); // Refresh user data
      handleClose();
    } catch (error: any) {
      console.error('Failed to update profile:', error);
      // Optional: show error messages to the user
      alert(
        error.data?.[0]?.message ||
          t('editProfileDialog.failedToUpdateProfile'),
      );
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
        {t('editProfileDialog.editProfile')}
        <IconButton
          aria-label={t('editProfileDialog.close')}
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
        {/* Profile picture section */}
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
            <Box sx={{ display: 'flex', alignItems: 'center' }}>
              <IconButton
                color="primary"
                aria-label={t('editProfileDialog.uploadPicture')}
                component="span"
              >
                <PhotoCamera />
              </IconButton>
              {profilePicture && (
                <Typography variant="body2">{profilePicture.name}</Typography>
              )}
            </Box>
          </label>
        </Box>

        {/* User information section */}
        <Box component="form" noValidate autoComplete="off">
          <TextField
            label={t('editProfileDialog.firstName')}
            variant="outlined"
            fullWidth
            margin="normal"
            value={firstName}
            onChange={(e) => setFirstName(e.target.value)}
          />
          <TextField
            label={t('editProfileDialog.lastName')}
            variant="outlined"
            fullWidth
            margin="normal"
            value={lastName}
            onChange={(e) => setLastName(e.target.value)}
          />
          <TextField
            label={t('editProfileDialog.username')}
            variant="outlined"
            fullWidth
            margin="normal"
            value={username}
            onChange={(e) => setUsername(e.target.value)}
          />
          <TextField
            label={t('editProfileDialog.email')}
            variant="outlined"
            fullWidth
            margin="normal"
            value={email}
            onChange={(e) => setEmail(e.target.value)}
          />
          <FormControl fullWidth margin="normal" variant="outlined">
            <InputLabel>{t('editProfileDialog.career')}</InputLabel>
            <Select
              label={t('editProfileDialog.career')}
              value={careerId}
              onChange={(e) => setCareerId(e.target.value)}
              disabled={isLoadingCareers}
            >
              <MenuItem value="">
                <em>{t('editProfileDialog.selectCareer')}</em>
              </MenuItem>
              {careers?.map((career: Career) => (
                <MenuItem key={career.id} value={career.id}>
                  {career.name}
                </MenuItem>
              ))}
            </Select>
          </FormControl>
        </Box>

        {/* Password change section */}
        <Box sx={{ mt: 4 }}>
          <Typography variant="h6" gutterBottom>
            {t('editProfileDialog.changePassword')}
          </Typography>
          <TextField
            label={t('editProfileDialog.currentPassword')}
            variant="outlined"
            type={showCurrentPassword ? 'text' : 'password'}
            fullWidth
            margin="normal"
            value={currentPassword}
            onChange={(e) => setCurrentPassword(e.target.value)}
            InputProps={{
              endAdornment: (
                <InputAdornment position="end">
                  <IconButton
                    onClick={() => setShowCurrentPassword(!showCurrentPassword)}
                    edge="end"
                  >
                    {showCurrentPassword ? <VisibilityOff /> : <Visibility />}
                  </IconButton>
                </InputAdornment>
              ),
            }}
          />
          <TextField
            label={t('editProfileDialog.newPassword')}
            variant="outlined"
            type={showNewPassword ? 'text' : 'password'}
            fullWidth
            margin="normal"
            value={newPassword}
            onChange={(e) => setNewPassword(e.target.value)}
            InputProps={{
              endAdornment: (
                <InputAdornment position="end">
                  <IconButton
                    onClick={() => setShowNewPassword(!showNewPassword)}
                    edge="end"
                  >
                    {showNewPassword ? <VisibilityOff /> : <Visibility />}
                  </IconButton>
                </InputAdornment>
              ),
            }}
          />
          <TextField
            label={t('editProfileDialog.confirmNewPassword')}
            variant="outlined"
            type={showConfirmNewPassword ? 'text' : 'password'}
            fullWidth
            margin="normal"
            value={confirmNewPassword}
            onChange={(e) => setConfirmNewPassword(e.target.value)}
            InputProps={{
              endAdornment: (
                <InputAdornment position="end">
                  <IconButton
                    onClick={() =>
                      setShowConfirmNewPassword(!showConfirmNewPassword)
                    }
                    edge="end"
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
        </Box>
      </DialogContent>
      <DialogActions>
        <Button
          onClick={handleClose}
          disabled={isUpdatingUser || isUpdatingPicture}
        >
          {t('editProfileDialog.cancel')}
        </Button>
        <Button
          onClick={handleSave}
          variant="contained"
          disabled={isUpdatingUser || isUpdatingPicture}
        >
          {isUpdatingUser || isUpdatingPicture
            ? t('editProfileDialog.saving')
            : t('editProfileDialog.saveChanges')}
        </Button>
      </DialogActions>
    </Dialog>
  );
};

export default EditProfileDialog;
