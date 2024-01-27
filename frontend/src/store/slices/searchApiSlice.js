import { isUuid } from "../../functions/utils";
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

          if (queryParams.institutionId && isUuid(queryParams.institutionId)) {
            throw new Error('errors.institutionId');
          }

          if (queryParams.careerId && isUuid(queryParams.careerId)) {
            throw new Error('errors.careerId');
          }

          if (queryParams.subjectId && isUuid(queryParams.subjectId)) {
            throw new Error('errors.subjectId');
          }

          const filteredParams = Object.fromEntries(
            // eslint-disable-next-line no-unused-vars
            Object.entries(queryParams).filter(([_, value]) => value)
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
  useLazySearchNotesQuery,
} = searchApiSlice
