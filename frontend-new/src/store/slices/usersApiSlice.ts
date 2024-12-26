// store/slices/usersApiSlice.ts

import { fetchBaseQuery } from '@reduxjs/toolkit/query/react';
import { apiSlice } from './apiSlice';
import { User, Career, Institution } from '../../types';

import { mapApiUser } from '../../utils/mappers';

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
  pictureId?: string;
  profilePicture: File;
  userId?: string;
}

interface CreateUserArgs {
  email: string;
  password: string;
  institutionId: string;
  careerId: string;
}

export const usersApiSlice = apiSlice.injectEndpoints({
  endpoints: (builder) => ({
    getUsers: builder.query<User[], void>({
      query: () => '/users',
      transformResponse: (response: any) => {
        return Array.isArray(response) ? response.map(mapApiUser) : [];
      },
      providesTags: ['Users'],
    }),
    getOwner: builder.query<User, UserArgs>({
      query: ({ userId, url }) => url || `/users/${userId}`,
      transformResponse: (response: any) => {
        return mapApiUser(response);
      },
      providesTags: (result, error, { userId }) => [
        { type: 'Users', id: userId },
      ],
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
            subjectsUrl: userData.subjects,
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
      providesTags: (result, error, { userId }) => [
        { type: 'Users', id: userId },
      ],
    }),
    getUserPicture: builder.query<any, PictureArgs>({
      query: ({ pictureId, url }) => ({
        url: url || `/pictures/${pictureId}`,
      }),
      keepUnusedDataFor: 60 * 60 * 60 * 24 * 30, // 30 days
    }),
    updatePicture: builder.mutation<void, PictureArgs>({
      query: ({ url, profilePicture, userId }) => {
        const formData = new FormData();
        formData.append('profilePicture', profilePicture);
        return {
          url: url || '/pictures',
          method: 'POST',
          body: formData,
        };
      },
      invalidatesTags: (result, error, { userId }) => [
        { type: 'Users', id: userId },
      ],
    }),
    updateUser: builder.mutation<void, UpdateUserArgs>({
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
    createUser: builder.mutation<void, CreateUserArgs>({
      query: (userInfo) => ({
        url: '/users',
        method: 'POST',
        body: JSON.stringify(userInfo),
        headers: {
          'Content-Type': 'application/vnd.apuntea.user-create-v1.0+json',
        },
      }),
      invalidatesTags: ['Users'],
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
