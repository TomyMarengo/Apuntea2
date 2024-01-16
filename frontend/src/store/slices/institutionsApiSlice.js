import { apiSlice } from './apiSlice';

export const institutionsApiSlice = apiSlice.injectEndpoints({
  endpoints: (builder) => ({
    getInstitutions: builder.query({
      query: () => '/institutions',
      keepUnusedDataFor: 5, // 5 seconds
    }),
    getInstitution: builder.mutation({
      query: ({ institutionId, url }) => url || `/institutions/${institutionId}`,
      keepUnusedDataFor: 5, // 5 seconds
    }),
    getCareers: builder.query({
      query: (institutionId) => `/institutions/${institutionId}/careers`,
      keepUnusedDataFor: 5, // 5 seconds
    }),
    getCareer: builder.mutation({
      query: ({ institutionId, careerId, url }) => url || `/institutions/${institutionId}/careers/${careerId}`,
      keepUnusedDataFor: 5, // 5 seconds
    }),
    getSubjects: builder.query({
      query: ({ institutionId, careerId }) => `/institutions/${institutionId}/careers/${careerId}/subjects`,
      keepUnusedDataFor: 5, // 5 seconds
    }),
  }),
});

export const {
  useGetInstitutionsQuery,
  useGetInstitutionMutation,
  useGetCareersQuery,
  useGetCareerMutation,
  useGetSubjectsQuery,
} = institutionsApiSlice;
