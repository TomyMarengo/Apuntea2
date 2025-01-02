// src/components/Follow/FollowersModal.tsx

import {
  Dialog,
  DialogTitle,
  DialogContent,
  List,
  CircularProgress,
  Box,
} from '@mui/material';
import React, { useState, useEffect } from 'react';
import { useTranslation } from 'react-i18next';
import InfiniteScroll from 'react-infinite-scroll-component';
import { toast } from 'react-toastify';

import FollowItem from './FollowItem';
import { useGetFollowersQuery } from '../../store/slices/usersApiSlice';

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
  const { t } = useTranslation('followersModal');
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
      toast.error(t('errorLoadingFollowers'));
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
      <DialogTitle>
        {t('followers')}: {data?.totalCount}
      </DialogTitle>
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
                <FollowItem
                  key={follower.id}
                  followId={follower.id}
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
            {t('noFollowers')}
          </Box>
        )}
      </DialogContent>
    </Dialog>
  );
};

export default FollowersModal;
