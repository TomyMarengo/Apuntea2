import { apiSlice } from './apiSlice';

export const reviewsApiSlice = apiSlice.injectEndpoints({
  endpoints: (builder) => ({
    getReviews: builder.query({
      query: ({ noteId, url }) => url || `/reviews?noteId=${noteId}`,
      providesTags: ['Reviews'],
      refetchOnMountOrArgChange: true,
    }),
    getReview: builder.query({
      query: ({ noteId, userId, url }) => url || `/reviews/${noteId}_${userId}`,
      providesTags: ['Reviews'],
      refetchOnMountOrArgChange: true,
    }),
    createReview: builder.mutation({
      query: ({ noteId, score, content, url }) => ({
        url: url || `/reviews`,
        method: 'POST',
        body: { noteId, score, content },
      }),
      invalidatesTags: ['Reviews'],
    }),
    updateReview: builder.mutation({
      query: ({ noteId, userId, score, content, url }) => ({
        url: url || `/reviews/${noteId}_${userId}`,
        method: 'PUT',
        body: { score, content },
      }),
      invalidatesTags: ['Reviews'],
    }),
  }),
});

export const { useGetReviewsQuery, useGetReviewQuery, useCreateReviewMutation, useUpdateReviewMutation } =
  reviewsApiSlice;
