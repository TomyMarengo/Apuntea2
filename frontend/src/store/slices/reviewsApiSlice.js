import { apiSlice } from './apiSlice';

export const reviewsApiSlice = apiSlice.injectEndpoints({
  endpoints: (builder) => ({
    getReviews: builder.query({
      query: ({ noteId, url }) => url || `/reviews?noteId=${noteId}`,
      refetchOnMountOrArgChange: true,
    }),
  }),
});

export const { useGetReviewsQuery } = reviewsApiSlice;
