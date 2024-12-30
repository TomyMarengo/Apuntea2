// src/components/Follow/FollowersModal.tsx

import React, { useState, useEffect } from 'react';
import {
  Dialog,
  DialogTitle,
  DialogContent,
  List,
  CircularProgress,
  Box,
} from '@mui/material';
import InfiniteScroll from 'react-infinite-scroll-component';
import { useGetFollowersQuery } from '../../store/slices/usersApiSlice';
import FollowerItem from './FollowerItem';
import { toast } from 'react-toastify';
import { useTranslation } from 'react-i18next';

interface FollowersModalProps {
  open: boolean;
  handleClose: () => void;
  userId: string;
  followingUrl: string;
}

const FollowersModal: React.FC<FollowersModalProps> = ({
  open,
  handleClose,
  userId,
  followingUrl,
}) => {
  const { t } = useTranslation();
  const [page, setPage] = useState(1);
  const pageSize = 10;

  const { data, error, isLoading } = useGetFollowersQuery(
    { url: followingUrl, page, pageSize },
    {
      skip: !open,
      refetchOnMountOrArgChange: true,
    },
  );

  const fetchMoreData = () => {
    if (data && page < data.totalPages) {
      setPage((prev) => prev + 1);
    }
  };

  // Handle errors
  React.useEffect(() => {
    if (error) {
      toast.error(t('follow.errorLoadingFollowers'));
    }
  }, [error, t]);

  // Reset page when modal is reopened
  useEffect(() => {
    if (open) {
      setPage(1);
    }
  }, [open]);

  return (
    <Dialog open={open} onClose={handleClose} fullWidth maxWidth="sm">
      <DialogTitle>{t('follow.followers')}</DialogTitle>
      <DialogContent
        id="scrollableDiv"
        style={{ height: '60vh', overflow: 'auto', padding: 0 }}
      >
        {isLoading ? (
          <Box
            display="flex"
            justifyContent="center"
            alignItems="center"
            height="100%"
          >
            <CircularProgress />
          </Box>
        ) : (
          <InfiniteScroll
            dataLength={data?.users.length || 0}
            next={fetchMoreData}
            hasMore={data ? page < data.totalPages : false}
            loader={
              <Box
                display="flex"
                justifyContent="center"
                alignItems="center"
                padding={2}
              >
                <CircularProgress />
              </Box>
            }
            scrollableTarget="scrollableDiv"
          >
            <List>
              {data?.users.map((follower) => (
                <FollowerItem
                  key={follower.id}
                  followerId={follower.id}
                  currentUserId={userId}
                />
              ))}
            </List>
          </InfiniteScroll>
        )}
        {data && data.users.length === 0 && (
          <Box
            display="flex"
            justifyContent="center"
            alignItems="center"
            padding={2}
          >
            {t('follow.noFollowers')}
          </Box>
        )}
      </DialogContent>
    </Dialog>
  );
};

export default FollowersModal;
