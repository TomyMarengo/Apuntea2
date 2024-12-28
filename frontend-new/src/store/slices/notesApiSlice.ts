// src/store/slices/notesApiSlice.ts

import { apiSlice } from './apiSlice';
import { Note, Category } from '../../types';
import { mapApiNote } from '../../utils/mappers';

interface NoteQueryArgs {
  noteId?: string;
  userId?: string;
  url?: string;
}

interface CreateNoteArgs {
  name: string;
  parentId: string;
  visible: boolean;
  file: File;
  category: Category;
  url?: string;
}

interface UpdateNoteArgs {
  noteId?: string;
  name?: string;
  visible?: boolean;
  category?: Category;
  url?: string;
}

interface DeleteNoteArgs {
  noteId?: string;
  reason?: string;
  url?: string;
}

interface FavoriteNoteArgs {
  noteId?: string;
  userId?: string;
  url?: string;
}

interface GetUserNotesFavoritesArgs {
  userId: string;
  page?: number;
  pageSize?: number;
  url?: string;
}

interface PaginatedNotesResponse {
  notes: Note[];
  totalCount: number;
  totalPages: number;
}

export const notesApiSlice = apiSlice.injectEndpoints({
  endpoints: (builder) => ({
    getNote: builder.query<Note, NoteQueryArgs>({
      query: ({ noteId, url }) => url || `/notes/${noteId}`,
      transformResponse: (response: any) => {
        return mapApiNote(response);
      },
      providesTags: (result, error, { noteId }) => [
        { type: 'Notes', id: noteId },
      ],
    }),
    createNote: builder.mutation<boolean, CreateNoteArgs>({
      queryFn: async (
        { name, parentId, visible, file, category },
        _api,
        _extraOptions,
        baseQuery,
      ) => {
        const result = await baseQuery({
          url: '/notes',
          method: 'POST',
          body: { name, parentId, visible, file, category },
          headers: {
            'Content-Type': 'multipart/form-data',
          },
        });
        return { data: result.error === undefined };
      },
      invalidatesTags: ['Directories'],
    }),
    updateNote: builder.mutation<boolean, UpdateNoteArgs>({
      queryFn: async (
        { noteId, name, visible, category, url },
        _api,
        _extraOptions,
        baseQuery,
      ) => {
        const body: any = {};
        if (name !== undefined) body.name = name;
        if (visible !== undefined) body.visible = visible;
        if (category !== undefined) body.category = category;

        const result = await baseQuery({
          url: url || `/notes/${noteId}`,
          method: 'PATCH',
          body,
        });
        return { data: result.error === undefined };
      },
      invalidatesTags: ['Notes'],
    }),
    deleteNote: builder.mutation<boolean, DeleteNoteArgs>({
      queryFn: async (
        { noteId, reason, url },
        _api,
        _extraOptions,
        baseQuery,
      ) => {
        const body: any = {};
        if (reason !== undefined) body.reason = reason;

        const result = await baseQuery({
          url: url || `/notes/${noteId}`,
          method: 'POST',
          body,
        });
        return { data: result.error === undefined };
      },
      invalidatesTags: ['Notes'],
    }),
    getUserNotesFavorites: builder.query<
      PaginatedNotesResponse,
      GetUserNotesFavoritesArgs
    >({
      query: ({ userId, page = 1, pageSize = 10, url }) => {
        if (url) {
          return url;
        }
        const params = new URLSearchParams();
        params.append('favBy', userId);
        params.append('page', String(page));
        params.append('pageSize', String(pageSize));

        return `/notes?${params.toString()}`;
      },
      transformResponse: (response: any, meta: any): PaginatedNotesResponse => {
        const totalCount = Number(
          meta.response.headers.get('X-Total-Count') || '0',
        );
        const totalPages = Number(
          meta.response.headers.get('X-Total-Pages') || '0',
        );
        const notes: Note[] = Array.isArray(response)
          ? response.map(mapApiNote)
          : [];

        return {
          notes,
          totalCount,
          totalPages,
        };
      },
      providesTags: (result) =>
        result
          ? [
              ...result.notes.map(({ id }) => ({ type: 'Notes' as const, id })),
              { type: 'Notes', id: 'PARTIAL-LIST' },
            ]
          : [{ type: 'Notes', id: 'PARTIAL-LIST' }],
    }),
    getIsFavoriteNote: builder.query<boolean, FavoriteNoteArgs>({
      queryFn: async (
        { noteId, userId, url },
        _queryApi,
        _extraOptions,
        baseQuery,
      ) => {
        const endpoint = url || `/notes/${noteId}/favorites/${userId}`;
        const result = await baseQuery(endpoint);
        return { data: result.error === undefined };
      },
    }),
    addFavoriteNote: builder.mutation<boolean, NoteQueryArgs>({
      queryFn: async ({ noteId, url }, _api, _extraOptions, baseQuery) => {
        const result = await baseQuery({
          url: url || `/notes/${noteId}/favorites`,
          method: 'POST',
        });
        return { data: result.error === undefined };
      },
    }),
    removeFavoriteNote: builder.mutation<boolean, FavoriteNoteArgs>({
      queryFn: async (
        { noteId, userId, url },
        _api,
        _extraOptions,
        baseQuery,
      ) => {
        const result = await baseQuery({
          url: url || `/notes/${noteId}/favorites/${userId}`,
          method: 'DELETE',
        });
        return { data: result.error === undefined };
      },
    }),
    addInteractionNote: builder.mutation<boolean, FavoriteNoteArgs>({
      queryFn: async (
        { noteId, userId, url },
        _api,
        _extraOptions,
        baseQuery,
      ) => {
        const result = await baseQuery({
          url: url || `/notes/${noteId}/interactions/${userId}`,
          method: 'POST',
        });
        return { data: result.error === undefined };
      },
    }),
    getLatestNotes: builder.query<Note[], NoteQueryArgs>({
      query: ({ userId, url }) => url || `/notes?user=${userId}&sortBy=date`,
      transformResponse: (response: any) => {
        const notes: Note[] = Array.isArray(response)
          ? response.map(mapApiNote)
          : [];
        return notes;
      },
      providesTags: ['Notes'],
    }),
  }),
});

export const {
  useGetNoteQuery,
  useCreateNoteMutation,
  useUpdateNoteMutation,
  useDeleteNoteMutation,
  useGetUserNotesFavoritesQuery,
  useGetIsFavoriteNoteQuery,
  useAddFavoriteNoteMutation,
  useRemoveFavoriteNoteMutation,
  useAddInteractionNoteMutation,
  useGetLatestNotesQuery,
} = notesApiSlice;
