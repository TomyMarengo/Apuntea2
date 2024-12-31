// src/components/Follow/FollowItem.tsx

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

interface FollowItemProps {
  followId: string;
  currentUserId: string;
}

const FollowItem: React.FC<FollowItemProps> = ({ followId, currentUserId }) => {
  const { t } = useTranslation('followItem');

  const {
    data: followUserData,
    error: userError,
    isLoading: userLoading,
  } = useGetUserQuery({ userId: followId });

  const { data: isFollowing, refetch } = useIsFollowingUserQuery(
    { userId: followId, followerId: currentUserId },
    { skip: !followId || !currentUserId },
  );

  const [followUser] = useFollowUserMutation();
  const [unfollowUser] = useUnfollowUserMutation();

  const handleFollow = async () => {
    try {
      const result = await followUser({
        userId: followId,
      }).unwrap();
      if (result) {
        toast.success(t('successFollow'));
        refetch();
      } else {
        toast.error(t('errorFollow'));
      }
    } catch (error) {
      toast.error(t('errorFollow'));
    }
  };

  const handleUnfollow = async () => {
    try {
      const result = await unfollowUser({
        userId: followId,
        followerId: currentUserId,
      }).unwrap();
      if (result) {
        toast.success(t('successUnfollow'));
        refetch();
      } else {
        toast.error(t('errorUnfollow'));
      }
    } catch (error) {
      toast.error(t('errorUnfollow'));
    }
  };

  if (userLoading) {
    return (
      <ListItem>
        <ListItemAvatar>
          <Avatar />
        </ListItemAvatar>
        <ListItemText primary={t('loading')} />
      </ListItem>
    );
  }

  if (userError || !followUserData) {
    return (
      <ListItem>
        <ListItemText primary={t('errorLoadingUser')} />
      </ListItem>
    );
  }

  return (
    <ListItem>
      <ListItemAvatar>
        <Avatar
          src={followUserData.profilePictureUrl}
          alt={followUserData.username}
        />
      </ListItemAvatar>
      <ListItemText
        primary={`${followUserData.firstName || ''} ${
          followUserData.lastName || followUserData.username
        }`}
      />
      <Box>
        {isFollowing ? (
          <Button variant="outlined" color="primary" onClick={handleUnfollow}>
            {t('unfollow')}
          </Button>
        ) : (
          <Button variant="contained" color="primary" onClick={handleFollow}>
            {t('follow')}
          </Button>
        )}
      </Box>
    </ListItem>
  );
};

export default FollowItem;
