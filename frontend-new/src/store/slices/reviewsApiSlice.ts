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

interface CreateReviewArgs {
  noteId: string;
  score: number;
  content?: string;
  url?: string;
}

interface UpdateReviewArgs {
  noteId: string;
  userId: string;
  score: number;
  content?: string;
  url?: string;
}

interface DeleteReviewArgs {
  noteId: string;
  userId: string;
  reason?: string;
  url?: string;
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
    createReview: builder.mutation<boolean, CreateReviewArgs>({
      queryFn: async (
        { noteId, score, content, url },
        _queryApi,
        _extraOptions,
        fetchWithBQ,
      ) => {
        const response = await fetchWithBQ({
          url: url || '/reviews',
          method: 'POST',
          body: { noteId, score, content },
        });
        if (response.error) {
          return { error: response.error };
        }
        return { data: true };
      },
      invalidatesTags: (result, error, { noteId }) => [
        { type: 'Reviews', id: noteId },
      ],
    }),
    updateReview: builder.mutation<boolean, UpdateReviewArgs>({
      queryFn: async (
        { noteId, userId, score, content, url },
        _queryApi,
        _extraOptions,
        fetchWithBQ,
      ) => {
        const response = await fetchWithBQ({
          url: url || `/reviews/${noteId}_${userId}`,
          method: 'PUT',
          body: { score, content },
        });
        if (response.error) {
          return { error: response.error };
        }
        return { data: true };
      },
      invalidatesTags: (result, error, { noteId, userId }) => [
        { type: 'Reviews', id: noteId },
        { type: 'Reviews', id: `${noteId}_${userId}` },
      ],
    }),
    deleteReview: builder.mutation<boolean, DeleteReviewArgs>({
      queryFn: async (
        { noteId, userId, reason, url },
        _queryApi,
        _extraOptions,
        fetchWithBQ,
      ) => {
        const response = await fetchWithBQ({
          url: url || `/reviews/${noteId}_${userId}`,
          method: 'DELETE',
          body: { reason },
        });
        if (response.error) {
          return { error: response.error };
        }
        return { data: true };
      },
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
  useDeleteReviewMutation,
} = reviewsApiSlice;
