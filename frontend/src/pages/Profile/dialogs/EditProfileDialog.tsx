// src/pages/Profile/dialogs/EditProfileDialog.tsx

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
import React, { useState, useEffect } from 'react';
import { useTranslation } from 'react-i18next';
import { toast } from 'react-toastify';

import { useGetCareersQuery } from '../../../store/slices/institutionsApiSlice';
import {
  useUpdateUserMutation,
  useUpdatePictureMutation,
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

  const [firstName, setFirstName] = useState(user.firstName || '');
  const [lastName, setLastName] = useState(user.lastName || '');
  const [username, setUsername] = useState(user.username || '');
  const [email, setEmail] = useState(user.email || '');
  const [careerId, setCareerId] = useState(user.career?.id || '');
  const [profilePicture, setProfilePicture] = useState<File | null>(null);
  const [preview, setPreview] = useState<string | null>(null);

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
      setPreview(null);
      setCurrentPassword('');
      setNewPassword('');
      setConfirmNewPassword('');
      setShowCurrentPassword(false);
      setShowNewPassword(false);
      setShowConfirmNewPassword(false);
    }
  }, [open, user]);

  useEffect(() => {
    if (profilePicture) {
      const objectUrl = URL.createObjectURL(profilePicture);
      setPreview(objectUrl);

      return () => {
        URL.revokeObjectURL(objectUrl);
      };
    } else {
      setPreview(null);
    }
  }, [profilePicture]);

  const handleSave = async () => {
    try {
      if (newPassword && newPassword !== confirmNewPassword) {
        toast.error(t('passwordsDoNotMatch'));
        return;
      }

      let passwordChanged = false;

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

      if (newPassword) {
        passwordChanged = true;
      }

      if (profilePicture) {
        await updatePicture({
          profilePicture,
        }).unwrap();
      }

      if (passwordChanged) {
        toast.success(t('passwordChangedSuccessfully'));
      } else {
        toast.success(t('profileUpdatedSuccessfully'));
      }

      onUpdateSuccess();
      handleClose();
    } catch (error: any) {
      console.error('Failed to update profile:', error);

      if (error.data?.[0]?.message) {
        toast.error(error.data[0].message);
      } else {
        toast.error(t('failedToUpdateProfile'));
      }
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
            src={preview || user.profilePictureUrl || ''}
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
                aria-label={t('uploadPicture')}
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
        <Box sx={{ mt: 4 }}>
          <Typography variant="h6" gutterBottom>
            {t('changePassword')}
          </Typography>
          <TextField
            label={t('currentPassword')}
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
            label={t('newPassword')}
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
            label={t('confirmNewPassword')}
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
