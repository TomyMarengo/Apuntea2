// src/pages/Reviews/ReviewsPage.tsx

import React, { useState, useEffect } from 'react';
import {
  Box,
  Typography,
  Select,
  MenuItem,
  InputLabel,
  FormControl,
  CircularProgress,
  SelectChangeEvent,
} from '@mui/material';
import { useTranslation } from 'react-i18next';
import { useSearchParams, useNavigate } from 'react-router-dom';
import { useSelector } from 'react-redux';
import { selectCurrentUser } from '../../store/slices/authSlice';
import { useGetReviewsQuery } from '../../store/slices/reviewsApiSlice';
import ReviewCard from '../../components/ReviewCard';
import PaginationBar from '../../components/PaginationBar';
import { Review } from '../../types';

const ReviewsPage: React.FC = () => {
  const { t } = useTranslation();
  const currentUser = useSelector(selectCurrentUser);
  const [searchParams, _setSearchParams] = useSearchParams();
  const navigate = useNavigate();

  // Extract URL parameters
  const page = parseInt(searchParams.get('page') || '1', 10);
  const pageSize = 10;
  const filterType = searchParams.get('filter') || 'received'; // 'received' or 'given'

  // Local state for filter
  const [filter, setFilter] = useState<'received' | 'given'>(
    filterType === 'given' ? 'given' : 'received',
  );

  // Update URL parameters when filter or page changes
  useEffect(() => {
    const params = new URLSearchParams();
    params.set('page', String(page));
    params.set('filter', filter);
    navigate({ search: `?${params}` }, { replace: true });
  }, [page, filter]);

  // Parameters for the query
  const queryParams: any = {
    page,
    pageSize,
  };

  if (filter === 'given') {
    queryParams.userId = currentUser?.id;
  } else {
    queryParams.targetUser = currentUser?.id;
  }

  // Fetch reviews based on the filter
  const { data, isLoading, isError } = useGetReviewsQuery(queryParams, {
    skip: !currentUser,
  });

  // Handle filter change with correct typing
  const handleFilterChange = (
    event: SelectChangeEvent<'received' | 'given'>,
  ) => {
    setFilter(event.target.value as 'received' | 'given');
  };

  return (
    <Box sx={{ p: 3 }}>
      {/* Title and Filter */}
      <Box
        sx={{
          display: 'flex',
          justifyContent: 'space-between',
          alignItems: 'center',
          mb: 3,
          flexDirection: { xs: 'column', sm: 'row' },
          gap: 2,
        }}
      >
        <Typography variant="h5">{t('reviewsPage.title')}</Typography>
        <FormControl variant="outlined" sx={{ minWidth: 200 }}>
          <InputLabel id="filter-select-label">
            {t('reviewsPage.filterLabel')}
          </InputLabel>
          <Select
            labelId="filter-select-label"
            value={filter}
            onChange={handleFilterChange}
            label={t('reviewsPage.filterLabel')}
          >
            <MenuItem value="received">
              {t('reviewsPage.filterReceived')}
            </MenuItem>
            <MenuItem value="given">{t('reviewsPage.filterGiven')}</MenuItem>
          </Select>
        </FormControl>
      </Box>

      {/* Reviews List */}
      <Box>
        {isLoading && (
          <Box sx={{ display: 'flex', justifyContent: 'center', py: 5 }}>
            <CircularProgress />
          </Box>
        )}
        {isError && (
          <Typography
            variant="body1"
            color="error"
            sx={{ textAlign: 'center', py: 5 }}
          >
            {t('reviewsPage.errorFetching')}
          </Typography>
        )}
        {data && data.reviews.length > 0
          ? data.reviews.map((review: Review) => (
              <ReviewCard
                key={`${review.noteId}_${review.userId}_${review.createdAt}`}
                review={review}
              />
            ))
          : !isLoading && (
              <Typography variant="body1" sx={{ textAlign: 'center', py: 5 }}>
                {t('reviewsPage.noReviews')}
              </Typography>
            )}
      </Box>

      {/* Pagination Bar */}
      {data && data.reviews.length > 0 && (
        <PaginationBar
          currentPage={page}
          pageSize={pageSize}
          totalPages={data.totalPages}
          totalCount={data.totalCount}
        />
      )}
    </Box>
  );
};

export default ReviewsPage;
