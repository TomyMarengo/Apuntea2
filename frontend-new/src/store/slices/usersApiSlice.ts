// store/slices/usersApiSlice.ts

import { fetchBaseQuery } from '@reduxjs/toolkit/query/react';
import { apiSlice } from './apiSlice';
import { User, Career, Institution } from '../../types';

interface UserArgs {
  userId?: string;
  url?: string;
}

interface UpdateUserArgs {
  userId: string;
  email?: string;
  firstName?: string;
  lastName?: string;
  username?: string;
  careerId?: string;
  password?: string;
  notificationsEnabled?: boolean;
  oldPassword?: string;
  url?: string;
}

interface PictureArgs {
  url?: string;
  profilePicture: File;
}

export const usersApiSlice = apiSlice.injectEndpoints({
  endpoints: (builder) => ({
    getUsers: builder.query<any, void>({
      query: () => '/users',
      providesTags: ['Users'],
    }),
    getOwner: builder.query<User, UserArgs>({
      query: ({ userId, url }) => url || `/users/${userId}`,
      transformResponse: (response: any) => ({
        id: response.id,
        email: response.email,
        username: response.username,
        locale: response.locale,
        status: response.status,
        notificationsEnabled: response.notificationsEnabled,
        selfUrl: response.self,
        institutionUrl: response.institution,
        careerUrl: response.career,
        subjectFavoritesUrl: response.subjectFavorites,
        noteFavoritesUrl: response.noteFavorites,
        noteGroupsUrl: response.noteGroups,
        reviewsReceivedUrl: response.reviewsReceived,
        directoryFavoritesUrl: response.directoryFavorites,
        followingUrl: response.following,
      }),
      providesTags: ['Users'],
    }),
    getUser: builder.query<User, UserArgs>({
      async queryFn({ userId, url }, _queryApi, _extraOptions, baseQuery) {
        try {
          // Fetch user data
          const userResult = await baseQuery({
            url: url || `/users/${userId}`,
          });
          if (userResult.error) return { error: userResult.error };

          const userData = userResult.data as any;

          // Fetch career data if careerUrl is present
          let careerData: Career | undefined = undefined;
          if (userData.career) {
            const careerResult = await baseQuery({ url: userData.career });
            if (!careerResult.error) {
              careerData = careerResult.data as Career;
            }
          }

          // Fetch institution data if institutionUrl is present
          let institutionData: Institution | undefined = undefined;
          if (userData.institution) {
            const institutionResult = await baseQuery({
              url: userData.institution,
            });
            if (!institutionResult.error) {
              institutionData = institutionResult.data as Institution;
            }
          }

          // Combine data into a single user object
          const combinedUser: User = {
            id: userData.id,
            email: userData.email,
            username: userData.username,
            locale: userData.locale,
            status: userData.status,
            notificationsEnabled: userData.notificationsEnabled,
            profilePictureUrl: userData.profilePictureUrl || '',
            career: careerData,
            institution: institutionData,
            selfUrl: userData.self,
            institutionUrl: userData.institution,
            careerUrl: userData.career,
            subjectFavoritesUrl: userData.subjectFavorites,
            noteFavoritesUrl: userData.noteFavorites,
            noteGroupsUrl: userData.noteGroups,
            reviewsReceivedUrl: userData.reviewsReceived,
            directoryFavoritesUrl: userData.directoryFavorites,
            followingUrl: userData.following,
            // Add other fields as necessary
          };

          return { data: combinedUser };
        } catch (error) {
          return {
            error: {
              status: 'CUSTOM_ERROR',
              error: 'Failed to fetch user data',
            },
          };
        }
      },
      providesTags: ['Users'],
    }),
    getUserPicture: builder.query<any, { pictureId: string; url?: string }>({
      query: ({ pictureId, url }) => ({
        url: url || `/pictures/${pictureId}`,
      }),
      keepUnusedDataFor: 60 * 60 * 60 * 24 * 30, // 30 days
      providesTags: ['ProfilePicture'],
    }),
    updatePicture: builder.mutation<any, PictureArgs>({
      query: ({ url, profilePicture }) => {
        const formData = new FormData();
        formData.append('profilePicture', profilePicture);
        return {
          url: url || '/pictures',
          method: 'POST',
          body: formData,
        };
      },
      invalidatesTags: ['Users', 'ProfilePicture'],
    }),
    updateUser: builder.mutation<any, UpdateUserArgs>({
      query: ({
        userId,
        email,
        firstName,
        lastName,
        username,
        careerId,
        password,
        notificationsEnabled,
        url,
        oldPassword,
      }) => {
        const data: Record<string, any> = {};
        let headers: Record<string, string> = {};

        if (firstName !== undefined) data.firstName = firstName;
        if (lastName !== undefined) data.lastName = lastName;
        if (username !== undefined) data.username = username;
        if (careerId !== undefined) data.careerId = careerId;

        if (password !== undefined && oldPassword !== undefined) {
          data.password = password;
          headers['Authorization'] = `Basic ${btoa(`${email}:${oldPassword}`)}`;
        }
        if (notificationsEnabled !== undefined) {
          data.notificationsEnabled = notificationsEnabled;
        }

        headers['Content-Type'] =
          'application/vnd.apuntea.user-update-v1.0+json';

        return {
          url: url || `/users/${userId}`,
          method: 'PATCH',
          body: JSON.stringify(data),
          headers,
        };
      },
      invalidatesTags: ['Users'],
    }),
    createUser: builder.mutation<any, Record<string, any>>({
      query: (userInfo) => ({
        url: '/users',
        method: 'POST',
        body: JSON.stringify(userInfo),
        headers: {
          'Content-Type': 'application/vnd.apuntea.user-create-v1.0+json',
        },
      }),
      transformResponse: async (response: any, meta: any) => {
        return { location: meta.response.headers.get('Location') };
      },
    }),
  }),
});

export const {
  useGetUsersQuery,
  useGetUserQuery,
  useGetUserPictureQuery,
  useLazyGetUserQuery,
  useUpdateUserMutation,
  useUpdatePictureMutation,
  useCreateUserMutation,
  useGetOwnerQuery,
  useLazyGetOwnerQuery,
} = usersApiSlice;
