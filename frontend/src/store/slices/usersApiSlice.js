import { apiSlice } from './apiSlice';

export const usersApiSlice = apiSlice.injectEndpoints({
  endpoints: (builder) => ({
    getUsers: builder.query({
      query: () => '/users',
      keepUnusedDataFor: 5 * 60 * 60 * 1000, // 5 hours
    }),
    getUser: builder.mutation({
      query: (userId) => ({
        url: `/users/${userId}`,
        method: 'GET',
      }),
    }),
    updateUser: builder.mutation({
      query: ({ userId, firstName, lastName, username, careerId, profilePicture, password, notificationsEnabled }) => {
        const bodyParams = {
          ...(firstName !== undefined && { firstName }),
          ...(lastName !== undefined && { lastName }),
          ...(username !== undefined && { username }),
          ...(careerId !== undefined && { careerId }),
          ...(profilePicture !== undefined && { profilePicture }),
          ...(password !== undefined && { password }),
          ...(notificationsEnabled !== undefined && { notificationsEnabled }),
        };

        return {
          url: `/users/${userId}`,
          method: 'PATCH',
          body: bodyParams,
        };
      },
    }),
  }),
});

export const { useGetUsersQuery, useGetUserMutation, useUpdateUserMutation } = usersApiSlice;
