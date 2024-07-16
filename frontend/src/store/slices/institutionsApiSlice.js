import { apiSlice } from './apiSlice';

export const institutionsApiSlice = apiSlice.injectEndpoints({
  endpoints: (builder) => ({
    getInstitutions: builder.query({
      query: () => '/institutions',
      keepUnusedDataFor: 24 * 60 * 60, // 24 hours
    }),
    getInstitution: builder.query({
      query: ({ institutionId, url }) => url || `/institutions/${institutionId}`,
      keepUnusedDataFor: 24 * 60 * 60, // 24 hours
    }),
    getCareers: builder.query({
      query: ({ institutionId, url }) => url || `/institutions/${institutionId}/careers`,
      keepUnusedDataFor: 24 * 60 * 60, // 24 hours
    }),
    getCareer: builder.query({
      query: ({ institutionId, careerId, url }) => url || `/institutions/${institutionId}/careers/${careerId}`,
      keepUnusedDataFor: 24 * 60 * 60, // 24 hours
    }),
    getSubjectsByCareer: builder.query({
      query: ({ careerId, url }) => url || `/subjects?careerId=${careerId}`,
      keepUnusedDataFor: 24 * 60 * 60, // 24 hours
    }),
    getSubject: builder.query({
      query: ({ subjectId, url }) => url || `/subjects/${subjectId}`,
      keepUnusedDataFor: 24 * 60 * 60, // 24 hours
    }),
    getSubjectCareer: builder.query({
      query: ({ institutionId, careerId, subjectId, url }) =>
        url || `/institutions/${institutionId}/careers/${careerId}/subjectcareers/${subjectId}`,
      keepUnusedDataFor: 24 * 60 * 60, // 24 hours
    }),
    getCareerSubjectsByYear: builder.query({
      query: ({ careerId, year, url }) => url || `/subjects?careerId=${careerId}&year=${year}`,
      providesTags: ['Subjects'],
      refetchOnMountOrArgChange: true,
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
  useGetSubjectsByCareerQuery,
  useLazyGetSubjectsByCareerQuery,
  useGetSubjectQuery,
  useLazyGetSubjectQuery,
  useGetSubjectCareerQuery,
  useLazyGetSubjectCareerQuery,
  useGetCareerSubjectsByYearQuery,
} = institutionsApiSlice;
