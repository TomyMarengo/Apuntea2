// src/store/slices/directoriesApiSlice.ts

import { apiSlice } from './apiSlice';
import { Directory } from '../../types';
import { mapApiDirectory } from '../../utils/mappers';

interface DirectoryQueryArgs {
  directoryId?: string;
  url?: string;
}

interface CreateDirectoryArgs {
  name: string;
  parentId: string;
  visible: boolean;
  iconColor: string;
  url?: string;
}

interface UpdateDirectoryArgs {
  name?: string;
  parentId?: string;
  visible?: boolean;
  iconColor?: string;
  url?: string;
}

interface DeleteDirectoryArgs {
  directoryId?: string;
  reason?: string;
  url?: string;
}

interface FavoritesArgs {
  userId: string;
  page?: number;
  pageSize?: number;
  url?: string;
}

interface PaginatedDirectoriesResponse {
  directories: Directory[];
  totalCount: number;
  totalPages: number;
}

interface FavoriteDirectoryArgs {
  directoryId?: string;
  userId?: string;
  url?: string;
  // If url is present, that takes precedence
  // Otherwise we do /directories/{directoryId}/favorites/{userId}
}

export const directoriesApiSlice = apiSlice.injectEndpoints({
  endpoints: (builder) => ({
    createDirectory: builder.mutation<boolean, CreateDirectoryArgs>({
      queryFn: async (
        { name, parentId, visible, iconColor, url },
        _api,
        _extraOptions,
        baseQuery,
      ) => {
        const result = await baseQuery({
          url: url || '/directories',
          method: 'POST',
          body: { name, parentId, visible, iconColor },
        });
        return { data: result.error === undefined };
      },
      invalidatesTags: ['Directories'],
    }),
    updateDirectory: builder.mutation<boolean, UpdateDirectoryArgs>({
      queryFn: async (
        { name, parentId, visible, iconColor, url },
        _api,
        _extraOptions,
        baseQuery,
      ) => {
        const body: any = {};
        if (name !== undefined) body.name = name;
        if (parentId !== undefined) body.parentId = parentId;
        if (visible !== undefined) body.visible = visible;
        if (iconColor !== undefined) body.iconColor = iconColor;

        const result = await baseQuery({
          url: url || `/directories/${parentId}`,
          method: 'PATCH',
          body,
        });
        return { data: result.error === undefined };
      },
      invalidatesTags: ['Directories'],
    }),
    deleteDirectory: builder.mutation<boolean, DeleteDirectoryArgs>({
      queryFn: async (
        { directoryId, reason, url },
        _api,
        _extraOptions,
        baseQuery,
      ) => {
        const body: any = {};
        if (reason !== undefined) body.reason = reason;

        const result = await baseQuery({
          url: url || `/directories/${directoryId}`,
          method: 'POST',
          body,
        });
        return { data: result.error === undefined };
      },
      invalidatesTags: ['Directories'],
    }),
    getDirectory: builder.query<Directory, DirectoryQueryArgs>({
      query: ({ directoryId, url }) => url || `/directories/${directoryId}`,
      transformResponse: (response: any) => {
        return mapApiDirectory(response);
      },
      providesTags: (result, error, { directoryId }) => [
        { type: 'Directories', id: directoryId },
      ],
    }),
    getUserDirectoriesFavorites: builder.query<
      PaginatedDirectoriesResponse,
      FavoritesArgs
    >({
      query: ({ userId, page = 1, pageSize = 10, url }) => {
        if (url) {
          return url;
        }
        const params = new URLSearchParams();
        params.append('favBy', userId);
        params.append('page', String(page));
        params.append('pageSize', String(pageSize));

        return `/directories?${params.toString()}`;
      },
      transformResponse: (
        response: any,
        meta: any,
      ): PaginatedDirectoriesResponse => {
        const totalCount = Number(
          meta.response.headers.get('X-Total-Count') || '0',
        );
        const totalPages = Number(
          meta.response.headers.get('X-Total-Pages') || '0',
        );
        const directories: Directory[] = Array.isArray(response)
          ? response.map(mapApiDirectory)
          : [];

        return {
          directories,
          totalCount,
          totalPages,
        };
      },
      providesTags: (result) =>
        result
          ? [
              ...result.directories.map(({ id }) => ({
                type: 'Directories' as const,
                id,
              })),
              { type: 'Directories', id: 'PARTIAL-LIST' },
            ]
          : [{ type: 'Directories', id: 'PARTIAL-LIST' }],
    }),
    getIsFavoriteDirectory: builder.query<boolean, FavoriteDirectoryArgs>({
      queryFn: async (
        { directoryId, userId, url },
        _queryApi,
        _extraOptions,
        baseQuery,
      ) => {
        const result = await baseQuery({
          url: url || `/directories/${directoryId}/favorites/${userId}`,
          method: 'GET',
        });
        return { data: result.error === undefined };
      },
    }),
    addFavoriteDirectory: builder.mutation<boolean, DirectoryQueryArgs>({
      queryFn: async ({ directoryId, url }, _api, _extraOptions, baseQuery) => {
        const result = await baseQuery({
          url: url || `/directories/${directoryId}/favorites`,
          method: 'POST',
        });
        return { data: result.error === undefined };
      },
    }),
    removeFavoriteDirectory: builder.mutation<boolean, FavoriteDirectoryArgs>({
      queryFn: async (
        { directoryId, userId, url },
        _api,
        _extraOptions,
        baseQuery,
      ) => {
        const result = await baseQuery({
          url: url || `/directories/${directoryId}/favorites/${userId}`,
          method: 'DELETE',
        });
        return { data: result.error === undefined };
      },
    }),
  }),
});

export const {
  useCreateDirectoryMutation,
  useUpdateDirectoryMutation,
  useDeleteDirectoryMutation,
  useGetDirectoryQuery,
  useGetUserDirectoriesFavoritesQuery,
  useGetIsFavoriteDirectoryQuery,
  useAddFavoriteDirectoryMutation,
  useRemoveFavoriteDirectoryMutation,
} = directoriesApiSlice;
