// src/store/slices/usersApiSlice.ts

import { ApiResponse, apiSlice } from './apiSlice';
import { setCurrentUser } from './authSlice';
import { setLocale } from './languageSlice';
import {
  USER_COLLECTION_CONTENT_TYPE,
  USER_CONTENT_TYPE,
  USER_CREATE_CONTENT_TYPE,
  USER_EMAIL_COLLECTION_CONTENT_TYPE,
  USER_REQUEST_PASSWORD_CHANGE_CONTENT_TYPE,
  USER_UPDATE_CONTENT_TYPE,
  USER_UPDATE_PASSWORD_CONTENT_TYPE,
  USER_UPDATE_STATUS_CONTENT_TYPE,
} from '../../contentTypes.ts';
import { User, Career, Institution, UserStatus } from '../../types';
import { extractErrorMessages } from '../../utils/helpers';
import { mapApiUser } from '../../utils/mappers';

interface GetUsersArgs {
  url?: string;
  query?: string;
  followedBy?: string;
  following?: string;
  status?: UserStatus;
  page?: number;
  pageSize?: number;
}

interface UserArgs {
  userData?: any;
  userId?: string;
  url?: string;
}

export interface UpdateUserArgs {
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
  url?: string;
}

interface FollowUserArgs {
  userId: string;
  url?: string;
}

interface UnfollowUserArgs {
  userId: string;
  followerId: string;
  url?: string;
}

interface IsFollowingUser {
  userId: string;
  followerId: string;
  url?: string;
}

interface UpdateUserStatusArgs {
  userId: string;
  status: UserStatus;
  banReason?: string;
  url?: string;
}

interface UpdatePasswordArgs {
  userId: string;
  password: string;
  email: string;
  code: string;
}

