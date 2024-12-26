// src/pages/Profile/RecentReviews.tsx

import React from 'react';
import {
  Box,
  Typography,
  List,
  ListItem,
  ListItemText,
  Button,
  CircularProgress,
} from '@mui/material';
import { useTranslation } from 'react-i18next';
import { Link as RouterLink } from 'react-router-dom';
import { useGetReviewsQuery } from '../../store/slices/reviewsApiSlice';
import { Review } from '../../types';

interface RecentReviewsProps {
  userId: string;
}

const RecentReviews: React.FC<RecentReviewsProps> = ({ userId }) => {
  const { t } = useTranslation();

  const { data, isLoading, error } = useGetReviewsQuery(
    { userId },
    {
      // Adjust the API endpoint if necessary
    },
  );

  if (isLoading) {
    return (
      <Typography variant="body1">
        <CircularProgress size={24} /> {t('loading')}
      </Typography>
    );
  }

  if (error) {
    return (
      <Typography variant="body1" color="error">
        {t('errorFetchingReviews')}
      </Typography>
    );
  }

  return (
    <Box>
      <Box sx={{ display: 'flex', justifyContent: 'space-between', mb: 2 }}>
        <Typography variant="h6">{t('recentReviews')}</Typography>
        <Button
          component={RouterLink}
          to="/profile/reviews"
          variant="outlined"
          size="small"
        >
          {t('viewMore')}
        </Button>
      </Box>
      {data && data.length > 0 ? (
        <List>
          {data.slice(0, 5).map((review: Review) => (
            <ListItem key={review.id} disablePadding>
              <ListItemText
                primary={`Review for Note ID: ${review.noteId}`}
                secondary={`Score: ${review.score} - ${review.content.substring(0, 50)}...`}
              />
            </ListItem>
          ))}
        </List>
      ) : (
        <Typography>{t('noRecentReviews')}</Typography>
      )}
    </Box>
  );
};

export default RecentReviews;
