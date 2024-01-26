import { Box, Button } from '../index';
import { useTranslation } from 'react-i18next';
import {
  useGetReviewQuery,
  useCreateReviewMutation,
  useUpdateReviewMutation,
} from '../../store/slices/reviewsApiSlice';
import { useForm } from '../../hooks/index';
import { reviewInputs } from '../../constants/forms';
import { useEffect } from 'react';
import StarSelector from '../Buttons/StarSelector';

const ReviewBox = ({ note, userId }) => {
  const { t } = useTranslation();
  const [createReview, { isLoading: isLoadingCreate }] = useCreateReviewMutation();
  const [updateReview, { isLoading: isLoadingUpdate }] = useUpdateReviewMutation();

  const {
    data: review,
    isLoading: isLoadingReview,
    error: errorReview,
  } = useGetReviewQuery({ noteId: note.id, userId });

  const { form, setFormValues, handleChange, handleSubmit } = useForm({
    initialValues: {
      noteId: note.id,
      userId,
      score: 5,
      content: '',
    },
    submitCallback: review && !isLoadingReview ? updateReview : createReview,
  });

  useEffect(() => {
    if (review) {
      setFormValues({
        noteId: note.id,
        userId,
        score: review.score,
        content: review.content,
      });
    }
  }, [review]);

  return (
    <Box className="flex flex-col">
      <form onSubmit={handleSubmit} className="my-2">
        {isLoadingReview ? (
          <span>...</span>
        ) : (
          <>
            <textarea
              {...reviewInputs.find((input) => input.name === 'content')} //TODO: ojo, se está trayendo también errorMessage
              className="review-text bg-dark-bg w-full p-2 border-[1px] background-bg resize-none rounded-lg focus:ring-4 focus:ring-light-pri"
              placeholder="Escribe un comentario..." //TODO: translate
              onChange={handleChange}
              value={form.content}
            ></textarea>
            <div className="flex flex-row justify-between mt-3 gap-4 items-center">
              <StarSelector
                {...reviewInputs.find((input) => input.name === 'score')}
                onStarClick={handleChange}
                initialRating={form.score}
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
