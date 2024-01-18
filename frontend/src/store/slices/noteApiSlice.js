import { apiSlice } from './apiSlice';

export const noteApiSlice = apiSlice.injectEndpoints({
    endpoints: builder => ({
        getNote: builder.query({
            query: (noteId) => `/notes/${noteId}`, 
            transformResponse: async (response) => {
                const note = await response;
                return {note};
              } 
        }),
        // getNoteFile: builder.query({
        //     query: (noteId, fileType) => ({
        //         url: `/notes/${noteId}/file`,
        //         method: 'GET',
        //         headers: {
        //             'content-type': fileType,
        //         }
        //     }),
        //     transformResponse: async (response) => {
        //         const noteFile = await response;
        //         return {noteFile};
        //     },
        //     responseHandler: 'text'
        // }),
        deleteNote: builder.mutation({
            query: noteId => ({
                url: `/notes/${noteId}`,
                method: 'DELETE',
            }),
        }),
        updateNote: builder.mutation({
            query: ({ noteId, note }) => ({
                url: `/notes/${noteId}`,
                method: 'PATCH',
                body: note,
            }),
        })
    })
})

export const {
    useGetNoteQuery,
    useGetNoteFileQuery,
    useDeleteNoteMutation,
    useUpdateNoteMutation,
} = noteApiSlice;