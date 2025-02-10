// src/pages/Reviews/ReviewsPage.tsx

import { zodResolver } from '@hookform/resolvers/zod';
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
import React, { useEffect, useMemo } from 'react';
import { Helmet } from 'react-helmet-async';
import { useForm, Controller } from 'react-hook-form';
import { useTranslation } from 'react-i18next';
import { useSelector } from 'react-redux';
import { useSearchParams, useNavigate } from 'react-router-dom';
import { z } from 'zod';

import PaginationBar from '../../components/PaginationBar';
import ReviewCard from '../../components/ReviewCard';
import { selectCurrentUser } from '../../store/slices/authSlice';
import { useLazyGetReviewsQuery } from '../../store/slices/reviewsApiSlice';
import { Review } from '../../types';

const reviewsSchema = z.object({
  filter: z.string(),
  page: z.string(),
  pageSize: z.string(),
});

type ReviewsFormValues = z.infer<typeof reviewsSchema>;

const ReviewsPage: React.FC = () => {
  const { t } = useTranslation('reviewsPage');
  const [searchParams] = useSearchParams();
  const navigate = useNavigate();

  const currentUser = useSelector(selectCurrentUser);
  const [getReviews, { data, isLoading, isError, error }] =
    useLazyGetReviewsQuery();

  const defaultValues = useMemo(
    () => ({
      filter: searchParams.get('filter') || 'received',
      page: searchParams.get('page') || '1',
      pageSize: searchParams.get('pageSize') || '1',
    }),
    [searchParams],
  );

  const { control, watch, reset } = useForm<ReviewsFormValues>({
    resolver: zodResolver(reviewsSchema),
    defaultValues,
  });

  const watchedValues = watch();
  const { filter, page, pageSize } = watchedValues;

  const fetchData = async (data: ReviewsFormValues) => {
    if (!currentUser) {
      return;
    }

    const queryParams: Record<string, string> = { ...data };
    if (filter === 'given') {
      queryParams.userId = currentUser?.id;
    } else {
      queryParams.targetUser = currentUser?.id;
    }

    await getReviews({
      ...queryParams,
    });
  };

  useEffect(() => {
    reset(defaultValues);
    fetchData(defaultValues);
  }, [searchParams]);

  const onFilterChange = (e: SelectChangeEvent<string>) => {
    const newParams = new URLSearchParams(window.location.search);
    newParams.set('filter', e.target.value as string);
    newParams.set('page', '1');
    navigate(`?${newParams.toString()}`);
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

            gap: 2,
          }}
        >
          <Typography variant="h5">{t('title')}</Typography>

          <FormControl variant="outlined" sx={{ minWidth: 200 }}>
            <InputLabel id="filter-select-label">{t('filterLabel')}</InputLabel>
            <Controller
              name="filter"
              control={control}
              render={({ field }) => (
                <Select
                  {...field}
                  labelId="filter-select-label"
                  label={t('filterLabel')}
                  onChange={(e) => {
                    field.onChange(e);
                    onFilterChange(e);
                  }}
                >
                  <MenuItem value="received">{t('filterReceived')}</MenuItem>
                  <MenuItem value="given">{t('filterGiven')}</MenuItem>
                </Select>
              )}
            />
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
            currentPage={Number(page)}
            pageSize={Number(pageSize)}
            totalPages={data.totalPages}
            totalCount={data.totalCount}
          />
        )}
      </Box>
    </>
  );
};

export default ReviewsPage;
