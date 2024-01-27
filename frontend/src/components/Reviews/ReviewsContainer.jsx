import { useTranslation } from 'react-i18next';
import { useSelector } from 'react-redux';

import { ReviewBox, ReviewCard } from '../index';
import { useGetReviewsQuery } from '../../store/slices/reviewsApiSlice';
import { selectCurrentUserId } from '../../store/slices/authSlice';

const ReviewsContainer = ({ score, note }) => {
  const { t } = useTranslation();
  const { data: reviews, isLoading: isLoadingReviews, error: errorReviews } = useGetReviewsQuery({ noteId: note.id });
  const userId = useSelector(selectCurrentUserId);

  return (
    <div className="reviews">
      {isLoadingReviews ? (
        <span>... </span>
      ) : (
        <>
          <h2 className="text-3xl text-dark-pri mb-1">{t('data.reviews')}</h2>
          <span>
            {t('data.score')}: {score} ⭐
          </span>
          <div className="p-2 mt-1">
            <div className="reviews-container">
              {reviews?.map((review) => {
                if (review.userId !== userId) {
                  return <ReviewCard key={review.userId} {...review} />;
                }
              })}
            </div>
            <ReviewBox note={note} userId={userId} />
          </div>
        </>
      )}
    </div>
  );
};
export default ReviewsContainer;
