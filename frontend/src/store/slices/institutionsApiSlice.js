import { apiSlice } from './apiSlice';

export const institutionsApiSlice = apiSlice.injectEndpoints({
  endpoints: (builder) => ({
    getInstitutions: builder.query({
      query: () => '/institutions',
    }),
    getInstitution: builder.query({
      query: ({ institutionId, url }) => url || `/institutions/${institutionId}`,
    }),
    getCareers: builder.query({
      query: ({ institutionId, url }) => url || `/institutions/${institutionId}/careers`,
    }),
    getCareer: builder.query({
      query: ({ institutionId, careerId, url }) => url || `/institutions/${institutionId}/careers/${careerId}`,
    }),
    getSubjects: builder.query({
      query: ({ institutionId, careerId, url }) => url || `/institutions/${institutionId}/careers/${careerId}/subjects`,
    }),
    getSubject: builder.query({
      query: ({ institutionId, careerId, subjectId, url }) => url || `/institutions/${institutionId}/careers/${careerId}/subjects/${subjectId}`,
    }),
  }),
});

export const {
  useGetInstitutionsQuery,
  useLazyGetInstitutionsQuery,
  useGetInstitutionQuery,
  useLazyGetInstitutionQuery,
  useGetCareersQuery,
  useLazyGetCareersQuery,
  useGetCareerQuery,
  useLazyGetCareerQuery,
  useGetSubjectsQuery,
  useLazyGetSubjectsQuery,
  useGetSubjectQuery,
  useLazyGetSubjectQuery,
} = institutionsApiSlice;
