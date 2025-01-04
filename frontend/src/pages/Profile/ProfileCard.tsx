// src/pages/Profile/ProfileCard.tsx

import { Edit as EditIcon, PhotoCamera } from '@mui/icons-material';
import {
  Card,
  Typography,
  Avatar,
  IconButton,
  Box,
  Switch,
  FormControlLabel,
  Grid,
  Button,
  CircularProgress,
  Tooltip,
} from '@mui/material';
import React, { useState, useEffect } from 'react';
import { useTranslation } from 'react-i18next';
import { toast } from 'react-toastify';

import ChangePasswordDialog from './dialogs/ChangePasswordDialog';
import EditProfileDialog from './dialogs/EditProfileDialog';
import FollowersModal from '../../components/Follow/FollowersModal';
import FollowingModal from '../../components/Follow/FollowingModal';
import {
  useUpdateUserMutation,
  useUpdatePictureMutation,
  useGetFollowersQuery,
  useGetFollowingsQuery,
} from '../../store/slices/usersApiSlice';
import { User } from '../../types';

interface ProfileCardProps {
  user: User;
  onUpdateSuccess: () => void;
}

const ProfileCard: React.FC<ProfileCardProps> = ({ user, onUpdateSuccess }) => {
  const { t } = useTranslation('profileCard');
  const [editModalOpen, setEditModalOpen] = useState(false);
  const [changePasswordModalOpen, setChangePasswordModalOpen] = useState(false);
  const [followersModalOpen, setFollowersModalOpen] = useState(false);
  const [followingModalOpen, setFollowingModalOpen] = useState(false);
  const [notificationsEnabled, setNotificationsEnabled] = useState(
    user.notificationsEnabled,
  );
  const [preview, setPreview] = useState<string | null>(null);

  const [updateUser] = useUpdateUserMutation();
  const [updatePicture] = useUpdatePictureMutation();

  useEffect(() => {
    setPreview(user.profilePictureUrl || null);
  }, [user.profilePictureUrl]);

  const handleEditClick = () => setEditModalOpen(true);
  const handleEditModalClose = () => setEditModalOpen(false);

  // Handler to open/close Change Password dialog
  const handleChangePasswordClick = () => setChangePasswordModalOpen(true);
  const handleChangePasswordModalClose = () =>
    setChangePasswordModalOpen(false);

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
          ? t('enableEmailNotifications')
          : t('disableEmailNotifications'),
      );
      onUpdateSuccess();
    } catch (error) {
      console.error('Failed to update user:', error);
      toast.error(t('updateFailed'));
    }
  };

  const { data: followersData, isLoading: isLoadingFollowers } =
    useGetFollowersQuery({
      url: user.followingUrl,
      page: 1,
      pageSize: 1,
    });

  const { data: followingsData, isLoading: isLoadingFollowings } =
    useGetFollowingsQuery({
      url: user.followedByUrl,
      page: 1,
      pageSize: 1,
    });

  const handleFollowersClick = () => setFollowersModalOpen(true);
  const handleFollowersModalClose = () => setFollowersModalOpen(false);

  const handleFollowingClick = () => setFollowingModalOpen(true);
  const handleFollowingModalClose = () => setFollowingModalOpen(false);

  const handleProfilePictureChange = async (
    event: React.ChangeEvent<HTMLInputElement>,
  ) => {
    const files = event.target.files;
    if (files && files[0]) {
      const file = files[0];
      const validTypes = ['image/jpeg', 'image/png', 'image/jpg'];
      const isValidType = validTypes.includes(file.type);
      const isValidSize = file.size <= 64 * 1024 * 1024; // 64 MB

      if (!isValidType) {
        toast.error(t('validation.profilePictureInvalidFileType'));
        return;
      }

      if (!isValidSize) {
        toast.error(t('validation.profilePictureFileTooLarge'));
        return;
      }

      const objectUrl = URL.createObjectURL(file);
      setPreview(objectUrl);

      try {
        const result = await updatePicture({ profilePicture: file }).unwrap();
        if (result.success) {
          toast.success(t('messages.pictureUpdatedSuccessfully'));
          onUpdateSuccess();
        } else {
          toast.error(
            t('messages.failedToUpdatePicture', {
              errorMessage:
                result.messages && result.messages.length > 0
                  ? `: ${result.messages[0]}`
                  : '',
            }),
          );
        }
      } catch (error) {
        console.error('Failed to update profile picture:', error);
        toast.error(t('failedToUpdatePicture', { errorMessage: '' }));
      } finally {
        URL.revokeObjectURL(objectUrl);
      }
    }
  };

  return (
    <>
      <Card sx={{ p: 3, boxShadow: 3, borderRadius: 2 }}>
        <Grid container spacing={2} alignItems="center">
          <Grid item>
            <Box sx={{ position: 'relative' }}>
              <Avatar
                src={preview || ''}
                alt={user.username}
                sx={{ width: 120, height: 120 }}
              />
              <label htmlFor="profile-picture-upload">
                <input
                  accept="image/*"
                  id="profile-picture-upload"
                  type="file"
                  hidden
                  onChange={handleProfilePictureChange}
                />
                <Tooltip title={t('uploadPicture')}>
                  <IconButton
                    color="primary"
                    component="span"
                    sx={{
                      position: 'absolute',
                      bottom: 0,
                      right: 0,
                      backgroundColor: 'background.paper',
                      border: '1px solid',
                      '&:hover': {
                        backgroundColor: 'primary.main',
                        '& .MuiSvgIcon-root': {
                          color: 'background.paper',
                        },
                      },
                    }}
                  >
                    <PhotoCamera />
                  </IconButton>
                </Tooltip>
              </label>
            </Box>
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
              {/* Pencil icon for EditProfileDialog */}
              <IconButton onClick={handleEditClick}>
                <EditIcon />
              </IconButton>
            </Box>
            <Box sx={{ mt: 1 }}>
              <Typography variant="body1" color="textSecondary">
                <strong>{t('username')}:</strong> {user.username}
              </Typography>
              <Typography variant="body1" color="textSecondary">
                <strong>{t('email')}:</strong> {user.email}
              </Typography>
              <Typography variant="body1" color="textSecondary">
                <strong>{t('career')}:</strong>{' '}
                {user.career?.name || t('notSet')}
              </Typography>
              <Typography variant="body1" color="textSecondary">
                <strong>{t('institution')}:</strong>{' '}
                {user.institution?.name || t('notSet')}
              </Typography>

              <Box sx={{ mt: 2, display: 'flex', gap: 2 }}>
                <Button onClick={handleFollowersClick}>
                  {t('followers')}
                  {': '}
                  {/* Follower count logic */}
                  {!isLoadingFollowers && (followersData?.totalCount || 0)}
                  {isLoadingFollowers && <CircularProgress size={20} />}
                </Button>
                <Button onClick={handleFollowingClick}>
                  {t('following')}
                  {': '}
                  {/* Following count logic */}
                  {!isLoadingFollowings && (followingsData?.totalCount || 0)}
                  {isLoadingFollowings && <CircularProgress size={20} />}
                </Button>
              </Box>

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
                    ? t('disableEmailNotifications')
                    : t('enableEmailNotifications')
                }
                sx={{ mt: 2 }}
              />

              {/* Button to open the ChangePasswordDialog */}
              <Box sx={{ mt: 2 }}>
                <Button variant="outlined" onClick={handleChangePasswordClick}>
                  {t('changePassword')}
                </Button>
              </Box>
            </Box>
          </Grid>
        </Grid>
      </Card>

      {/* Edit Profile Dialog (still using the pencil icon above) */}
      <EditProfileDialog
        open={editModalOpen}
        handleClose={handleEditModalClose}
        user={user}
        onUpdateSuccess={onUpdateSuccess}
      />

      {/* Change Password Dialog */}
      <ChangePasswordDialog
        open={changePasswordModalOpen}
        handleClose={handleChangePasswordModalClose}
        user={user}
        onUpdateSuccess={onUpdateSuccess}
      />

      <FollowersModal
        open={followersModalOpen}
        handleClose={handleFollowersModalClose}
        userId={user.id}
        followingUrl={user.followingUrl}
      />

      <FollowingModal
        open={followingModalOpen}
        handleClose={handleFollowingModalClose}
        userId={user.id}
        followedByUrl={user.followedByUrl}
      />
    </>
  );
};

export default ProfileCard;
