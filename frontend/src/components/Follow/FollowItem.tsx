// src/components/Follow/FollowItem.tsx

import {
  ListItem,
  ListItemAvatar,
  Avatar,
  ListItemText,
  Button,
  Box,
  Link as MuiLink,
} from '@mui/material';
import React from 'react';
import { useTranslation } from 'react-i18next';
import { Link } from 'react-router-dom';
import { toast } from 'react-toastify';

import {
  useIsFollowingUserQuery,
  useFollowUserMutation,
  useUnfollowUserMutation,
} from '../../store/slices/usersApiSlice';
import { User } from '../../types';

interface FollowItemProps {
  // followId: string;
  targetUser: User;
  currentUserId: string;
  onChange: () => void;
}

const FollowItem: React.FC<FollowItemProps> = ({
  // followId,
  targetUser,
  currentUserId,
  onChange,
}) => {
  const { t } = useTranslation('followItem');

  const { data: isFollowing, refetch } = useIsFollowingUserQuery(
    { userId: targetUser.id, followerId: currentUserId },
    { skip: !targetUser || !currentUserId },
  );

  const [followUser] = useFollowUserMutation();
  const [unfollowUser] = useUnfollowUserMutation();

  const handleFollow = async () => {
    try {
      const result = await followUser({
        userId: targetUser.id,
        url: targetUser.followersUrl,
      }).unwrap();
      if (result.success) {
        toast.success(t('successFollow', { username: targetUser?.username }));
        refetch();
        onChange();
      } else {
        toast.error(t('errorFollow', { username: targetUser?.username }));
      }
    } catch {
      toast.error(t('errorFollow', { username: targetUser?.username }));
    }
  };

  const handleUnfollow = async () => {
    try {
      const result = await unfollowUser({
        userId: targetUser.id,
        followerId: currentUserId,
      }).unwrap();
      if (result.success) {
        toast.success(t('successUnfollow', { username: targetUser?.username }));
        refetch();
        onChange();
      } else {
        toast.error(t('errorUnfollow', { username: targetUser?.username }));
      }
    } catch {
      toast.error(t('errorUnfollow', { username: targetUser?.username }));
    }
  };

  return (
    <ListItem>
      <ListItemAvatar>
        <Avatar src={targetUser.profilePictureUrl} alt={targetUser.username} />
      </ListItemAvatar>
      <ListItemText
        primary={
          <MuiLink
            component={Link}
            to={`/users/${targetUser.id}`}
            underline="hover"
          >
            {`${targetUser.firstName || ''} ${
              targetUser.lastName || targetUser.username
            }`}
          </MuiLink>
        }
      />
      {currentUserId && targetUser.id !== currentUserId && (
        <Box>
          {isFollowing?.success ? (
            <Button variant="outlined" color="primary" onClick={handleUnfollow}>
              {t('unfollow')}
            </Button>
          ) : (
            <Button variant="contained" color="primary" onClick={handleFollow}>
              {t('follow')}
            </Button>
          )}
        </Box>
      )}
    </ListItem>
  );
};

export default FollowItem;
