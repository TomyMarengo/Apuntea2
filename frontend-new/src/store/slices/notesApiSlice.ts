// src/store/slices/notesApiSlice.ts

import { apiSlice } from './apiSlice';
import { Note, Category, FileType } from '../../types';

interface NoteQueryArgs {
  noteId?: string;
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

interface FavoritesArgs {
  noteId?: string;
  userId?: string;
  url?: string;
}

export const notesApiSlice = apiSlice.injectEndpoints({
  endpoints: (builder) => ({
    getNote: builder.query<Note, NoteQueryArgs>({
      query: ({ noteId, url }) => url || `/notes/${noteId}`,
      transformResponse: (response: any) => {
        const note: Note = {
          id: response.id,
          name: response.name,
          visible: response.visible,
          fileType: response.fileType as FileType,
          category: response.category as Category,
          avgScore: response.avgScore,
          createdAt: response.createdAt,
          lastModifiedAt: response.lastModifiedAt,
          selfUrl: response.self,
          fileUrl: response.file,
          interactions: response.interactions,
          interactionsUrl: response.interactionsUri,
          ownerUrl: response.owner,
          parentUrl: response.parent,
          reviewsUrl: response.reviews,
          subjectUrl: response.subject,
        };

        return note;
      },
      providesTags: (result, error, { noteId }) => [
        { type: 'Notes', id: noteId },
      ],
    }),
    getNoteFile: builder.query<any, NoteQueryArgs>({
      query: ({ noteId, fileType, url }) => ({
        url: url || `/notes/${noteId}/file`,
        headers: {
          'content-type': fileType || '',
        },
      }),
      keepUnusedDataFor: 2592000, // 30 days
    }),
    deleteNote: builder.mutation<any, { noteId?: string; url?: string }>({
      query: ({ noteId, url }) => ({
        url: url || `/notes/${noteId}`,
        method: 'DELETE',
      }),
      invalidatesTags: ['Notes'],
    }),
    updateNote: builder.mutation<any, NoteUpdateArgs>({
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
      invalidatesTags: ['Notes'],
    }),
    getUserNotesFavorites: builder.query<
      any,
      { userId?: string; url?: string }
    >({
      query: ({ userId, url }) => url || `/notes?favBy=${userId}`,
      providesTags: ['Notes'],
    }),
    getIsFavoriteNote: builder.query<any, FavoritesArgs>({
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
    addFavoriteNote: builder.mutation<any, { noteId?: string; url?: string }>({
      query: ({ noteId, url }) => ({
        url: url || `/notes/${noteId}/favorites`,
        method: 'POST',
      }),
    }),
    removeFavoriteNote: builder.mutation<any, FavoritesArgs>({
      query: ({ noteId, userId, url }) => ({
        url: url || `/notes/${noteId}/favorites/${userId}`,
        method: 'DELETE',
      }),
    }),
    getLatestNotes: builder.query<any, { userId?: string; url?: string }>({
      query: ({ userId, url }) => url || `/notes?user=${userId}&sortBy=date`,
    }),
  }),
});

export const {
  useGetNoteQuery,
  useGetNoteFileQuery,
  useDeleteNoteMutation,
  useUpdateNoteMutation,
  useGetUserNotesFavoritesQuery,
  useGetIsFavoriteNoteQuery,
  useAddFavoriteNoteMutation,
  useRemoveFavoriteNoteMutation,
  useGetLatestNotesQuery,
} = notesApiSlice;
