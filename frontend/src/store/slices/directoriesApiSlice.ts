// src/store/slices/directoriesApiSlice.ts

import { apiSlice } from './apiSlice';
import {
  DELETE_REASON_CONTENT_TYPE,
  DIRECTORY_CREATE_CONTENT_TYPE,
  DIRECTORY_UPDATE_CONTENT_TYPE,
} from '../../contentTypes.ts';
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
  directoryId?: string;
  name?: string;
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
  rdir?: string;
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
        // Remove # from iconColor if present
        if (iconColor && iconColor.startsWith('#')) {
          iconColor = iconColor.slice(1);
        }

        const result = await baseQuery({
          url: url || '/directories',
          method: 'POST',
          body: JSON.stringify({ name, parentId, visible, iconColor }),
          headers: {
            'Content-Type': DIRECTORY_CREATE_CONTENT_TYPE,
          },
        });
        return { data: result.error === undefined };
      },
      invalidatesTags: ['Directories'],
    }),
    updateDirectory: builder.mutation<boolean, UpdateDirectoryArgs>({
      queryFn: async (
        { directoryId, name, visible, iconColor, url },
        _api,
        _extraOptions,
        baseQuery,
      ) => {
        const body: any = {};
        if (name !== undefined) body.name = name;
        if (visible !== undefined) body.visible = visible;
        if (iconColor !== undefined && iconColor.startsWith('#'))
          body.iconColor = iconColor.slice(1);

        const result = await baseQuery({
          url: url || `/directories/${directoryId}`,
          method: 'PATCH',
          body: JSON.stringify(body),
          headers: {
            'Content-Type': DIRECTORY_UPDATE_CONTENT_TYPE,
          },
        });
        return { data: result.error === undefined };
      },
      invalidatesTags: (result, error, { directoryId }) => [
        { type: 'Directories', id: directoryId },
      ],
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

        let result;
        if (reason) {
          result = await baseQuery({
            url: url || `/directories/${directoryId}`,
            method: 'POST',
            body: JSON.stringify(body),
            headers: {
              'Content-Type': DELETE_REASON_CONTENT_TYPE,
            },
          });
        } else {
          result = await baseQuery({
            url: url || `/directories/${directoryId}`,
            method: 'DELETE',
          });
        }
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
      query: ({ userId, rdir, page = 1, pageSize = 10, url }) => {
        if (url) {
          return url;
        }
        const params = new URLSearchParams();
        params.append('favBy', userId);
        params.append('page', String(page));
        params.append('pageSize', String(pageSize));
        if (rdir) {
          params.append('rdir', rdir);
        }

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
  useLazyGetDirectoryQuery,
  useGetUserDirectoriesFavoritesQuery,
  useGetIsFavoriteDirectoryQuery,
  useAddFavoriteDirectoryMutation,
  useRemoveFavoriteDirectoryMutation,
} = directoriesApiSlice;
