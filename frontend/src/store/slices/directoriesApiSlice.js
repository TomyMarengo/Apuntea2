import { apiSlice } from './apiSlice';
export const directoriesApiSlice = apiSlice.injectEndpoints({
  endpoints: (builder) => ({
    getUserDirectoriesFavorites: builder.query({
      query: ({ userId, url }) => url || `/directories?favBy=${userId}`,
      providesTags: ['Directories'],
    }),

    getIsFavoriteDirectory: builder.query({
      query: ({ directoryId, userId, url }) => url || `/directories/${directoryId}/favorites/${userId}`,
    }),

    addFavoriteDirectory: builder.mutation({
      query: ({ directoryId, url }) => ({
        url: url || `/directories/${directoryId}/favorites`,
        method: 'POST',
      }),
    }),
    removeFavoriteDirectory: builder.mutation({
      query: ({ directoryId, userId, url }) => ({
        url: url || `/directories/${directoryId}/favorites/${userId}`,
        method: 'DELETE',
      }),
    }),
  }),
});

export const { useGetIsFavoriteDirectoryQuery, useAddFavoriteDirectoryMutation, useRemoveFavoriteDirectoryMutation } =
  directoriesApiSlice;
