import { apiSlice } from './apiSlice';

export const notesApiSlice = apiSlice.injectEndpoints({
  endpoints: (builder) => ({
    getNote: builder.query({
      query: ({ noteId, url }) => url || `/notes/${noteId}`,
      providesTags: ['Notes'],
      refetchOnMountOrArgChange: true,
    }),
    getNoteFile: builder.query({
      query: ({ noteId, fileType, url }) => ({
        url: url || `/notes/${noteId}/file`,
        headers: {
          'content-type': fileType,
        },
      }),
      keepUnusedDataFor: 30 * 24 * 60 * 60, // 30 days
    }),
    deleteNote: builder.mutation({
      query: ({ noteId, url }) => ({
        url: url || `/notes/${noteId}`,
        method: 'DELETE',
      }),
      invalidatesTags: ['Notes'],
    }),
    updateNote: builder.mutation({
      query: ({ noteId, note, url }) => ({
        url: url || `/notes/${noteId}`,
        method: 'PATCH',
        body: note,
      }),
      invalidatesTags: ['Notes'],
    }),
    getUserNotesFavorites: builder.query({
      query: ({ userId, url }) => url || `/notes?favBy=${userId}`,
      providesTags: ['Notes'],
    }),
    getIsFavoriteNote: builder.query({
      query: ({ noteId, userId, url }) => url || `/notes/${noteId}/favorites/${userId}`,
    }),
    addFavoriteNote: builder.mutation({
      query: ({ noteId, url }) => ({
        url: url || `/notes/${noteId}/favorites`,
        method: 'POST',
      }),
    }),
    removeFavoriteNote: builder.mutation({
      query: ({ noteId, userId, url }) => ({
        url: url || `/notes/${noteId}/favorites/${userId}`,
        method: 'DELETE',
      }),
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
} = notesApiSlice;
