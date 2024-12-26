// src/store/slices/directoriesApiSlice.ts

import { apiSlice } from './apiSlice';
import { Directory } from '../../types';

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
        const directory: Directory = {
          id: response.id,
          name: response.name,
          visible: response.visible,
          iconColor: response.iconColor,
          createdAt: response.createdAt,
          lastModifiedAt: response.lastModifiedAt,
          selfUrl: response.self,
          ownerUrl: response.owner,
          parentUrl: response.parent,
        };

        return directory;
      },
      providesTags: ['Directories'],
    }),
    getUserDirectoriesFavorites: builder.query<any, FavoritesArgs>({
      query: ({ userId, url }) => url || `/directories?favBy=${userId}`,
      providesTags: ['Directories'],
    }),
    getIsFavoriteDirectory: builder.query<any, FavoriteDirectoryArgs>({
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
    addFavoriteDirectory: builder.mutation<
      any,
      Omit<FavoriteDirectoryArgs, 'userId'>
    >({
      query: ({ directoryId, url }) => ({
        url: url || `/directories/${directoryId}/favorites`,
        method: 'POST',
      }),
    }),
    removeFavoriteDirectory: builder.mutation<any, FavoriteDirectoryArgs>({
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
