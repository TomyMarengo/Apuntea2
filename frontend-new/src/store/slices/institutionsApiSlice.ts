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
interface InstitutionQueryArgs {
  institutionId?: string;
  url?: string;
}

interface CareerQueryArgs {
  institutionId?: string;
  careerId?: string;
  url?: string;
}

interface SubjectQueryArgs {
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

interface CreateSubjectArgs {
  name: string;
  year: number;
  institutionId?: string;
  careerId?: string;
  url?: string;
}

interface UpdateSubjectArgs {
  name?: string;
  year?: number;
  institutionId: string;
  careerId: string;
  subjectId: string;
}

interface LinkSubjectArgs {
  year: number;
  institutionId?: string;
  careerId?: string;
  subjectId?: string;
  url?: string;
}

interface UnlinkSubjectArgs {
  institutionId?: string;
  careerId?: string;
  subjectId?: string;
  url?: string;
}

//TODO: Pensar mejor los invalidateTags y provideTags

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
    getInstitution: builder.query<Institution, InstitutionQueryArgs>({
      query: ({ institutionId, url }) =>
        url || `/institutions/${institutionId}`,
      transformResponse: (response: any) => mapApiInstitution(response),
      keepUnusedDataFor: 86400,
    }),
    getCareers: builder.query<Career[], InstitutionQueryArgs>({
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
    getCareer: builder.query<Career, CareerQueryArgs>({
      query: ({ institutionId, careerId, url }) =>
        url || `/institutions/${institutionId}/careers/${careerId}`,
      transformResponse: (response: any) => mapApiCareer(response),
      keepUnusedDataFor: 86400,
    }),
    getSubject: builder.query<Subject, SubjectQueryArgs>({
      query: ({ subjectId, url }) => url || `/subjects/${subjectId}`,
      transformResponse: (response: any) => mapApiSubject(response),
      providesTags: ['Subjects'],
      keepUnusedDataFor: 86400,
    }),
    createSubject: builder.mutation<boolean, CreateSubjectArgs>({
      async queryFn(
        { name, year, institutionId, careerId, url },
        _queryApi,
        _extraOptions,
        baseQuery,
      ) {
        try {
          // Step 1: Create the subject
          const subjectResult = await baseQuery({
            url: url || '/subjects',
            method: 'POST',
            body: { name },
            headers: {
              'Content-Type': 'application/vnd.apuntea.subject-v1.0+json',
            },
          });
          if (subjectResult.error) return { data: false }; // Return false if there's an error

          const subjectData = subjectResult.data as { id: string };
          const subjectId = subjectData.id;

          // Step 2: Associate the subject with the career and year
          if (institutionId && careerId) {
            const subjectCareerResult = await baseQuery({
              url: `/institutions/${institutionId}/careers/${careerId}/subjectcareers`,
              method: 'POST',
              body: { subjectId, year },
              headers: {
                'Content-Type':
                  'application/vnd.apuntea.subjectcareer-create-v1.0+json',
              },
            });
            if (subjectCareerResult.error) return { data: false }; // Return false if there's an error
          }

          return { data: true }; // Return true if everything was successful
        } catch (error: any) {
          return { data: false }; // Return false if an exception occurs
        }
      },
      invalidatesTags: ['Subjects'],
    }),
    updateSubject: builder.mutation<boolean, UpdateSubjectArgs>({
      async queryFn(
        { name, year, institutionId, careerId, subjectId },
        _queryApi,
        _extraOptions,
        baseQuery,
      ) {
        try {
          console.log('name', name);
          console.log('year', year);

          // Step 1: Update the subject
          if (name) {
            const subjectResult = await baseQuery({
              url: `/subjects/${subjectId}`,
              method: 'PUT',
              body: { name },
              headers: {
                'Content-Type': 'application/vnd.apuntea.subject-v1.0+json',
              },
            });
            if (subjectResult.error) return { data: false }; // Return false if there's an error
          }

          // Step 2: Associate the subject with the career and year
          if (year) {
            const subjectCareerResult = await baseQuery({
              url: `/institutions/${institutionId}/careers/${careerId}/subjectcareers/${subjectId}`,
              method: 'PUT',
              body: { year },
              headers: {
                'Content-Type':
                  'application/vnd.apuntea.subjectcareer-v1.0+json',
              },
            });
            if (subjectCareerResult.error) return { data: false }; // Return false if there's an error
          }
          return { data: true }; // Return true if everything was successful
        } catch (error: any) {
          return { data: false }; // Return false if an exception occurs
        }
      },
      invalidatesTags: ['Subjects'],
    }),
    linkSubjectCareer: builder.mutation<boolean, LinkSubjectArgs>({
      async queryFn(
        { year, institutionId, careerId, subjectId, url },
        _queryApi,
        _extraOptions,
        baseQuery,
      ) {
        try {
          if (subjectId && year) {
            const subjectCareerResult = await baseQuery({
              url:
                url ||
                `/institutions/${institutionId}/careers/${careerId}/subjectcareers`,
              method: 'POST',
              body: { subjectId, year },
              headers: {
                'Content-Type':
                  'application/vnd.apuntea.subjectcareer-create-v1.0+json',
              },
            });
            if (subjectCareerResult.error) return { data: false }; // Return false if there's an error
          }
          return { data: true }; // Return true if everything was successful
        } catch (error: any) {
          return { data: false }; // Return false if an exception occurs
        }
      },
      invalidatesTags: ['Subjects'],
    }),
    unlinkSubjectCareer: builder.mutation<boolean, UnlinkSubjectArgs>({
      async queryFn(
        { institutionId, careerId, subjectId, url },
        _queryApi,
        _extraOptions,
        baseQuery,
      ) {
        try {
          if (institutionId && careerId) {
            const subjectCareerResult = await baseQuery({
              url:
                url ||
                `/institutions/${institutionId}/careers/${careerId}/subjectcareers/${subjectId}`,
              method: 'DELETE',
            });
            if (subjectCareerResult.error) return { data: false }; // Return false if there's an error
          }
          return { data: true }; // Return true if everything was successful
        } catch (error: any) {
          return { data: false }; // Return false if an exception occurs
        }
      },
      invalidatesTags: ['Subjects'],
    }),
    getSubjectsNotInCareer: builder.query<Subject[], CareerQueryArgs>({
      query: ({ careerId, url }) => url || `/subjects?notInCareer=${careerId}`,
      transformResponse: (response: any) => {
        const subjects: Subject[] = Array.isArray(response)
          ? response.map(mapApiSubject)
          : [];
        return subjects;
      },
      providesTags: ['Subjects'],
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
      providesTags: ['Subjects'],
      keepUnusedDataFor: 86400,
    }),
    getSubjectCareers: builder.query<
      SubjectCareer[],
      CareerQueryArgs & { subjectId?: string }
    >({
      query: ({ institutionId, careerId, url }) =>
        url ||
        `/institutions/${institutionId}/careers/${careerId}/subjectcareers`,
      transformResponse: (response: any) => {
        const subjectCareers: SubjectCareer[] = Array.isArray(response)
          ? response.map(mapApiSubjectCareer)
          : [];
        return subjectCareers;
      },
      providesTags: ['Subjects'],
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
  useGetInstitutionQuery,
  useGetCareersQuery,
  useGetCareerQuery,
  useGetSubjectQuery,
  useCreateSubjectMutation,
  useUpdateSubjectMutation,
  useGetSubjectsNotInCareerQuery,
  useGetSubjectsByCareerQuery,
  useLinkSubjectCareerMutation,
  useUnlinkSubjectCareerMutation,
  useGetSubjectCareersQuery,
  useGetCareerSubjectsByYearQuery,
} = institutionsApiSlice;
