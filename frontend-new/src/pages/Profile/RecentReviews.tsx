// src/pages/Profile/RecentReviews.tsx

import React from 'react';
import { Box, Typography, Button, CircularProgress } from '@mui/material';
import { useTranslation } from 'react-i18next';
import { Link as RouterLink } from 'react-router-dom';
import { useGetReviewsQuery } from '../../store/slices/reviewsApiSlice';
import { Review } from '../../types';
import ReviewCard from '../../components/ReviewCard';

interface RecentReviewsProps {
  userId?: string;
}

const RecentReviews: React.FC<RecentReviewsProps> = ({ userId }) => {
  const { t } = useTranslation();

  const { data, isLoading, error } = useGetReviewsQuery({ userId });

  if (isLoading) {
    return (
      <Box sx={{ display: 'flex', alignItems: 'center' }}>
        <CircularProgress size={24} />{' '}
        <Typography sx={{ ml: 2 }}>{t('recentReviews.loading')}</Typography>
      </Box>
    );
  }

  if (error) {
    return (
      <Typography variant="body1" color="error">
        {t('recentReviews.errorFetchingReviews')}
      </Typography>
    );
  }

  return (
    <Box>
      <Box sx={{ display: 'flex', justifyContent: 'space-between', mb: 2 }}>
        <Typography variant="h6">{t('recentReviews.recentReviews')}</Typography>
        <Button
          component={RouterLink}
          to="/reviews"
          variant="outlined"
          size="small"
        >
          {t('recentReviews.viewMore')}
        </Button>
      </Box>
      {data && data.length > 0 ? (
        <Box>
          {data.slice(0, 5).map((review: Review) => (
            <ReviewCard key={review.userId + review.noteId} review={review} />
          ))}
        </Box>
      ) : (
        <Typography>{t('recentReviews.noRecentReviews')}</Typography>
      )}
    </Box>
  );
};

export default RecentReviews;
