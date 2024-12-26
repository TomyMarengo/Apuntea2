// src/store/slices/directoriesApiSlice.ts

import { apiSlice } from './apiSlice';
import { Directory } from '../../types';
import { mapApiDirectory } from '../../utils/mappers';

interface DirectoryQueryArgs {
  directoryId?: string;
  url?: string;
}

interface FavoritesArgs {
  userId?: string;
  url?: string;
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
    getDirectory: builder.query<Directory, DirectoryQueryArgs>({
      query: ({ directoryId, url }) => url || `/directories/${directoryId}`,
      transformResponse: (response: any) => {
        return mapApiDirectory(response);
      },
      providesTags: (result, error, { directoryId }) => [
        { type: 'Directories', id: directoryId },
      ],
    }),
    getUserDirectoriesFavorites: builder.query<Directory[], FavoritesArgs>({
      query: ({ userId, url }) => url || `/directories?favBy=${userId}`,
      transformResponse: (response: any) => {
        const directories: Directory[] = Array.isArray(response)
          ? response.map(mapApiDirectory)
          : [];
        return directories;
      },
      providesTags: ['Directories'],
    }),
    getIsFavoriteDirectory: builder.query<boolean, FavoriteDirectoryArgs>({
      queryFn: async (
        { directoryId, userId, url },
        _queryApi,
        _extraOptions,
        baseQuery,
      ) => {
        const endpoint =
          url || `/directories/${directoryId}/favorites/${userId}`;
        const result = await baseQuery(endpoint);
        if (result.error) {
          if (result.error.status === 404) {
            return { data: false };
          }
          return { error: result.error };
        }
        return { data: true };
      },
    }),
    addFavoriteDirectory: builder.mutation<void, DirectoryQueryArgs>({
      query: ({ directoryId, url }) => ({
        url: url || `/directories/${directoryId}/favorites`,
        method: 'POST',
      }),
    }),
    removeFavoriteDirectory: builder.mutation<void, FavoriteDirectoryArgs>({
      query: ({ directoryId, userId, url }) => ({
        url: url || `/directories/${directoryId}/favorites/${userId}`,
        method: 'DELETE',
      }),
    }),
  }),
});

export const {
  useGetDirectoryQuery,
  useGetUserDirectoriesFavoritesQuery,
  useGetIsFavoriteDirectoryQuery,
  useAddFavoriteDirectoryMutation,
  useRemoveFavoriteDirectoryMutation,
} = directoriesApiSlice;
