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
  useGetUserQuery,
  useIsFollowingUserQuery,
  useFollowUserMutation,
  useUnfollowUserMutation,
} from '../../store/slices/usersApiSlice';

interface FollowItemProps {
  followId: string;
  currentUserId: string;
  onChange: () => void;
}

const FollowItem: React.FC<FollowItemProps> = ({
  followId,
  currentUserId,
  onChange,
}) => {
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
      if (result.success) {
        toast.success(
          t('successFollow', { username: followUserData?.username }),
        );
        refetch();
        onChange();
      } else {
        toast.error(t('errorFollow', { username: followUserData?.username }));
      }
    } catch {
      toast.error(t('errorFollow', { username: followUserData?.username }));
    }
  };

  const handleUnfollow = async () => {
    try {
      const result = await unfollowUser({
        userId: followId,
        followerId: currentUserId,
      }).unwrap();
      if (result.success) {
        toast.success(
          t('successUnfollow', { username: followUserData?.username }),
        );
        refetch();
        onChange();
      } else {
        toast.error(t('errorUnfollow', { username: followUserData?.username }));
      }
    } catch {
      toast.error(t('errorUnfollow', { username: followUserData?.username }));
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
        primary={
          <MuiLink
            component={Link}
            to={`/users/${followUserData.id}`}
            underline="hover"
          >
            {`${followUserData.firstName || ''} ${
              followUserData.lastName || followUserData.username
            }`}
          </MuiLink>
        }
      />
      {followId !== currentUserId && (
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
