// src/pages/Profile/ProfileCard.tsx

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
  Button,
  CircularProgress,
} from '@mui/material';
import { Edit as EditIcon } from '@mui/icons-material';
import { useTranslation } from 'react-i18next';
import { toast } from 'react-toastify';
import EditProfileDialog from './EditProfileDialog';
import { User } from '../../types';
import {
  useUpdateUserMutation,
  useGetFollowersQuery,
  useGetFollowingsQuery,
} from '../../store/slices/usersApiSlice';
import FollowersModal from '../../components/Follow/FollowersModal';
import FollowingModal from '../../components/Follow/FollowingModal';

interface ProfileCardProps {
  user: User;
  onUpdateSuccess: () => void;
}

const ProfileCard: React.FC<ProfileCardProps> = ({ user, onUpdateSuccess }) => {
  const { t } = useTranslation();
  const [editModalOpen, setEditModalOpen] = useState(false);
  const [followersModalOpen, setFollowersModalOpen] = useState(false);
  const [followingModalOpen, setFollowingModalOpen] = useState(false);
  const [updateUser] = useUpdateUserMutation();
  const [notificationsEnabled, setNotificationsEnabled] = useState(
    user.notificationsEnabled,
  );

  const handleEditClick = () => setEditModalOpen(true);
  const handleEditModalClose = () => setEditModalOpen(false);

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

  const handleFollowersClick = () => {
    setFollowersModalOpen(true);
  };

  const handleFollowersModalClose = () => {
    setFollowersModalOpen(false);
  };

  // Handlers para FollowingModal
  const handleFollowingClick = () => {
    setFollowingModalOpen(true);
  };

  const handleFollowingModalClose = () => {
    setFollowingModalOpen(false);
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
              <IconButton onClick={handleEditClick}>
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
              <Box sx={{ mt: 2, display: 'flex', gap: 2 }}>
                <Button onClick={handleFollowersClick}>
                  {t('profileCard.followers')} {': '}
                  {!isLoadingFollowers && (followersData?.totalCount || 0)}
                  {isLoadingFollowers && <CircularProgress size={20} />}
                </Button>
                <Button onClick={handleFollowingClick}>
                  {t('profileCard.following')} {': '}
                  {!isLoadingFollowings && (followingsData?.totalCount || 0)}
                  {isLoadingFollowings && <CircularProgress size={20} />}
                </Button>
              </Box>
            </Box>
          </Grid>
        </Grid>
      </Card>

      <EditProfileDialog
        open={editModalOpen}
        handleClose={handleEditModalClose}
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
