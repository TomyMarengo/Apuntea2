import { expect, describe, it } from 'vitest';
import { expectToBePagedContent, setupApiStore } from '../setup/utils';
import {
  useGetReviewsQuery,
  reviewsApiSlice,
  useCreateReviewMutation,
  useUpdateReviewMutation,
  useDeleteReviewMutation,
  useGetReviewQuery,
  useGetMyReviewQuery,
} from '../../store/slices/reviewsApiSlice';
import { waitFor } from '@testing-library/react';
import { useWithWrapper } from '../setup/wrapper.jsx';
import {
  someUserId,
  someNoteId,
  invalidScoreMsg,
} from '../mocks/reviewsApiMocks.js';

// Setup API store helper for reviews API
function setupReviewsApiStore() {
  return setupApiStore(reviewsApiSlice);
}
const store = setupReviewsApiStore();

describe('reviewsApiSlice', () => {
  it('should fetch reviews successfully', async () => {
    const { result } = useWithWrapper(() => useGetReviewsQuery({}), store);
    await waitFor(() => expect(result.current.isSuccess).toBe(true));
    const { reviews, totalCount, totalPages } = result.current.data;
    expectToBePagedContent(reviews, totalCount, totalPages);
  });

  it('should fetch a single review successfully', async () => {
    const { result } = useWithWrapper(
      () => useGetReviewQuery({ noteId: someNoteId, userId: someUserId }),
      store,
    );
    await waitFor(() => expect(result.current.isSuccess).toBe(true));
    const review = result.current.data;
    expect(review).toBeTruthy();
    expect(review.noteId).toBe(someNoteId);
    expect(review.userId).toBe(someUserId);
  });

  it('should fetch my review successfully', async () => {
    const { result } = useWithWrapper(
      () =>
        useGetMyReviewQuery({
          url: `/reviews?noteId=${someNoteId}`,
          userId: someUserId,
        }),
      store,
    );
    await waitFor(() => expect(result.current.isSuccess).toBe(true));
    const review = result.current.data;
    expect(review).toBeTruthy();
    expect(review.userId).toBe(someUserId);
  });

  it('should create a review successfully', async () => {
    let { result: wrapperResult } = useWithWrapper(
      async () => useCreateReviewMutation(),
      store,
    );
    const [createReview] = await wrapperResult.current;
    const result = await createReview({
      noteId: someNoteId,
      score: 5,
    }).unwrap();
    expect(result.success).toBe(true);
    expect(result.messages).empty;
  });

  it('should fail to create a review with invalid score and relay error messages', async () => {
    let { result: wrapperResult } = useWithWrapper(
      async () => useCreateReviewMutation(),
      store,
    );
    const [createReview] = await wrapperResult.current;
    const result = await createReview({
      noteId: someNoteId,
      score: -1,
    }).unwrap();
    expect(result.success).toBe(false);
    expect(result.messages).contain(invalidScoreMsg);
  });

  it('should modify a review successfully', async () => {
    let { result: wrapperResult } = useWithWrapper(
      async () => useUpdateReviewMutation(),
      store,
    );
    const [updateReview] = await wrapperResult.current;
    const result = await updateReview({
      noteId: someNoteId,
      score: 5,
    }).unwrap();
    expect(result.success).toBe(true);
    expect(result.messages).empty;
  });

  it('should fail to modify a review with invalid score and relay error messages', async () => {
    let { result: wrapperResult } = useWithWrapper(
      async () => useUpdateReviewMutation(),
      store,
    );
    const [updateReview] = await wrapperResult.current;
    const result = await updateReview({
      noteId: someNoteId,
      score: -1,
    }).unwrap();
    expect(result.success).toBe(false);
    expect(result.messages).contain(invalidScoreMsg);
  });
});

//--useGetReviewsQuery,
//--useGetMyReviewQuery,
//--useGetReviewQuery,
//--useCreateReviewMutation,
//--useUpdateReviewMutation,
//  useDeleteReviewMutation,
