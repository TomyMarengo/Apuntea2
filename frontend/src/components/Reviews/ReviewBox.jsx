import { Box, Button } from '../index';
import { useTranslation } from 'react-i18next';
import {
  useGetReviewQuery,
  useCreateReviewMutation,
  useUpdateReviewMutation,
} from '../../store/slices/reviewsApiSlice';
import { useForm } from '../../hooks/index';
import { reviewInputs } from '../../constants/forms';
import StarSelector from '../Buttons/StarSelector';
import { PostReviewSchema, PatchReviewSchema } from '../../constants/schemas';

const ReviewBox = ({ note, userId }) => {
  const { t } = useTranslation();
  const [createReview, { isLoading: isLoadingCreate }] = useCreateReviewMutation();
  const [updateReview, { isLoading: isLoadingUpdate }] = useUpdateReviewMutation();

  const {
    data: review,
    isLoading: isLoadingReview,
    error: errorReview,
  } = useGetReviewQuery({ noteId: note.id, userId });

  const initialValues = { noteId: note.id, score: 5, content: '' };

  const { handleChange, handleSubmit, errors } = useForm({
    initialValues: review ? undefined : initialValues,
    args: review ? { userId, noteId: note.id } : undefined,
    submitCallback: review && !isLoadingReview ? updateReview : createReview,
    schema: review ? PatchReviewSchema : PostReviewSchema,
  });

  return (
    <Box className="flex flex-col mt-2">
      <form onSubmit={handleSubmit} className="my-2">
        {isLoadingReview ? (
          <span>...</span>
        ) : (
          <>
            <textarea
              {...reviewInputs.find((input) => input.name === 'content')}
              className="review-text bg-dark-bg w-full p-2 border-[1px] background-bg resize-none rounded-lg focus:ring-4 focus:ring-light-pri"
              placeholder={t('pages.notes.review.placeholder')}
              onChange={handleChange}
              defaultValue={review?.content || initialValues.content}
            />
            {errors?.content &&
              errors.content.length > 0 &&
              errors.content.map((error) => (
                <span className="text-red-500" key={error}>
                  {t(error)}
                </span>
              ))}
            <div className="flex flex-row justify-between mt-3 gap-4 items-center">
              <StarSelector
                {...reviewInputs.find((input) => input.name === 'score')}
                onStarClick={handleChange}
                initialRating={review?.score || initialValues.score}
              />
              <Button type="submit">{isLoadingReview ? t('actions.loading') : t('actions.save')}</Button>
            </div>
          </>
        )}
      </form>
    </Box>
  );
};

export default ReviewBox;
