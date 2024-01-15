import { apiSlice } from "./apiSlice";

export const searchApiSlice = apiSlice.injectEndpoints({
  endpoints: builder => (
    {
      search: builder.query({
        query: ({ institutionId, careerId, subjectId, word, pageNumber, ascending, sortBy, category, pageSize, parentId }) => `/search?institutionId=${institutionId}&careerId=${careerId}&subjectId=${subjectId}&word=${word}&page=${pageNumber}&asc=${ascending}&sortBy=${sortBy}&category=${category}&pageSize=${pageSize}&parentId=${parentId}`,
        keepUnusedDataFor: 5, // 5 seconds
        transformResponse: async (response) => {
          const totalCount = response.headers.get('X-Total-Count');
          const totalPages = response.headers.get('X-Total-Pages');

          const data = await response.json();

          return {
            data,
            totalCount,
            totalPages,
          };
        }
      }),
    })
})

export const {
  useSearchQuery,
} = searchApiSlice
