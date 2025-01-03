// src/pages/Users/UserProfileCard.tsx

import {
  Card,
  Typography,
  Avatar,
  Box,
  Button,
  CircularProgress,
  Grid,
} from '@mui/material';
import React, { useState } from 'react';
import { useTranslation } from 'react-i18next';
import { useSelector } from 'react-redux';
import { toast } from 'react-toastify';

import FollowersModal from '../../components/Follow/FollowersModal';
import FollowingModal from '../../components/Follow/FollowingModal';
import { selectCurrentUserId } from '../../store/slices/authSlice';
import {
  useFollowUserMutation,
  useUnfollowUserMutation,
  useIsFollowingUserQuery,
  useGetFollowersQuery,
  useGetFollowingsQuery,
} from '../../store/slices/usersApiSlice';
import { User } from '../../types';

interface UserProfileCardProps {
  user: User;
}

const UserProfileCard: React.FC<UserProfileCardProps> = ({ user }) => {
  const { t } = useTranslation('userProfileCard');
  const currentUserId = useSelector(selectCurrentUserId);

  const [followUser, { isLoading: isFollowingLoading }] =
    useFollowUserMutation();
  const [unfollowUser, { isLoading: isUnfollowingLoading }] =
    useUnfollowUserMutation();

  const [followersModalOpen, setFollowersModalOpen] = useState(false);
  const [followingModalOpen, setFollowingModalOpen] = useState(false);

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

  const handleFollowingClick = () => {
    setFollowingModalOpen(true);
  };

  const handleFollowingModalClose = () => {
    setFollowingModalOpen(false);
  };

  const {
    data: isFollowing,
    isLoading: isCheckLoading,
    refetch,
  } = useIsFollowingUserQuery(
    {
      userId: user.id,
      followerId: currentUserId,
    },
    { skip: !currentUserId },
  );

  const handleFollow = async () => {
    try {
      const result = await followUser({
        userId: user.id,
      }).unwrap();
      if (result.success) {
        toast.success(t('followed'));
        refetch();
      } else {
        toast.error(t('followFailed'));
      }
    } catch (error) {
      console.error('Failed to follow user:', error);
      toast.error(t('followFailed'));
    }
  };

  const handleUnfollow = async () => {
    try {
      const result = await unfollowUser({
        userId: user.id,
        followerId: currentUserId!,
      }).unwrap();
      if (result.success) {
        toast.success(t('unfollowed'));
        refetch();
      } else {
        toast.error(t('unfollowFailed'));
      }
    } catch (error) {
      console.error('Failed to unfollow user:', error);
      toast.error(t('unfollowFailed'));
    }
  };

  if (isCheckLoading) {
    return (
      <Box sx={{ display: 'flex', justifyContent: 'center', my: 2 }}>
        <CircularProgress />
      </Box>
    );
  }

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
              <Box sx={{ marginLeft: 'auto' }}>
                {isFollowing?.success ? (
                  <Button
                    variant="outlined"
                    onClick={handleUnfollow}
                    disabled={isUnfollowingLoading}
                  >
                    {isUnfollowingLoading ? (
                      <CircularProgress size={24} />
                    ) : (
                      t('unfollow')
                    )}
                  </Button>
                ) : (
                  <Button
                    variant="contained"
                    onClick={handleFollow}
                    disabled={isFollowingLoading}
                  >
                    {isFollowingLoading ? (
                      <CircularProgress size={24} />
                    ) : (
                      t('follow')
                    )}
                  </Button>
                )}
              </Box>
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
                  {!isLoadingFollowers && (followersData?.totalCount || 0)}
                  {isLoadingFollowers && <CircularProgress size={20} />}
                </Button>
                <Button onClick={handleFollowingClick}>
                  {t('following')}
                  {': '}
                  {!isLoadingFollowings && (followingsData?.totalCount || 0)}
                  {isLoadingFollowings && <CircularProgress size={20} />}
                </Button>
              </Box>
            </Box>
          </Grid>
        </Grid>
      </Card>

      <FollowersModal
        open={followersModalOpen}
        handleClose={handleFollowersModalClose}
        userId={currentUserId}
        followingUrl={user.followingUrl}
      />

      <FollowingModal
        open={followingModalOpen}
        handleClose={handleFollowingModalClose}
        userId={currentUserId}
        followedByUrl={user.followedByUrl}
      />
    </>
  );
};

export default UserProfileCard;