export const usersApiSlice = apiSlice.injectEndpoints({
  endpoints: (builder) => ({
    getUsers: builder.query<
      {
        users: User[];
        totalCount: number;
        totalPages: number;
      },
      GetUsersArgs
    >({
      query: ({ url, query, status, page = 1, pageSize = 10 }) => {
        if (url) return url;

        const params = new URLSearchParams();
        if (query) params.append('query', query);
        if (status) params.append('status', status.toLowerCase());
        params.append('page', String(page));
        params.append('pageSize', String(pageSize));

        return {
          url: `/users?${params.toString()}`,
          headers: {
            Accept: USER_EMAIL_COLLECTION_CONTENT_TYPE,
          },
        };
      },
      transformResponse: (response: any, meta: any) => {
        const totalCount = Number(
          meta.response.headers.get('X-Total-Count') || '0',
        );
        const totalPages = Number(
          meta.response.headers.get('X-Total-Pages') || '0',
        );
        const users: User[] = Array.isArray(response)
          ? response.map(mapApiUser)
          : [];

        return {
          users,
          totalCount,
          totalPages,
        };
      },
      providesTags: (result) =>
        result
          ? [
              ...result.users.map(({ id }) => ({ type: 'Users' as const, id })),
              { type: 'Users', id: 'PARTIAL-LIST' },
            ]
          : [{ type: 'Users', id: 'PARTIAL-LIST' }],
    }),
    getUser: builder.query<User, UserArgs>({
      query: ({ userId, url }) => ({
        url: url || `/users/${userId}`,
        headers: {
          Accept: USER_CONTENT_TYPE,
        },
      }),
      transformResponse: (response: any) => {
        return mapApiUser(response);
      },
      providesTags: (_result, _error, { userId }) => [
        { type: 'Users', id: userId },
      ],
    }),
    getLoggedUser: builder.query<User, UserArgs>({
      async queryFn(
        { userData, userId, url },
        queryApi,
        _extraOptions,
        baseQuery,
      ) {
        try {
          if (!userData) {
            const userResult = await baseQuery({
              url: url || `/users/${userId}`,
              headers: {
                Accept: USER_CONTENT_TYPE,
              },
            });
            if (userResult.error) return { error: userResult.error };
            userData = userResult.data as any;
          }

          let careerData: Career | undefined;
          if (userData.career) {
            const careerResult = await baseQuery({ url: userData.career });
            if (!careerResult.error) {
              careerData = careerResult.data as Career;
            }
          }

          let institutionData: Institution | undefined;
          if (userData.institution) {
            const institutionResult = await baseQuery({
              url: userData.institution,
            });
            if (!institutionResult.error) {
              institutionData = institutionResult.data as Institution;
            }
          }

          // Map user data and then add career and institution fields
          const mappedUser = mapApiUser(userData);
          const user: User = {
            ...mappedUser,
            career: careerData,
            institution: institutionData,
          };

          queryApi.dispatch(setCurrentUser(user));
          if (user.locale) queryApi.dispatch(setLocale(user.locale));

          return { data: user };
        } catch (error) {
          console.error('Failed to fetch user data:', error);
          return {
            error: {
              status: 'CUSTOM_ERROR',
              error: 'Failed to fetch user data',
            },
          };
        }
      },
      providesTags: (_result, _error, { userId }) => [
        { type: 'Users', id: userId },
      ],
    }),
    getUserPicture: builder.query<any, PictureArgs>({
      query: ({ pictureId, url }) => ({
        url: url || `/pictures/${pictureId}`,
      }),
      keepUnusedDataFor: 60 * 60 * 60 * 24 * 30, // 30 days
    }),
    updatePicture: builder.mutation<ApiResponse, PictureArgs>({
      queryFn: async (
        { url, profilePicture },
        _api,
        _extraOptions,
        baseQuery,
      ) => {
        const formData = new FormData();
        formData.append('profilePicture', profilePicture);
        const result = await baseQuery({
          url: url || '/pictures',
          method: 'POST',
          body: formData,
        });
        let errorMessages = extractErrorMessages(result.error);

        return {
          data: {
            success: result.error === undefined,
            messages: errorMessages,
          },
        };
      },
      invalidatesTags: (_result, _error, { userId }) => [
        { type: 'Users', id: userId },
      ],
    }),
    updateUser: builder.mutation<ApiResponse, UpdateUserArgs>({
      queryFn: async (
        {
          userId,
          firstName,
          lastName,
          username,
          careerId,
          password,
          notificationsEnabled,
          oldPassword,
          url,
        },
        _api,
        _extraOptions,
        baseQuery,
      ) => {
        const data: Record<string, any> = {};

        if (firstName !== undefined) data.firstName = firstName;
        if (lastName !== undefined) data.lastName = lastName;
        if (username !== undefined) data.username = username;
        if (careerId !== undefined) data.careerId = careerId;

        if (password !== undefined && oldPassword !== undefined) {
          data.password = password;
          data.oldPassword = oldPassword;
        }
        if (notificationsEnabled !== undefined) {
          data.notificationsEnabled = notificationsEnabled;
        }

        const result = await baseQuery({
          url: url || `/users/${userId}`,
          method: 'PATCH',
          body: JSON.stringify(data),
          headers: {
            'Content-Type': USER_UPDATE_CONTENT_TYPE,
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
      invalidatesTags: (_result, _error, { userId }) => [
        { type: 'Users', id: userId },
      ],
    }),
    updateUserStatus: builder.mutation<ApiResponse, UpdateUserStatusArgs>({
      queryFn: async (
        { userId, status, banReason, url },
        _api,
        _extraOptions,
        baseQuery,
      ) => {
        const data: Record<string, any> = { status };

        if (banReason !== undefined && status === UserStatus.BANNED) {
          data.reason = banReason;
        }

        data.status = status.toLowerCase();

        const result = await baseQuery({
          url: url || `/users/${userId}`,
          method: 'PATCH',
          body: JSON.stringify(data),
          headers: {
            'Content-Type': USER_UPDATE_STATUS_CONTENT_TYPE,
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
      invalidatesTags: (_result, _error, { userId }) => [
        { type: 'Users', id: userId },
      ],
    }),
    createUser: builder.mutation<{ userUrl: string }, CreateUserArgs>({
      query: (userInfo) => ({
        url: '/users',
        method: 'POST',
        body: JSON.stringify(userInfo),
        headers: {
          'Content-Type': USER_CREATE_CONTENT_TYPE,
        },
      }),
      transformResponse: async (_response: any, meta: any) => {
        return { userUrl: meta.response.headers.get('Location') };
      },
      invalidatesTags: ['Users'],
    }),
    getFollowers: builder.query<
      {
        users: User[];
        totalCount: number;
        totalPages: number;
      },
      GetUsersArgs
    >({
      query: ({ url, page, pageSize }) => {
        const params = new URLSearchParams();
        params.append('page', String(page));
        params.append('pageSize', String(pageSize));

        return {
          url: url + `&${params.toString()}`,
          headers: {
            Accept: USER_COLLECTION_CONTENT_TYPE,
          },
        };
      },
      transformResponse: (response: any, meta: any) => {
        const totalCount = Number(
          meta.response.headers.get('X-Total-Count') || '0',
        );
        const totalPages = Number(
          meta.response.headers.get('X-Total-Pages') || '0',
        );
        const users: User[] = Array.isArray(response)
          ? response.map(mapApiUser)
          : [];

        return {
          users,
          totalCount,
          totalPages,
        };
      },
    }),
    getFollowings: builder.query<
      {
        users: User[];
        totalCount: number;
        totalPages: number;
      },
      GetUsersArgs
    >({
      query: ({ url, page, pageSize }) => {
        const params = new URLSearchParams();
        params.append('page', String(page));
        params.append('pageSize', String(pageSize));

        return {
          url: url + `&${params.toString()}`,
          headers: {
            Accept: USER_COLLECTION_CONTENT_TYPE,
          },
        };
      },
      transformResponse: (response: any, meta: any) => {
        const totalCount = Number(
          meta.response.headers.get('X-Total-Count') || '0',
        );
        const totalPages = Number(
          meta.response.headers.get('X-Total-Pages') || '0',
        );
        const users: User[] = Array.isArray(response)
          ? response.map(mapApiUser)
          : [];

        return {
          users,
          totalCount,
          totalPages,
        };
      },
    }),
    followUser: builder.mutation<ApiResponse, FollowUserArgs>({
      queryFn: async ({ userId, url }, _api, _extraOptions, baseQuery) => {
        const result = await baseQuery({
          url: url || `/users/${userId}/followers`,
          method: 'POST',
        });
        return {
          data: {
            success: result.error === undefined,
            messages: [],
          },
        };
      },
      invalidatesTags: (_result, _error, { userId }) => [
        { type: 'Followers', id: userId },
      ],
    }),
    unfollowUser: builder.mutation<ApiResponse, UnfollowUserArgs>({
      queryFn: async (
        { userId, followerId, url },
        _api,
        _extraOptions,
        baseQuery,
      ) => {
        const result = await baseQuery({
          url: url || `/users/${userId}/followers/${followerId}`,
          method: 'DELETE',
        });

        return {
          data: {
            success: result.error === undefined,
            messages: [],
          },
        };
      },
      invalidatesTags: (_result, _error, { userId }) => [
        { type: 'Followers', id: userId },
      ],
    }),
    isFollowingUser: builder.query<ApiResponse, IsFollowingUser>({
      queryFn: async (
        { userId, followerId, url },
        _api,
        _extraOptions,
        baseQuery,
      ) => {
        const result = await baseQuery({
          url: url || `/users/${userId}/followers/${followerId}`,
        });
        return {
          data: {
            success: result.error === undefined,
            messages: [],
          },
        };
      },
      providesTags: (_result, _error, { userId }) => [
        { type: 'Followers', id: userId },
      ],
    }),
    requestPasswordChange: builder.mutation<boolean, { email: string }>({
      queryFn: async ({ email }, _queryApi, _extraOptions, baseQuery) => {
        try {
          const result = await baseQuery({
            url: '/users',
            method: 'POST',
            body: JSON.stringify({ email }),
            headers: {
              'Content-Type': USER_REQUEST_PASSWORD_CHANGE_CONTENT_TYPE,
            },
          });
          if (result.error) {
            return { data: false };
          }
          return { data: true };
        } catch (error) {
          console.error('Failed to request password change:', error);
          return { data: false };
        }
      },
    }),

    updateUserPassword: builder.mutation<ApiResponse, UpdatePasswordArgs>({
      queryFn: async (
        { userId, password, email, code },
        _api,
        _extraOptions,
        baseQuery,
      ) => {
        const authorization = btoa(`${email}:${code}`);
        const result = await baseQuery({
          url: `/users/${userId}`,
          method: 'PATCH',
          body: JSON.stringify({ password }),
          headers: {
            'Content-Type': USER_UPDATE_PASSWORD_CONTENT_TYPE,
            Authorization: `Basic ${authorization}`,
          },
        });

        let errorMessages = extractErrorMessages(result.error);
        console.log('result!!', result);

        return {
          data: {
            success: result.error === undefined,
            messages: errorMessages,
          },
        };
      },
      invalidatesTags: (_result, _error, { userId }) => [
        { type: 'Users', id: userId },
      ],
    }),
    getUserByEmail: builder.query<User[], string>({
      query: (email) => ({
        url: `/users?email=${encodeURIComponent(email)}`,
        headers: {
          Accept: USER_COLLECTION_CONTENT_TYPE,
        },
      }),
      transformResponse: (response: any) => {
        return Array.isArray(response) ? response.map(mapApiUser) : [];
      },
    }),
  }),
});

export const {
  useGetUsersQuery,
  useLazyGetUsersQuery,
  useGetUserQuery,
  useGetUserPictureQuery,
  useGetLoggedUserQuery,
  useLazyGetLoggedUserQuery,
  useUpdatePictureMutation,
  useUpdateUserMutation,
  useCreateUserMutation,
  useGetFollowersQuery,
  useLazyGetFollowersQuery,
  useGetFollowingsQuery,
  useLazyGetFollowingsQuery,
  useFollowUserMutation,
  useUnfollowUserMutation,
  useIsFollowingUserQuery,
  useUpdateUserStatusMutation,
  useLazyGetUserQuery,
  useLazyGetUserByEmailQuery,
  useRequestPasswordChangeMutation,
  useUpdateUserPasswordMutation,
} = usersApiSlice;
