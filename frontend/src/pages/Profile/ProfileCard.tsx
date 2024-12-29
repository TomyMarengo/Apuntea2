import React, { useState } from 'react';
import {
  Card,
  Typography,
  Avatar,
  IconButton,
  Box,
  Switch,
  FormControlLabel,
  Grid,
} from '@mui/material';
import { Edit as EditIcon } from '@mui/icons-material';
import { useTranslation } from 'react-i18next';
import { toast } from 'react-toastify'; // Import toast
import EditProfileDialog from './EditProfileDialog';
import { User } from '../../types';
import { useUpdateUserMutation } from '../../store/slices/usersApiSlice';

interface ProfileCardProps {
  user: User;
  onUpdateSuccess: () => void;
}

const ProfileCard: React.FC<ProfileCardProps> = ({ user, onUpdateSuccess }) => {
  const { t } = useTranslation();
  const [open, setOpen] = useState(false);
  const [updateUser] = useUpdateUserMutation();
  const [notificationsEnabled, setNotificationsEnabled] = useState(
    user.notificationsEnabled,
  );

  const handleOpen = () => setOpen(true);
  const handleClose = () => setOpen(false);

  const handleToggleNotifications = async (
    event: React.ChangeEvent<HTMLInputElement>,
  ) => {
    const newValue = event.target.checked;
    setNotificationsEnabled(newValue);
    try {
      await updateUser({
        userId: user.id,
        notificationsEnabled: newValue,
      }).unwrap();
      toast.success(
        newValue
          ? t('profileCard.enableEmailNotifications')
          : t('profileCard.disableEmailNotifications'),
      );
      onUpdateSuccess();
    } catch (error) {
      toast.error(t('profileCard.updateFailed'));
    }
  };

  return (
    <>
      <Card sx={{ p: 3, boxShadow: 3, borderRadius: 2 }}>
        <Grid container spacing={2} alignItems="center">
          <Grid item>
            <Avatar
              src={user.profilePictureUrl || ''}
              alt={user.username}
              sx={{ width: 120, height: 120 }}
            />
          </Grid>
          <Grid item xs>
            <Box
              sx={{
                display: 'flex',
                justifyContent: 'space-between',
                alignItems: 'center',
              }}
            >
              {user.firstName || user.lastName ? (
                <Typography variant="h5">
                  {user.firstName} {user.lastName}
                </Typography>
              ) : (
                <Typography variant="h5">{user.username}</Typography>
              )}
              <IconButton onClick={handleOpen}>
                <EditIcon />
              </IconButton>
            </Box>
            <Box sx={{ mt: 1 }}>
              <Typography variant="body1" color="textSecondary">
                <strong>{t('profileCard.username')}:</strong> {user.username}
              </Typography>
              <Typography variant="body1" color="textSecondary">
                <strong>{t('profileCard.email')}:</strong> {user.email}
              </Typography>
              <Typography variant="body1" color="textSecondary">
                <strong>{t('profileCard.career')}:</strong>{' '}
                {user.career?.name || t('profileCard.notSet')}
              </Typography>
              <Typography variant="body1" color="textSecondary">
                <strong>{t('profileCard.institution')}:</strong>{' '}
                {user.institution?.name || t('profileCard.notSet')}
              </Typography>
              <FormControlLabel
                control={
                  <Switch
                    checked={notificationsEnabled}
                    onChange={handleToggleNotifications}
                    color="primary"
                  />
                }
                label={
                  notificationsEnabled
                    ? t('profileCard.disableEmailNotifications')
                    : t('profileCard.enableEmailNotifications')
                }
                sx={{ mt: 2 }}
              />
            </Box>
          </Grid>
        </Grid>
      </Card>

      <EditProfileDialog
        open={open}
        handleClose={handleClose}
        user={user}
        onUpdateSuccess={onUpdateSuccess}
      />
    </>
  );
};

export default ProfileCard;
