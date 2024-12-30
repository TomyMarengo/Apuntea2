// src/components/Follow/FollowingItem.tsx

import React, { useState } from 'react';
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
  useUnfollowUserMutation,
} from '../../store/slices/usersApiSlice';
import { toast } from 'react-toastify';
import { useTranslation } from 'react-i18next';

interface FollowingItemProps {
  followingId: string;
  currentUserId: string;
}

const FollowingItem: React.FC<FollowingItemProps> = ({
  followingId,
  currentUserId,
}) => {
  const { t } = useTranslation();
  const [isFollowing, setIsFollowing] = useState(true);

  const {
    data: followingUser,
    error: userError,
    isLoading: userLoading,
  } = useGetUserQuery({ userId: followingId });

  const [unfollowUser] = useUnfollowUserMutation();

  const handleUnfollow = async () => {
    try {
      const result = await unfollowUser({
        userId: followingId,
        followerId: currentUserId,
      }).unwrap();
      if (result) {
        toast.success(t('follow.successUnfollow'));
        setIsFollowing(false);
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

  if (userError || !followingUser) {
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
          src={followingUser.profilePictureUrl}
          alt={followingUser.username}
        />
      </ListItemAvatar>
      <ListItemText
        primary={`${followingUser.firstName || ''} ${followingUser.lastName || followingUser.username}`}
      />
      <Box>
        {isFollowing && (
          <Button variant="outlined" color="primary" onClick={handleUnfollow}>
            {t('follow.unfollow')}
          </Button>
        )}
      </Box>
    </ListItem>
  );
};

export default FollowingItem;
