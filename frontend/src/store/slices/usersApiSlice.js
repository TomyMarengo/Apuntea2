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
    }),
    getUserPicture: builder.query({
      query: ({ pictureId, url }) => ({
        url: url || `/pictures/${pictureId}`,
      }),
      providesTags: ['ProfilePicture'],
    }),
    updateUser: builder.mutation({
      query: ({
        userId,
        email, 
        firstName,
        lastName,
        username,
        careerId,
        // profilePicture,
        password,
        notificationsEnabled,
        url,
        oldPassword
      }) => {
        // const formData = new FormData();
        let headers = {};
        const data = {};
        // const aditionalHeaders = {};

        if (firstName !== undefined) data.firstName = firstName;
        if (lastName !== undefined) data.lastName = lastName;
        if (username !== undefined) data.username = username;
        if (careerId !== undefined) data.careerId = careerId;
        // TODO: Refactor profilePicture
        // if (profilePicture !== undefined) formData.append('profilePicture', profilePicture);
        if (password !== undefined && oldPassword !== undefined) {
          data.password = password;
          headers = {
            Authorization: `Basic ${btoa(`${email}:${oldPassword}`)}`
          } 
        }
        if (notificationsEnabled !== undefined) data.notificationsEnabled = notificationsEnabled;

        headers['Content-Type'] = "application/vnd.apuntea.user-update-v1.0+json";

        return {
          url: url || `/users/${userId}`,
          method: 'PATCH',
          body: JSON.stringify(data),
          headers 
        };
      },
      invalidatesTags: ['Users'],
    }),
    createUser: builder.mutation({
      query: userInfo => ({
        url: '/users',
        method: 'POST',
        body: JSON.stringify(userInfo),
        headers: {
            'Content-Type': "application/vnd.apuntea.user-create-v1.0+json"
        }
      }),
      transformResponse: async (response, meta) => {
        return { location: meta.response.headers.get('Location') }
      }
    }),
  }),
});

export const { useGetUsersQuery, useGetUserQuery, useGetUserPictureQuery, useLazyGetUserQuery, useUpdateUserMutation, useCreateUserMutation } =
  usersApiSlice;
