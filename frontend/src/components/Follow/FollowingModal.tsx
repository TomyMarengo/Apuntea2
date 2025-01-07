// src/components/Follow/FollowingModal.tsx

import RefreshIcon from '@mui/icons-material/Refresh';
import {
  Dialog,
  DialogTitle,
  DialogContent,
  List,
  CircularProgress,
  Box,
  IconButton,
} from '@mui/material';
import React, { useState, useEffect, useCallback } from 'react';
import { useTranslation } from 'react-i18next';
import InfiniteScroll from 'react-infinite-scroll-component';
import { toast } from 'react-toastify';

import FollowItem from './FollowItem';
import { useLazyGetFollowingsQuery } from '../../store/slices/usersApiSlice';
import { User } from '../../types';

interface FollowingModalProps {
  open: boolean;
  handleClose: () => void;
  userId: string;
  followedByUrl: string;
  onChange: () => void;
}

const FollowingModal: React.FC<FollowingModalProps> = ({
  open,
  handleClose,
  userId,
  followedByUrl,
  onChange,
}) => {
  const { t } = useTranslation('followingModal');
  const [page, setPage] = useState(1);
  const [followings, setFollowings] = useState<User[]>([]);
  const [hasMore, setHasMore] = useState(true);
  const [isFetchingMore, setIsFetchingMore] = useState(false);
  const pageSize = 10;

  const [triggerGetFollowings, { data, error, isLoading, isFetching, reset }] =
    useLazyGetFollowingsQuery();

  // Fetch followings when modal opens
  useEffect(() => {
    if (open) {
      setPage(1);
      setFollowings([]);
      setHasMore(true);
      setIsFetchingMore(false);
      reset();
      triggerGetFollowings({ url: followedByUrl, page: 1, pageSize });
    }
  }, [open]);

  // Handle errors
  useEffect(() => {
    if (error) {
      toast.error(t('errorLoadingFollowings'));
    }
  }, [error, t]);

  // Accumulate followings when new data is received
  useEffect(() => {
    if (!isLoading) {
      const newFollowings =
        data?.users.filter(
          (newFollowing: User) =>
            !followings.some((f) => f.id === newFollowing.id),
        ) || [];

      setFollowings((prev) => [...prev, ...newFollowings]);

      if (data && page >= data.totalPages) {
        setHasMore(false);
      }

      setIsFetchingMore(false);
    }
  }, [data, isLoading]);

  const handleRefresh = () => {
    setPage(1);
    setFollowings([]);
    setHasMore(true);
    setIsFetchingMore(false);
    reset();
    triggerGetFollowings({ url: followedByUrl, page: 1, pageSize });
  };

  // Function to load more data
  const fetchMoreData = useCallback(() => {
    if (!isFetchingMore && hasMore && !isLoading && !isFetching) {
      setIsFetchingMore(true);
      setPage((prev) => {
        const nextPage = prev + 1;
        triggerGetFollowings({ url: followedByUrl, page: nextPage, pageSize });
        return nextPage;
      });
    }
  }, [
    isFetchingMore,
    hasMore,
    isLoading,
    isFetching,
    followedByUrl,
    pageSize,
    triggerGetFollowings,
  ]);

  const handleChange = () => {
    triggerGetFollowings({ url: followedByUrl, page: 1, pageSize });
    onChange();
  };

  return (
    <Dialog open={open} onClose={handleClose} fullWidth maxWidth="sm">
      <DialogTitle
        sx={{
          display: 'flex',
          justifyContent: 'space-between',
          alignItems: 'center',
        }}
      >
        {t('followings')}: {data?.totalCount || 0}
        <IconButton onClick={handleRefresh} aria-label={t('refresh')}>
          <RefreshIcon />
        </IconButton>
      </DialogTitle>
      <DialogContent
        id="scrollableDivFollowing"
        style={{ height: '60vh', overflow: 'auto', padding: 0 }}
      >
        {isLoading && page === 1 ? (
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
            dataLength={followings.length}
            next={fetchMoreData}
            hasMore={hasMore}
            loader={
              <Box
                display="flex"
                justifyContent="center"
                alignItems="center"
                padding={2}
              >
                <CircularProgress size={24} />
              </Box>
            }
            scrollableTarget="scrollableDivFollowing"
            endMessage={
              followings.length > 0 && (
                <Box
                  display="flex"
                  justifyContent="center"
                  alignItems="center"
                  padding={2}
                >
                  {t('noMoreFollowings')}
                </Box>
              )
            }
          >
            <List>
              {followings.map((following) => (
                <FollowItem
                  key={following.id}
                  followId={following.id}
                  currentUserId={userId}
                  onChange={handleChange}
                />
              ))}
            </List>
          </InfiniteScroll>
        )}
        {!isLoading && followings.length === 0 && !error && (
          <Box
            display="flex"
            justifyContent="center"
            alignItems="center"
            padding={2}
          >
            {t('noFollowings')}
          </Box>
        )}
      </DialogContent>
    </Dialog>
  );
};

export default FollowingModal;
