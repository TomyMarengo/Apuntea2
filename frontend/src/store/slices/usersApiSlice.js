import { apiSlice } from './apiSlice';

export const usersApiSlice = apiSlice.injectEndpoints({
  endpoints: (builder) => ({
    getUsers: builder.query({
      query: () => '/users',
      providesTags: ['Users'],
    }),
    getUser: builder.query({
      query: ({ userId, url }) => url || `/users/${userId}`,
      providesTags: ['Users'],
      refetchOnMountOrArgChange: true,
    }),
    getUserPicture: builder.query({
      query: ({ pictureId, url }) => ({
        url: url || `/pictures/${pictureId}`,
      }),
      providesTags: ['ProfilePicture'],
      refetchOnMountOrArgChange: true,
    }),
    updateUser: builder.mutation({
      query: ({
        userId,
        firstName,
        lastName,
        username,
        careerId,
        profilePicture,
        password,
        notificationsEnabled,
        url,
      }) => {
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
          url: url || `/users/${userId}`,
          method: 'PATCH',
          body: formData,
          headers: {
            // Do not set Content-Type for FormData, as it will be automatically set
          },
          formData: true,
        };
      },
      invalidatesTags: ['Users', 'ProfilePicture'],
    }),
    createUser: builder.mutation({
      query: userInfo => ({
        url: '/users',
        method: 'POST',
        body: userInfo,
      }),
      transformResponse: async (response, meta) => {
        return { location: meta.response.headers.get('Location') }
      }
    }),
  }),
});

export const { useGetUsersQuery, useGetUserQuery, useGetUserPictureQuery, useLazyGetUserQuery, useUpdateUserMutation, useCreateUserMutation } =
  usersApiSlice;
