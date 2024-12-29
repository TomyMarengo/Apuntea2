// store/slices/usersApiSlice.ts

import { apiSlice } from './apiSlice';
import { User, Career, Institution, UserStatus } from '../../types';
import { setCurrentUser } from './authSlice';
import { mapApiUser } from '../../utils/mappers';

interface GetUsersArgs {
  query?: string;
  status?: UserStatus;
  page?: number;
  pageSize?: number;
}

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
      query: ({ query, status, page = 1, pageSize = 10 }) => {
        const params = new URLSearchParams();
        if (query) params.append('query', query);
        if (status) params.append('status', status.toLowerCase());
        params.append('page', String(page));
        params.append('pageSize', String(pageSize));

        return `/users?${params.toString()}`;
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
      query: ({ userId, url }) => url || `/users/${userId}`,
      transformResponse: (response: any) => {
        return mapApiUser(response);
      },
      providesTags: (_result, _error, { userId }) => [
        { type: 'Users', id: userId },
      ],
    }),
    getLoggedUser: builder.query<User, UserArgs>({
      async queryFn({ userId, url }, queryApi, _extraOptions, baseQuery) {
        try {
          const userResult = await baseQuery({
            url: url || `/users/${userId}`,
          });
          if (userResult.error) return { error: userResult.error };

          const userData = userResult.data as any;

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

          // Combine data into a single user object
          const user: User = {
            id: userData.id,
            email: userData.email,
            firstName: userData.firstName,
            lastName: userData.lastName,
            username: userData.username,
            locale: userData.locale,
            status: userData.status,
            notificationsEnabled: userData.notificationsEnabled,
            profilePictureUrl: userData.profilePicture || '',
            selfUrl: userData.self,
            subjectFavoritesUrl: userData.subjectFavorites,
            noteFavoritesUrl: userData.noteFavorites,
            subjectsUrl: userData.subjects,
            reviewsReceivedUrl: userData.reviewsReceived,
            directoryFavoritesUrl: userData.directoryFavorites,
            followingUrl: userData.following,
            careerUrl: userData.career,
            institutionUrl: userData.institution,
            career: careerData,
            institution: institutionData,
            // Add other fields as necessary
          };

          queryApi.dispatch(setCurrentUser(user));

          return { data: user };
        } catch (error) {
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
    updatePicture: builder.mutation<boolean, PictureArgs>({
      queryFn: async (
        { url, profilePicture, userId },
        _api,
        _extraOptions,
        baseQuery,
      ) => {
        const formData = new FormData();
        formData.append('profilePicture', profilePicture);
        const response = await baseQuery({
          url: url || '/pictures',
          method: 'POST',
          body: formData,
        });
        return { data: response.error === undefined };
      },
      invalidatesTags: (_result, _error, { userId }) => [
        { type: 'Users', id: userId },
      ],
    }),
    updateUser: builder.mutation<boolean, UpdateUserArgs>({
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

        const response = await baseQuery({
          url: url || `/users/${userId}`,
          method: 'PATCH',
          body: JSON.stringify(data),
          headers: {
            'Content-Type': 'application/vnd.apuntea.user-update-v1.0+json',
          },
        });

        return { data: response.error === undefined };
      },
      invalidatesTags: (_result, _error, { userId }) => [
        { type: 'Users', id: userId },
      ],
    }),
    updateUserStatus: builder.mutation<boolean, UpdateUserStatusArgs>({
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

        const response = await baseQuery({
          url: url || `/users/${userId}`,
          method: 'PATCH',
          body: JSON.stringify(data),
          headers: {
            'Content-Type':
              'application/vnd.apuntea.user-update-status-v1.0+json',
          },
        });

        return { data: response.error === undefined };
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
          'Content-Type': 'application/vnd.apuntea.user-create-v1.0+json',
        },
      }),
      transformResponse: async (_response: any, meta: any) => {
        return { userUrl: meta.response.headers.get('Location') };
      },
      invalidatesTags: ['Users'],
    }),
    followUser: builder.mutation<boolean, FollowUserArgs>({
      queryFn: async ({ userId, url }, _api, _extraOptions, baseQuery) => {
        const result = await baseQuery({
          url: url || `/users/${userId}/followers`,
          method: 'POST',
        });
        return { data: result.error === undefined };
      },
      invalidatesTags: (_result, _error, { userId }) => [
        { type: 'Users', id: userId },
      ],
    }),
    unfollowUser: builder.mutation<boolean, UnfollowUserArgs>({
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

        return { data: result.error === undefined };
      },
      invalidatesTags: (_result, _error, { userId }) => [
        { type: 'Users', id: userId },
      ],
    }),
    isFollowingUser: builder.query<boolean, IsFollowingUser>({
      queryFn: async (
        { userId, followerId, url },
        _api,
        _extraOptions,
        baseQuery,
      ) => {
        const result = await baseQuery({
          url: url || `/users/${userId}/followers/${followerId}`,
        });
        return { data: result.error === undefined };
      },
    }),
    requestPasswordChange: builder.mutation<boolean, { email: string }>({
      query: ({ email }) => ({
        url: '/users',
        method: 'POST',
        body: JSON.stringify({ email }),
        headers: {
          'Content-Type':
            'application/vnd.apuntea.request-password-change-v1.0+json',
        },
      }),
    }),
    updateUserPassword: builder.mutation<boolean, UpdatePasswordArgs>({
      queryFn: async (
        { userId, password, email, code },
        _api,
        _extraOptions,
        baseQuery,
      ) => {
        const authorization = btoa(`${email}:${code}`);
        const response = await baseQuery({
          url: `/users/${userId}`,
          method: 'PATCH',
          body: JSON.stringify({ password }),
          headers: {
            'Content-Type':
              'application/vnd.apuntea.user-update-password-v1.0+json',
            Authorization: `Basic ${authorization}`,
          },
        });

        // Return true if no error occurred (indicating a successful request), false otherwise
        return { data: response.error === undefined };
      },
      invalidatesTags: (_result, _error, { userId }) => [
        { type: 'Users', id: userId },
      ],
    }),
    getUserByEmail: builder.query<User[], string>({
      query: (email) => ({ url: `/users?email=${encodeURIComponent(email)}` }),
      transformResponse: (response: any) => {
        return Array.isArray(response) ? response.map(mapApiUser) : [];
      },
    }),
  }),
});

export const {
  useGetUsersQuery,
  useGetUserQuery,
  useGetUserPictureQuery,
  useGetLoggedUserQuery,
  useLazyGetLoggedUserQuery,
  useUpdatePictureMutation,
  useUpdateUserMutation,
  useCreateUserMutation,
  useFollowUserMutation,
  useUnfollowUserMutation,
  useIsFollowingUserQuery,
  useUpdateUserStatusMutation,
  useLazyGetUserQuery,
  useLazyGetUserByEmailQuery,
  useRequestPasswordChangeMutation,
  useUpdateUserPasswordMutation,
} = usersApiSlice;
