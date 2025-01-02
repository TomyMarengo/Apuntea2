// src/pages/Profile/RecentReviews.tsx

import { Box, Typography, Button, CircularProgress } from '@mui/material';
import React from 'react';
import { useTranslation } from 'react-i18next';
import { Link as RouterLink } from 'react-router-dom';

import ReviewCard from '../../components/ReviewCard';
import { useGetReviewsQuery } from '../../store/slices/reviewsApiSlice';
import { Review } from '../../types';

interface RecentReviewsProps {
  targetId?: string;
}

const RecentReviews: React.FC<RecentReviewsProps> = ({ targetId }) => {
  const { t } = useTranslation('recentReviews');

  const { data, isLoading, error } = useGetReviewsQuery({
    targetUser: targetId,
  });

  if (isLoading) {
    return (
      <Box sx={{ display: 'flex', alignItems: 'center' }}>
        <CircularProgress size={24} />{' '}
        <Typography sx={{ ml: 2 }}>{t('loading')}</Typography>
      </Box>
    );
  }

  if (error) {
    return (
      <Typography variant="body1" color="error">
        {t('errorFetchingReviews')}
      </Typography>
    );
  }

  const hasReviews = data && data.reviews?.length > 0;

  return (
    <Box>
      <Box sx={{ display: 'flex', justifyContent: 'space-between', mb: 2 }}>
        <Typography variant="h6">{t('recentReviews')}</Typography>
        {hasReviews && (
          <Button
            component={RouterLink}
            to="/reviews"
            variant="outlined"
            size="small"
          >
            {t('viewMore')}
          </Button>
        )}
      </Box>
      {hasReviews ? (
        <Box>
          {data.reviews.slice(0, 5).map((review: Review) => (
            <ReviewCard key={review.userId + review.noteId} review={review} />
          ))}
        </Box>
      ) : (
        <Typography>{t('noRecentReviews')}</Typography>
      )}
    </Box>
  );
};

export default RecentReviews;
