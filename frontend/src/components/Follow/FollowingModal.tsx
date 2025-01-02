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
import { useGetFollowingsQuery } from '../../store/slices/usersApiSlice';
import { User } from '../../types';

interface FollowingModalProps {
  open: boolean;
  handleClose: () => void;
  userId: string;
  followedByUrl: string;
}

const FollowingModal: React.FC<FollowingModalProps> = ({
  open,
  handleClose,
  userId,
  followedByUrl,
}) => {
  const { t } = useTranslation('followingModal');
  const [page, setPage] = useState(1);
  const [followings, setFollowings] = useState<User[]>([]);
  const [hasMore, setHasMore] = useState(true);
  const [isFetchingMore, setIsFetchingMore] = useState(false);
  const pageSize = 10;

  // Fetch followers based on the current page
  const { data, error, isLoading, isFetching, refetch } = useGetFollowingsQuery(
    { url: followedByUrl, page, pageSize },
    {
      skip: !open,
      refetchOnMountOrArgChange: true,
    },
  );

  // Handle errors
  useEffect(() => {
    if (error) {
      toast.error(t('errorLoadingFollowings'));
    }
  }, [error, t]);

  // Reset state when the modal opens
  useEffect(() => {
    if (open) {
      setPage(1);
      setFollowings([]);
      setHasMore(true);
      setIsFetchingMore(false);
    }
  }, [open]);

  // Accumulate followers when new data is received
  useEffect(() => {
    if (data) {
      // Filter duplicate followers
      const newFollowers = data.users.filter(
        (newFollower: User) => !followings.some((f) => f.id === newFollower.id),
      );

      setFollowings((prev) => [...prev, ...newFollowers]);

      // Determine if there are more pages to load
      if (page >= data.totalPages) {
        setHasMore(false);
      }

      // Finish loading
      setIsFetchingMore(false);
    }
  }, [data, page, followings]);

  // Function to load more data
  const fetchMoreData = useCallback(() => {
    if (!isFetchingMore && hasMore && !isLoading && !isFetching) {
      setIsFetchingMore(true);
      setPage((prev) => prev + 1);
    }
  }, [isFetchingMore, hasMore, isLoading, isFetching]);

  const handleRefresh = () => {
    setPage(1);
    setFollowings([]);
    setHasMore(true);
    setIsFetchingMore(false);
    refetch();
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
