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
            pageSize,
            parentId,
          };

          if (category && category !== 'note') {
            queryParams.category = category;
          }

          const filteredParams = Object.fromEntries(
            // eslint-disable-next-line no-unused-vars
            Object.entries(queryParams).filter(([_, value]) => value)
          );

          if (filteredParams.institutionId && !isUuid(filteredParams.institutionId)) {
            throw new Error('errors.institutionId');
          }

          if (filteredParams.careerId && !isUuid(filteredParams.careerId)) {
            throw new Error('errors.careerId');
          }

          if (filteredParams.subjectId && !isUuid(filteredParams.subjectId)) {
            throw new Error('errors.subjectId');
          }

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
        },
      }),
      searchDirectories: builder.query({
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
            pageSize,
            parentId,
          };

          const filteredParams = Object.fromEntries(
            // eslint-disable-next-line no-unused-vars
            Object.entries(queryParams).filter(([_, value]) => value)
          );

          if (filteredParams.institutionId && !isUuid(filteredParams.institutionId)) {
            throw new Error('errors.institutionId');
          }

          if (filteredParams.careerId && !isUuid(filteredParams.careerId)) {
            throw new Error('errors.careerId');
          }

          if (filteredParams.subjectId && !isUuid(filteredParams.subjectId)) {
            throw new Error('errors.subjectId');
          }

          if (filteredParams.sortBy === 'score') {
            filteredParams.sortBy = 'modified';
          }

          const queryString = new URLSearchParams(filteredParams).toString();
          return `/directories?${queryString}`;
        },
        transformResponse: async (response, meta) => {
          const totalCount = meta.response.headers.get('X-Total-Count');
          const totalPages = meta.response.headers.get('X-Total-Pages');
          const directories = await response;

          return {
            directories,
            totalCount,
            totalPages,
          };
        },
      }),
    })
})

export const {
  useSearchNotesQuery,
  useLazySearchNotesQuery,
  useSearchDirectoriesQuery,
  useLazySearchDirectoriesQuery,
} = searchApiSlice
