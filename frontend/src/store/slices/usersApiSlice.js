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
        const formData = new FormData();

        formData.append('userId', userId);

        if (firstName !== undefined) formData.append('firstName', firstName);
        if (lastName !== undefined) formData.append('lastName', lastName);
        if (username !== undefined) formData.append('username', username);
        if (careerId !== undefined) formData.append('careerId', careerId);
        if (profilePicture !== undefined) formData.append('profilePicture', profilePicture);
        if (password !== undefined) formData.append('password', password);
        if (notificationsEnabled !== undefined) formData.append('notificationsEnabled', notificationsEnabled);

        return {
          url: `/users/${userId}`,
          method: 'PATCH',
          body: formData,
          headers: {
            // Do not set Content-Type for FormData, as it will be automatically set
          },
          formData: true,
        };
      },
    }),
  }),
});

export const { useGetUsersQuery, useGetUserMutation, useUpdateUserMutation } = usersApiSlice;
