// src/store/slices/reviewsApiSlice.ts

import { apiSlice } from './apiSlice';
import { Review } from '../../types';
import { mapApiReview } from '../../utils/mappers';

interface getReviewsArgs {
  noteId?: string;
  userId?: string;
  url?: string;
}

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
    getReviews: builder.query<Review[], getReviewsArgs>({
      query: ({ noteId, userId, url }) => {
        let queryUrl = url || '/reviews';
        const params = new URLSearchParams();
        if (noteId) params.append('noteId', noteId);
        if (userId) params.append('userId', userId);
        if (!url) queryUrl += `?${params.toString()}`;
        return queryUrl;
      },
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
        return { data: response.error === undefined };
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
        return { data: response.error === undefined };
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
        return { data: response.error === undefined };
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
