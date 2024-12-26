// src/store/slices/notesApiSlice.ts

import { apiSlice } from './apiSlice';
import { Note, Category, FileType } from '../../types';
import { mapApiNote } from '../../utils/mappers';
interface NoteQueryArgs {
  noteId?: string;
  userId?: string;
  url?: string;
  fileType?: string;
}

interface NoteUpdateArgs {
  noteId?: string;
  name?: string;
  category?: Category;
  visible?: boolean;
  url?: string;
}

interface FavoriteNoteArgs {
  noteId?: string;
  userId?: string;
  url?: string;
}

interface UserFavoriteNotesArgs {
  userId?: string;
  url?: string;
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
    deleteNote: builder.mutation<void, NoteQueryArgs>({
      query: ({ noteId, url }) => ({
        url: url || `/notes/${noteId}`,
        method: 'DELETE',
      }),
      invalidatesTags: (result, error, { noteId }) => [
        { type: 'Notes', id: noteId },
      ],
    }),
    updateNote: builder.mutation<void, NoteUpdateArgs>({
      query: ({ noteId, name, category, visible, url }) => {
        const data: Record<string, any> = {};
        if (name !== undefined) data.name = name;
        if (category !== undefined) data.category = category;
        if (visible !== undefined) data.visible = visible === true;

        return {
          url: url || `/notes/${noteId}`,
          method: 'PATCH',
          headers: {
            'Content-Type': 'application/vnd.apuntea.note-update-v1.0+json',
          },
          body: JSON.stringify(data),
        };
      },
      invalidatesTags: (result, error, { noteId }) => [
        { type: 'Notes', id: noteId },
      ],
    }),
    getUserNotesFavorites: builder.query<Note[], UserFavoriteNotesArgs>({
      query: ({ userId, url }) => url || `/notes?favBy=${userId}`,
      transformResponse: (response: any) => {
        const notes: Note[] = Array.isArray(response)
          ? response.map(mapApiNote)
          : [];
        return notes;
      },
      providesTags: ['Notes'],
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
        if (result.error) {
          if (result.error.status === 404) {
            return { data: false };
          }
          return { error: result.error };
        }
        return { data: true };
      },
    }),
    addFavoriteNote: builder.mutation<void, NoteQueryArgs>({
      query: ({ noteId, url }) => ({
        url: url || `/notes/${noteId}/favorites`,
        method: 'POST',
      }),
    }),
    removeFavoriteNote: builder.mutation<void, FavoriteNoteArgs>({
      query: ({ noteId, userId, url }) => ({
        url: url || `/notes/${noteId}/favorites/${userId}`,
        method: 'DELETE',
      }),
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
  useDeleteNoteMutation,
  useUpdateNoteMutation,
  useGetUserNotesFavoritesQuery,
  useGetIsFavoriteNoteQuery,
  useAddFavoriteNoteMutation,
  useRemoveFavoriteNoteMutation,
  useGetLatestNotesQuery,
} = notesApiSlice;
