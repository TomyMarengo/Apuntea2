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
import React, { useState, useEffect } from 'react';
import { useTranslation } from 'react-i18next';
import InfiniteScroll from 'react-infinite-scroll-component';
import { toast } from 'react-toastify';

import FollowItem from './FollowItem';
import { useGetFollowingsQuery } from '../../store/slices/usersApiSlice';

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
      toast.error(t('errorLoadingFollowings'));
    }
  }, [error, t]);

  // Reset page when modal is reopened
  useEffect(() => {
    if (open) {
      setPage(1);
    }
  }, [open]);

  // Handle manual refresh
  const handleRefresh = () => {
    setPage(1);
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
        {t('following')}: {data?.totalCount}
        <IconButton onClick={handleRefresh} aria-label={t('refresh')}>
          <RefreshIcon />
        </IconButton>
      </DialogTitle>
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
                <FollowItem
                  key={following.id}
                  followId={following.id}
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
            {t('noFollowing')}
          </Box>
        )}
      </DialogContent>
    </Dialog>
  );
};

export default FollowingModal;
