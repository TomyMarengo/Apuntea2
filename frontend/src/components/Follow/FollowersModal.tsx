import {
  Dialog,
  DialogTitle,
  DialogContent,
  List,
  CircularProgress,
  Box,
} from '@mui/material';
import React, { useState, useEffect, useCallback } from 'react';
import { useTranslation } from 'react-i18next';
import InfiniteScroll from 'react-infinite-scroll-component';
import { toast } from 'react-toastify';

import FollowItem from './FollowItem';
import { useLazyGetFollowersQuery } from '../../store/slices/usersApiSlice';
import { User } from '../../types';

interface FollowersModalProps {
  open: boolean;
  handleClose: () => void;
  userId: string;
  followingUrl: string;
  onChange: () => void;
}

const FollowersModal: React.FC<FollowersModalProps> = ({
  open,
  handleClose,
  userId,
  followingUrl,
  onChange,
}) => {
  const { t } = useTranslation('followersModal');
  const [page, setPage] = useState(1);
  const [followers, setFollowers] = useState<User[]>([]);
  const [hasMore, setHasMore] = useState(true);
  const [isFetchingMore, setIsFetchingMore] = useState(false);
  const pageSize = 10;

  const [triggerGetFollowers, { data, error, isLoading, isFetching, reset }] =
    useLazyGetFollowersQuery();

  // Fetch followers when modal opens
  useEffect(() => {
    if (open) {
      setPage(1);
      setFollowers([]);
      setHasMore(true);
      setIsFetchingMore(false);
      reset();
      triggerGetFollowers({ url: followingUrl, page: 1, pageSize });
      console.log('triggerGetFollowers');
    }
  }, [open]);

  // Handle errors
  useEffect(() => {
    if (error) {
      toast.error(t('errorLoadingFollowers'));
    }
  }, [error, t]);

  // Accumulate followers when new data is received
  useEffect(() => {
    if (!isLoading) {
      const newFollowers =
        data?.users.filter(
          (newFollower: User) =>
            !followers.some((f) => f.id === newFollower.id),
        ) || [];

      setFollowers((prev) => [...prev, ...newFollowers]);

      if (data && page >= data.totalPages) {
        setHasMore(false);
      }

      setIsFetchingMore(false);
    }
  }, [data]);

  const fetchMoreData = useCallback(() => {
    if (!isFetchingMore && hasMore && !isLoading && !isFetching) {
      setIsFetchingMore(true);
      setPage((prev) => {
        const nextPage = prev + 1;
        triggerGetFollowers({ url: followingUrl, page: nextPage, pageSize });
        return nextPage;
      });
    }
  }, [
    isFetchingMore,
    hasMore,
    isLoading,
    isFetching,
    followingUrl,
    pageSize,
    triggerGetFollowers,
  ]);

  const handleChange = () => {
    triggerGetFollowers({ url: followingUrl, page: 1, pageSize });
    onChange();
  };

  return (
    <Dialog open={open} onClose={handleClose} fullWidth maxWidth="sm">
      <DialogTitle>
        {t('followers')}: {data?.totalCount || 0}
      </DialogTitle>
      <DialogContent
        id="scrollableDiv"
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
            dataLength={followers.length}
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
            scrollableTarget="scrollableDiv"
            endMessage={
              followers.length > 0 && (
                <Box
                  display="flex"
                  justifyContent="center"
                  alignItems="center"
                  padding={2}
                >
                  {t('noMoreFollowers')}
                </Box>
              )
            }
          >
            <List>
              {followers.map((follower) => (
                <FollowItem
                  key={follower.id}
                  followId={follower.id}
                  currentUserId={userId}
                  onChange={handleChange}
                />
              ))}
            </List>
          </InfiniteScroll>
        )}
        {!isLoading && followers.length === 0 && !error && (
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
