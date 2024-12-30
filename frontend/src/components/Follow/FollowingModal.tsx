// src/components/Follow/FollowingModal.tsx

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
import { useGetFollowingsQuery } from '../../store/slices/usersApiSlice';
import FollowingItem from './FollowingItem';
import { toast } from 'react-toastify';
import { useTranslation } from 'react-i18next';

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
  const { t } = useTranslation();
  const [page, setPage] = useState(1);
  const pageSize = 10;

  const { data, error, isLoading, refetch } = useGetFollowingsQuery(
    { url: followedByUrl, page, pageSize },
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
  useEffect(() => {
    if (error) {
      toast.error(t('follow.errorLoadingFollowings'));
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
      <DialogTitle>{t('follow.following')}</DialogTitle>
      <DialogContent
        id="scrollableDivFollowing"
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
            scrollableTarget="scrollableDivFollowing"
          >
            <List>
              {data?.users.map((following) => (
                <FollowingItem
                  key={following.id}
                  followingId={following.id}
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
            {t('follow.noFollowing')}
          </Box>
        )}
      </DialogContent>
    </Dialog>
  );
};

export default FollowingModal;
