// src/store/slices/institutionsApiSlice.ts

import { apiSlice } from './apiSlice';
import { Institution, Career, Subject } from '../../types';
import { mapApiSubject } from '../../utils/mappers';

/**
 * One-of logic:
 * - We can pass either { institutionId, careerId, subjectId }
 * - Or { url }
 */
interface InstitutionArgs {
  institutionId?: string;
  url?: string;
}

interface CareerArgs {
  institutionId?: string;
  careerId?: string;
  url?: string;
}

interface SubjectArgs {
  subjectId?: string;
  url?: string;
}

interface SubjectsByCareerArgs {
  careerId?: string;
  url?: string;
}

export const institutionsApiSlice = apiSlice.injectEndpoints({
  endpoints: (builder) => ({
    getInstitutions: builder.query<Institution[], void>({
      query: () => '/institutions',
      keepUnusedDataFor: 86400,
    }),
    getInstitution: builder.query<Institution, InstitutionArgs>({
      query: ({ institutionId, url }) =>
        url || `/institutions/${institutionId}`,
      keepUnusedDataFor: 86400,
    }),
    getCareers: builder.query<Career[], InstitutionArgs>({
      query: ({ institutionId, url }) =>
        url || `/institutions/${institutionId}/careers`,
      keepUnusedDataFor: 86400,
    }),
    getCareer: builder.query<Career, CareerArgs>({
      query: ({ institutionId, careerId, url }) =>
        url || `/institutions/${institutionId}/careers/${careerId}`,
      keepUnusedDataFor: 86400,
    }),
    getSubjectsByCareer: builder.query<Subject[], SubjectsByCareerArgs>({
      query: ({ careerId, url }) => url || `/subjects?careerId=${careerId}`,
      transformResponse: (response: any): Subject[] => {
        const subjects: Subject[] = Array.isArray(response)
          ? response.map(mapApiSubject)
          : [];

        return subjects;
      },
      keepUnusedDataFor: 86400,
    }),
    getSubject: builder.query<Subject, SubjectArgs>({
      query: ({ subjectId, url }) => url || `/subjects/${subjectId}`,
      transformResponse: (response: any) => mapApiSubject(response),
      keepUnusedDataFor: 86400,
    }),
    getSubjectCareer: builder.query<any, CareerArgs & { subjectId?: string }>({
      query: ({ institutionId, careerId, subjectId, url }) =>
        url ||
        `/institutions/${institutionId}/careers/${careerId}/subjectcareers/${subjectId}`,
      keepUnusedDataFor: 86400,
    }),
    getCareerSubjectsByYear: builder.query<
      any,
      { careerId: string; year: number; url?: string }
    >({
      query: ({ careerId, year, url }) =>
        url || `/subjects?careerId=${careerId}&year=${year}`,
      providesTags: ['Subjects'],
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
