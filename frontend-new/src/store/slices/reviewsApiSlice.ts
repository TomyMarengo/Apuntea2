// src/store/slices/reviewsApiSlice.ts

import { apiSlice } from './apiSlice';

interface ReviewArgs {
  noteId?: string;
  userId?: string;
  url?: string;
  score?: number;
  content?: string;
}

export const reviewsApiSlice = apiSlice.injectEndpoints({
  endpoints: (builder) => ({
    getReviews: builder.query<any, { noteId?: string; url?: string }>({
      query: ({ noteId, url }) => url || `/reviews?noteId=${noteId}`,
      providesTags: ['Reviews'],
      refetchOnMountOrArgChange: true,
    }),
    getReview: builder.query<
      any,
      { noteId?: string; userId?: string; url?: string }
    >({
      query: ({ noteId, userId, url }) => url || `/reviews/${noteId}_${userId}`,
      providesTags: ['Reviews'],
      refetchOnMountOrArgChange: true,
    }),
    createReview: builder.mutation<any, Omit<ReviewArgs, 'userId'>>({
      query: ({ noteId, score, content, url }) => ({
        url: url || '/reviews',
        method: 'POST',
        body: { noteId, score, content },
      }),
      invalidatesTags: ['Reviews'],
    }),
    updateReview: builder.mutation<any, Required<ReviewArgs>>({
      query: ({ noteId, userId, score, content, url }) => ({
        url: url || `/reviews/${noteId}_${userId}`,
        method: 'PUT',
        body: { score, content },
      }),
      invalidatesTags: ['Reviews'],
    }),
  }),
});

export const {
  useGetReviewsQuery,
  useGetReviewQuery,
  useCreateReviewMutation,
  useUpdateReviewMutation,
} = reviewsApiSlice;
