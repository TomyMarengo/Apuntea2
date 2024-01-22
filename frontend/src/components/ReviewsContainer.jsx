import { ReviewBox, ReviewCard } from './index';
import { useGetReviewsQuery } from '../store/slices/reviewsApiSlice';
import { useTranslation } from 'react-i18next';

const ReviewsContainer = ({ score, noteId }) => {
  const { t } = useTranslation();
  const { data: reviews, isLoading: isLoadingReviews, error: errorReviews } = useGetReviewsQuery({ noteId });

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
              {reviews.map((review) => (
                <ReviewCard key={review.userId} {...review} />
              ))}
            </div>
            <ReviewBox />
          </div>
        </>
      )}
    </div>
  );
};
export default ReviewsContainer;
