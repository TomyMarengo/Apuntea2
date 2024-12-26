// src/store/slices/reviewsApiSlice.ts

import { apiSlice } from './apiSlice';

import { Review } from '../../types';

import { mapApiReview } from '../../utils/mappers';

interface ReviewArgs {
  noteId?: string;
  userId?: string;
  url?: string;
  score?: number;
  content?: string;
}

export const reviewsApiSlice = apiSlice.injectEndpoints({
  endpoints: (builder) => ({
    getReviews: builder.query<Review[], ReviewArgs>({
      query: ({ noteId, userId, url }) =>
        url || `/reviews?noteId=${noteId}&userId=${userId}`,
      transformResponse: (response: any) => {
        return Array.isArray(response) ? response.map(mapApiReview) : [];
      },
      providesTags: ['Reviews'],
    }),
    getReview: builder.query<Review, ReviewArgs>({
      query: ({ noteId, userId, url }) => url || `/reviews/${noteId}_${userId}`,
      transformResponse: (response: any) => {
        return mapApiReview(response);
      },
      providesTags: (result, error, { noteId, userId }) => [
        { type: 'Reviews', id: `${noteId}_${userId}` },
      ],
    }),
    createReview: builder.mutation<void, ReviewArgs>({
      query: ({ noteId, score, content, url }) => ({
        url: url || '/reviews',
        method: 'POST',
        body: { noteId, score, content },
      }),
      invalidatesTags: (result, error, { noteId }) => [
        { type: 'Reviews', id: noteId },
      ],
    }),
    updateReview: builder.mutation<void, ReviewArgs>({
      query: ({ noteId, userId, score, content, url }) => ({
        url: url || `/reviews/${noteId}_${userId}`,
        method: 'PUT',
        body: { score, content },
      }),
      invalidatesTags: (result, error, { noteId, userId }) => [
        { type: 'Reviews', id: noteId },
        { type: 'Reviews', id: `${noteId}_${userId}` },
      ],
    }),
  }),
});

export const {
  useGetReviewsQuery,
  useGetReviewQuery,
  useCreateReviewMutation,
  useUpdateReviewMutation,
} = reviewsApiSlice;
