import { apiSlice } from './apiSlice';

export const notesApiSlice = apiSlice.injectEndpoints({
  endpoints: (builder) => ({
    getNote: builder.query({
      query: ({ noteId, url }) => url || `/notes/${noteId}`,
    }),
    getNoteFile: builder.query({
      query: ({ noteId, fileType, url }) => ({
        url: url || `/notes/${noteId}/file`,
        headers: {
          'content-type': fileType,
        },
      }),
    }),
    deleteNote: builder.mutation({
      query: ({ noteId, url }) => ({
        url: url || `/notes/${noteId}`,
        method: 'DELETE',
      }),
    }),
    updateNote: builder.mutation({
      query: ({ noteId, note, url }) => ({
        url: url || `/notes/${noteId}`,
        method: 'PATCH',
        body: note,
      }),
    }),
  }),
});

export const { useGetNoteQuery, useGetNoteFileQuery, useDeleteNoteMutation, useUpdateNoteMutation } = notesApiSlice;
