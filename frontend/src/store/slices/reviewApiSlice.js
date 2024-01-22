import { apiSlice } from './apiSlice';

export const reviewApiSlice = apiSlice.injectEndpoints({
  endpoints: (builder) => ({
    getReviews: builder.query({
      query: (noteId) => `/reviews?noteId=${noteId}`,
      transformResponse: async (response) => {
        const reviews = await response;
        return { reviews };
      },
    }),
  }),
});

export const { useGetReviewsQuery } = reviewApiSlice;
