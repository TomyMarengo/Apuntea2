// src/store/slices/notesApiSlice.ts

import { ApiResponse, apiSlice } from './apiSlice';
import {
  DELETE_REASON_CONTENT_TYPE,
  NOTE_UPDATE_CONTENT_TYPE,
} from '../../contentTypes.ts';
import { Note, NoteCategory } from '../../types';
import { extractErrorMessages } from '../../utils/helpers';
import { mapApiNote } from '../../utils/mappers';

interface NoteQueryArgs {
  noteId?: string;
  userId?: string;
  url?: string;
}

interface NoteFileArgs {
  noteId: string;
  url?: string;
}

interface CreateNoteArgs {
  name: string;
  parentId: string;
  visible: boolean;
  file: File;
  category: NoteCategory;
  url?: string;
}

interface UpdateNoteArgs {
  noteId?: string;
  name?: string;
  visible?: boolean;
  category?: NoteCategory;
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
      providesTags: (_result, _error, { noteId }) => [
        { type: 'Notes', id: noteId },
      ],
    }),
    getNoteFile: builder.query<Blob, NoteFileArgs>({
      query: ({ noteId, url }) => ({
        url: url || `/notes/${noteId}/file`,
        responseHandler: (res) => res.blob(),
      }),
    }),
    createNote: builder.mutation<ApiResponse, CreateNoteArgs>({
      queryFn: async (
        { name, parentId, visible, file, category },
        _api,
        _extraOptions,
        baseQuery,
      ) => {
        const formData = new FormData();
        formData.append('name', name);
        formData.append('parentId', parentId);
        formData.append('visible', String(visible));
        formData.append('category', category);

        if (file) {
          formData.append('file', file);
        }

        const result = await baseQuery({
          url: '/notes',
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
      invalidatesTags: ['Notes'],
    }),
    updateNote: builder.mutation<ApiResponse, UpdateNoteArgs>({
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
          body: JSON.stringify(body),
          headers: {
            'Content-Type': NOTE_UPDATE_CONTENT_TYPE,
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
      invalidatesTags: (_result, _error, { noteId }) => [
        { type: 'Notes', id: noteId },
      ],
    }),

    deleteNote: builder.mutation<ApiResponse, DeleteNoteArgs>({
      queryFn: async (
        { noteId, reason, url },
        _api,
        _extraOptions,
        baseQuery,
      ) => {
        const body: any = {};
        if (reason !== undefined) body.reason = reason;

        let result;
        if (reason) {
          result = await baseQuery({
            url: url || `/notes/${noteId}`,
            method: 'POST',
            body: JSON.stringify(body),
            headers: {
              'Content-Type': DELETE_REASON_CONTENT_TYPE,
            },
          });
        } else {
          result = await baseQuery({
            url: url || `/notes/${noteId}`,
            method: 'DELETE',
          });
        }
        return {
          data: {
            success: result.error === undefined,
            messages: [],
          },
        };
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
    getIsFavoriteNote: builder.query<ApiResponse, FavoriteNoteArgs>({
      queryFn: async (
        { noteId, userId, url },
        _queryApi,
        _extraOptions,
        baseQuery,
      ) => {
        const endpoint = url || `/notes/${noteId}/favorites/${userId}`;
        const result = await baseQuery(endpoint);
        return {
          data: {
            success: result.error === undefined,
            messages: [],
          },
        };
      },
    }),
    addFavoriteNote: builder.mutation<ApiResponse, NoteQueryArgs>({
      queryFn: async ({ noteId, url }, _api, _extraOptions, baseQuery) => {
        const result = await baseQuery({
          url: url || `/notes/${noteId}/favorites`,
          method: 'POST',
        });
        return {
          data: {
            success: result.error === undefined,
            messages: [],
          },
        };
      },
    }),
    removeFavoriteNote: builder.mutation<ApiResponse, FavoriteNoteArgs>({
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
        return {
          data: {
            success: result.error === undefined,
            messages: [],
          },
        };
      },
    }),
    addInteractionNote: builder.mutation<ApiResponse, FavoriteNoteArgs>({
      queryFn: async (
        { noteId, userId, url },
        _api,
        _extraOptions,
        baseQuery,
      ) => {
        const result = await baseQuery({
          url: url || `/notes/${noteId}/interactions/${userId}`,
          method: 'PUT',
        });
        return {
          data: {
            success: result.error === undefined,
            messages: [],
          },
        };
      },
    }),
    getLatestNotes: builder.query<Note[], NoteQueryArgs>({
      query: ({ userId, url }) => url || `/notes?userId=${userId}&sortBy=date`,
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
  useGetNoteFileQuery,
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
