import { apiSlice } from "./apiSlice";

export const searchApiSlice = apiSlice.injectEndpoints({
  endpoints: builder => (
    {
      searchNotes: builder.query({
        query: ({ institutionId, careerId, subjectId, word, page, asc, sortBy, category, pageSize, parentId }) => {
          // Filtrar los parámetros que no son undefined
          const queryParams = {
            institutionId,
            careerId,
            subjectId,
            word,
            page,
            asc,
            sortBy,
            category,
            pageSize,
            parentId,
          };

          const filteredParams = Object.fromEntries(
            // eslint-disable-next-line no-unused-vars
            Object.entries(queryParams).filter(([_, value]) => value !== undefined)
          );
          const queryString = new URLSearchParams(filteredParams).toString();
          return `/notes?${queryString}`;
        },
        transformResponse: async (response, meta) => {
          const totalCount = meta.response.headers.get('X-Total-Count');
          const totalPages = meta.response.headers.get('X-Total-Pages');
          const notes = await response;

          return {
            notes,
            totalCount,
            totalPages,
          };
        }
      }),
    })
})

export const {
  useSearchNotesQuery,
} = searchApiSlice
