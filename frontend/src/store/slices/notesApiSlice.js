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
      query: ({ noteId, name, category, visible, url }) => {
        const data = {};
        if (name !== undefined) data['name'] = name;
        if (category !== undefined) data['category'] = category;
        if (visible !== undefined) data['visible'] = visible === 'true';

        console.log(data);
        return {
          url: url || `/notes/${noteId}`,
          method: 'PATCH',
          body: data,
        };
      },
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
    getLatestNotes: builder.query({
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
