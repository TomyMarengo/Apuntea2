// src/store/slices/reviewsApiSlice.ts

import { ApiResponse, apiSlice } from './apiSlice';
import {
  DELETE_REASON_CONTENT_TYPE,
  REVIEW_COLLECTION_CONTENT_TYPE,
  REVIEW_CONTENT_TYPE,
  REVIEW_CREATE_CONTENT_TYPE,
  REVIEW_UPDATE_CONTENT_TYPE,
} from '../../contentTypes.ts';
import { Review } from '../../types';
import { extractErrorMessages } from '../../utils/helpers';
import { mapApiReview } from '../../utils/mappers';

interface GetReviewsArgs {
  noteId?: string;
  userId?: string;
  targetUser?: string;
  url?: string;
  page?: number;
  pageSize?: number;
}

interface PaginatedReviews {
  reviews: Review[];
  totalCount: number;
  totalPages: number;
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
  userId: string;
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
    getReviews: builder.query<PaginatedReviews, GetReviewsArgs>({
      query: ({ noteId, userId, targetUser, page = 1, pageSize = 10, url }) => {
        let queryUrl = url || '/reviews';
        const params = new URLSearchParams();

        if (noteId) params.append('noteId', noteId);
        if (userId) params.append('userId', userId);
        if (targetUser) params.append('targetUser', targetUser);
        params.append('page', String(page));
        params.append('pageSize', String(pageSize));

        if (!url) {
          queryUrl += `?${params.toString()}`;
        }

        return {
          url: queryUrl,
          headers: {
            Accept: REVIEW_COLLECTION_CONTENT_TYPE,
          },
        };
      },
      transformResponse: (response: any, meta: { response: Response }) => {
        const totalCount = Number(
          meta.response.headers.get('X-Total-Count') || '0',
        );
        const totalPages = Number(
          meta.response.headers.get('X-Total-Pages') || '0',
        );
        const reviews = Array.isArray(response)
          ? response.map(mapApiReview)
          : [];

        return {
          reviews,
          totalCount,
          totalPages,
        };
      },
      providesTags: (result, _error, { noteId }) =>
        result
          ? [
              ...result.reviews.map(({ noteId, userId }) => ({
                type: 'Reviews' as const,
                id: `${noteId}_${userId}`,
              })),
              { type: 'Reviews', id: `LIST_${noteId}` },
            ]
          : [{ type: 'Reviews', id: `LIST_${noteId}` }],
    }),
    getMyReview: builder.query<Review, ReviewArgs>({
      query: ({ userId, url }) => ({
        url: url + `&userId=${userId}`,
        headers: {
          Accept: REVIEW_COLLECTION_CONTENT_TYPE,
        },
      }),
      transformResponse: (response: any) => {
        const review = Array.isArray(response) ? response[0] : response;
        return mapApiReview(review);
      },
      providesTags: (_result, _error, { noteId, userId }) => [
        { type: 'Reviews', id: `${noteId}_${userId}` },
        { type: 'Reviews', id: `LIST_${noteId}` },
      ],
    }),
    getReview: builder.query<Review, ReviewArgs>({
      query: ({ noteId, userId, url }) => ({
        url: url || `/reviews/${noteId}_${userId}`,
        headers: {
          Accept: REVIEW_CONTENT_TYPE,
        },
      }),
      transformResponse: (response: any) => {
        return mapApiReview(response);
      },
      providesTags: (_result, _error, { noteId, userId }) => [
        { type: 'Reviews', id: `${noteId}_${userId}` },
      ],
    }),
    createReview: builder.mutation<ApiResponse, CreateReviewArgs>({
      queryFn: async (
        { noteId, score, content, url },
        _queryApi,
        _extraOptions,
        fetchWithBQ,
      ) => {
        const result = await fetchWithBQ({
          url: url || '/reviews',
          method: 'POST',
          body: JSON.stringify({ noteId, score, content }),
          headers: {
            'Content-Type': REVIEW_CREATE_CONTENT_TYPE,
          },
        });
        let errorMessages = extractErrorMessages(result.error);

        return {
          data: {
            success: result.error === undefined,
            messages: errorMessages,
          },
        };
      },
      invalidatesTags: (_result, _error, { noteId, userId }) => [
        { type: 'Reviews', id: `${noteId}_${userId}` },
        { type: 'Reviews', id: `LIST_${noteId}` },
      ],
    }),
    updateReview: builder.mutation<ApiResponse, UpdateReviewArgs>({
      queryFn: async (
        { noteId, userId, score, content, url },
        _queryApi,
        _extraOptions,
        fetchWithBQ,
      ) => {
        const result = await fetchWithBQ({
          url: url || `/reviews/${noteId}_${userId}`,
          method: 'PATCH',
          body: JSON.stringify({ score, content }),
          headers: {
            'Content-Type': REVIEW_UPDATE_CONTENT_TYPE,
          },
        });
        let errorMessages = extractErrorMessages(result.error);

        return {
          data: {
            success: result.error === undefined,
            messages: errorMessages,
          },
        };
      },
      invalidatesTags: (_result, _error, { noteId, userId }) => [
        { type: 'Reviews', id: `${noteId}_${userId}` },
      ],
    }),
    deleteReview: builder.mutation<ApiResponse, DeleteReviewArgs>({
      queryFn: async (
        { noteId, userId, reason, url },
        _api,
        _extraOptions,
        baseQuery,
      ) => {
        const body: any = {};
        if (reason !== undefined) body.reason = reason;

        const result = await baseQuery({
          url: url || `/reviews/${noteId}_${userId}`,
          method: 'POST',
          body: JSON.stringify(body),
          headers: {
            'Content-Type': DELETE_REASON_CONTENT_TYPE,
          },
        });

        return {
          data: {
            success: result.error === undefined,
            messages: [],
          },
        };
      },
    }),
  }),
});

export const {
  useGetReviewsQuery,
  useGetMyReviewQuery,
  useGetReviewQuery,
  useCreateReviewMutation,
  useUpdateReviewMutation,
  useDeleteReviewMutation,
} = reviewsApiSlice;
