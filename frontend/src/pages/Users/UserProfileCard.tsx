// src/pages/User/UserProfileCard.tsx

import {
  Card,
  Typography,
  Avatar,
  Box,
  Button,
  CircularProgress,
} from '@mui/material';
import { useTranslation } from 'react-i18next';
import { User } from '../../types';
import {
  useFollowUserMutation,
  useUnfollowUserMutation,
  useIsFollowingUserQuery,
} from '../../store/slices/usersApiSlice';
import { useSelector } from 'react-redux';
import { selectCurrentUserId } from '../../store/slices/authSlice';
import { toast } from 'react-toastify';

interface UserProfileCardProps {
  user: User;
}

const UserProfileCard: React.FC<UserProfileCardProps> = ({ user }) => {
  const { t } = useTranslation();
  const currentUserId = useSelector(selectCurrentUserId);

  const [followUser, { isLoading: isFollowingLoading }] =
    useFollowUserMutation();
  const [unfollowUser, { isLoading: isUnfollowingLoading }] =
    useUnfollowUserMutation();

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
      if (result) {
        toast.success(t('userProfileCard.followed'));
        refetch();
      } else {
        toast.error(t('userProfileCard.followFailed'));
      }
    } catch (error) {
      toast.error(t('userProfileCard.followFailed'));
    }
  };

  const handleUnfollow = async () => {
    try {
      const result = await unfollowUser({
        userId: user.id,
        followerId: currentUserId!,
      }).unwrap();
      if (result) {
        toast.success(t('userProfileCard.unfollowed'));
        refetch();
      } else {
        toast.error(t('userProfileCard.unfollowFailed'));
      }
    } catch (error) {
      toast.error(t('userProfileCard.unfollowFailed'));
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
    <Card sx={{ p: 3, mb: 4, boxShadow: 3, borderRadius: 2 }}>
      <Box sx={{ display: 'flex', alignItems: 'center' }}>
        <Avatar
          src={user.profilePictureUrl || ''}
          alt={user.username}
          sx={{ width: 100, height: 100, mr: 3 }}
        />
        <Box>
          <Typography variant="h5">
            {user.firstName} {user.lastName}
          </Typography>
          <Typography variant="body1" color="textSecondary">
            @{user.username}
          </Typography>
          <Typography variant="body1" color="textSecondary">
            {user.email}
          </Typography>
          <Typography variant="body1" color="textSecondary">
            {user.career?.name || t('userProfileCard.noCareer')}
          </Typography>
          <Typography variant="body1" color="textSecondary">
            {user.institution?.name || t('userProfileCard.noInstitution')}
          </Typography>
        </Box>
        <Box sx={{ marginLeft: 'auto' }}>
          {isFollowing ? (
            <Button
              variant="outlined"
              color="secondary"
              onClick={handleUnfollow}
              disabled={isUnfollowingLoading}
            >
              {isUnfollowingLoading ? (
                <CircularProgress size={24} />
              ) : (
                t('userProfileCard.unfollow')
              )}
            </Button>
          ) : (
            <Button
              variant="contained"
              color="primary"
              onClick={handleFollow}
              disabled={isFollowingLoading}
            >
              {isFollowingLoading ? (
                <CircularProgress size={24} />
              ) : (
                t('userProfileCard.follow')
              )}
            </Button>
          )}
        </Box>
      </Box>
    </Card>
  );
};

export default UserProfileCard;
