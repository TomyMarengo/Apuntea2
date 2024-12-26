// src/store/slices/institutionsApiSlice.ts

import { apiSlice } from './apiSlice';
import { Institution, Career, Subject, SubjectCareer } from '../../types';
import {
  mapApiSubject,
  mapApiInstitution,
  mapApiCareer,
  mapApiSubjectCareer,
} from '../../utils/mappers';

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

interface CareerSubjectsByYearArgs {
  careerId?: string;
  year?: number;
  userId?: string;
  url?: string;
}

export const institutionsApiSlice = apiSlice.injectEndpoints({
  endpoints: (builder) => ({
    getInstitutions: builder.query<Institution[], void>({
      query: () => '/institutions',
      transformResponse: (response: any) => {
        const institutions: Institution[] = Array.isArray(response)
          ? response.map(mapApiInstitution)
          : [];

        return institutions;
      },
      keepUnusedDataFor: 86400,
    }),
    getInstitution: builder.query<Institution, InstitutionArgs>({
      query: ({ institutionId, url }) =>
        url || `/institutions/${institutionId}`,
      transformResponse: (response: any) => mapApiInstitution(response),
      keepUnusedDataFor: 86400,
    }),
    getCareers: builder.query<Career[], InstitutionArgs>({
      query: ({ institutionId, url }) =>
        url || `/institutions/${institutionId}/careers`,
      transformResponse: (response: any) => {
        const careers: Career[] = Array.isArray(response)
          ? response.map(mapApiCareer)
          : [];

        return careers;
      },
      keepUnusedDataFor: 86400,
    }),
    getCareer: builder.query<Career, CareerArgs>({
      query: ({ institutionId, careerId, url }) =>
        url || `/institutions/${institutionId}/careers/${careerId}`,
      transformResponse: (response: any) => mapApiCareer(response),
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
    getSubjectCareer: builder.query<
      SubjectCareer,
      CareerArgs & { subjectId?: string }
    >({
      query: ({ institutionId, careerId, subjectId, url }) =>
        url ||
        `/institutions/${institutionId}/careers/${careerId}/subjectcareers/${subjectId}`,
      transformResponse: (response: any) => mapApiSubjectCareer(response),
      keepUnusedDataFor: 86400,
    }),
    getCareerSubjectsByYear: builder.query<Subject[], CareerSubjectsByYearArgs>(
      {
        query: ({ careerId, year, url }) =>
          url || `/subjects?careerId=${careerId}&year=${year}`,
        transformResponse: (response: any) => {
          const subjects: Subject[] = Array.isArray(response)
            ? response.map(mapApiSubject)
            : [];
          return subjects;
        },
        providesTags: ['Subjects'],
      },
    ),
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
