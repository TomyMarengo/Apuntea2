// src/components/Follow/FollowerItem.tsx

import React from 'react';
import {
  ListItem,
  ListItemAvatar,
  Avatar,
  ListItemText,
  Button,
  Box,
} from '@mui/material';
import {
  useGetUserQuery,
  useIsFollowingUserQuery,
  useFollowUserMutation,
  useUnfollowUserMutation,
} from '../../store/slices/usersApiSlice';
import { toast } from 'react-toastify';
import { useTranslation } from 'react-i18next';

interface FollowerItemProps {
  followerId: string;
  currentUserId: string;
}

const FollowerItem: React.FC<FollowerItemProps> = ({
  followerId,
  currentUserId,
}) => {
  const { t } = useTranslation();

  const {
    data: followerUser,
    error: userError,
    isLoading: userLoading,
  } = useGetUserQuery({ userId: followerId });

  const { data: isFollowing, refetch } = useIsFollowingUserQuery(
    { userId: followerId, followerId: currentUserId },
    { skip: !followerId || !currentUserId },
  );

  const [followUser] = useFollowUserMutation();
  const [unfollowUser] = useUnfollowUserMutation();

  const handleFollow = async () => {
    try {
      const result = await followUser({
        userId: followerId,
      }).unwrap();
      if (result) {
        toast.success(t('follow.successFollow'));
        refetch();
      } else {
        toast.error(t('follow.errorFollow'));
      }
    } catch (error) {
      toast.error(t('follow.errorFollow'));
    }
  };

  const handleUnfollow = async () => {
    try {
      const result = await unfollowUser({
        userId: followerId,
        followerId: currentUserId,
      }).unwrap();
      if (result) {
        toast.success(t('follow.successUnfollow'));
        refetch();
      } else {
        toast.error(t('follow.errorUnfollow'));
      }
    } catch (error) {
      toast.error(t('follow.errorUnfollow'));
    }
  };

  if (userLoading) {
    return (
      <ListItem>
        <ListItemAvatar>
          <Avatar />
        </ListItemAvatar>
        <ListItemText primary={t('follow.loading')} />
      </ListItem>
    );
  }

  if (userError || !followerUser) {
    return (
      <ListItem>
        <ListItemText primary={t('follow.errorLoadingUser')} />
      </ListItem>
    );
  }

  return (
    <ListItem>
      <ListItemAvatar>
        <Avatar
          src={followerUser.profilePictureUrl}
          alt={followerUser.username}
        />
      </ListItemAvatar>
      <ListItemText
        primary={`${followerUser.firstName || ''} ${followerUser.lastName || followerUser.username}`}
      />
      <Box>
        {isFollowing ? (
          <Button variant="outlined" color="primary" onClick={handleUnfollow}>
            {t('follow.unfollow')}
          </Button>
        ) : (
          <Button variant="contained" color="primary" onClick={handleFollow}>
            {t('follow.follow')}
          </Button>
        )}
      </Box>
    </ListItem>
  );
};

export default FollowerItem;
