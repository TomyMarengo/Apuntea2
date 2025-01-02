// src/pages/Reviews/ReviewsPage.tsx

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
import React, { useState, useEffect } from 'react';
import { Helmet } from 'react-helmet-async';
import { useTranslation } from 'react-i18next';
import { useSelector } from 'react-redux';
import { useSearchParams, useNavigate } from 'react-router-dom';

import PaginationBar from '../../components/PaginationBar';
import ReviewCard from '../../components/ReviewCard';
import { selectCurrentUser } from '../../store/slices/authSlice';
import { useGetReviewsQuery } from '../../store/slices/reviewsApiSlice';
import { Review } from '../../types';

const ReviewsPage: React.FC = () => {
  const { t } = useTranslation('reviewsPage');
  const currentUser = useSelector(selectCurrentUser);
  const [searchParams] = useSearchParams();
  const navigate = useNavigate();

  const page = parseInt(searchParams.get('page') || '1', 10);
  const pageSize = 10;
  const filterType = searchParams.get('filter') || 'received';

  const [filter, setFilter] = useState<'received' | 'given'>(
    filterType === 'given' ? 'given' : 'received',
  );

  useEffect(() => {
    const params = new URLSearchParams();
    params.set('page', String(page));
    params.set('filter', filter);
    navigate({ search: `?${params}` }, { replace: true });
  }, [page, filter, navigate]);

  const queryParams: any = {
    page,
    pageSize,
  };

  if (filter === 'given') {
    queryParams.userId = currentUser?.id;
  } else {
    queryParams.targetUser = currentUser?.id;
  }

  const { data, isLoading, isError, error } = useGetReviewsQuery(queryParams, {
    skip: !currentUser,
  });

  const handleFilterChange = (
    event: SelectChangeEvent<'received' | 'given'>,
  ) => {
    setFilter(event.target.value as 'received' | 'given');
  };

  let pageTitle = t('titlePage');
  if (isLoading) {
    pageTitle = t('loading');
  } else if (isError) {
    pageTitle = t('errorFetching', { error: String(error) });
  }

  return (
    <>
      <Helmet>
        <title>{pageTitle}</title>
      </Helmet>
      <Box sx={{ p: 3 }}>
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
          <Typography variant="h5">{t('title')}</Typography>
          <FormControl variant="outlined" sx={{ minWidth: 200 }}>
            <InputLabel id="filter-select-label">{t('filterLabel')}</InputLabel>
            <Select
              labelId="filter-select-label"
              value={filter}
              onChange={handleFilterChange}
              label={t('filterLabel')}
            >
              <MenuItem value="received">{t('filterReceived')}</MenuItem>
              <MenuItem value="given">{t('filterGiven')}</MenuItem>
            </Select>
          </FormControl>
        </Box>

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
              {t('errorFetching')}
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
                  {t('noReviews')}
                </Typography>
              )}
        </Box>

        {data && data.reviews.length > 0 && (
          <PaginationBar
            currentPage={page}
            pageSize={pageSize}
            totalPages={data.totalPages}
            totalCount={data.totalCount}
          />
        )}
      </Box>
    </>
  );
};

export default ReviewsPage;
