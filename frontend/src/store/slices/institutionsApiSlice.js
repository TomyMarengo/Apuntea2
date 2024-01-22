import { apiSlice } from './apiSlice';

export const institutionsApiSlice = apiSlice.injectEndpoints({
  endpoints: (builder) => (
    {
      getInstitutions: builder.query({
        query: () => '/institutions',
      }),
      getInstitution: builder.query({
        query: ({ institutionId, url }) => url || `/institutions/${institutionId}`,
      }),
      getCareers: builder.query({
        query: (institutionId) => `/institutions/${institutionId}/careers`,
      }),
      getCareer: builder.query({
        query: ({ institutionId, careerId, url }) => url || `/institutions/${institutionId}/careers/${careerId}`,
      }),
      getSubjects: builder.query({
        query: ({ institutionId, careerId }) => `/institutions/${institutionId}/careers/${careerId}/subjects`,
      }),
    }),
});

export const {
  useGetInstitutionsQuery,
  useGetInstitutionQuery,
  useLazyGetInstitutionQuery,
  useGetCareersQuery,
  useGetCareerQuery,
  useLazyGetCareerQuery,
  useGetSubjectsQuery,
} = institutionsApiSlice;
