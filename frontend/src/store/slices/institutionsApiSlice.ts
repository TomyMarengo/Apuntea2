// src/store/slices/institutionsApiSlice.ts

import { apiSlice, ApiResponse } from './apiSlice';
import {
  CAREER_COLLECTION_CONTENT_TYPE,
  CAREER_CONTENT_TYPE,
  INSTITUTION_COLLECTION_CONTENT_TYPE, INSTITUTION_CONTENT_TYPE, SUBJECT_CAREER_COLLECTION_CONTENT_TYPE,
  SUBJECT_CAREER_CONTENT_TYPE,
  SUBJECT_CAREER_CREATE_CONTENT_TYPE, SUBJECT_COLLECTION_CONTENT_TYPE,
  SUBJECT_CONTENT_TYPE,
} from '../../contentTypes.ts';
import { Institution, Career, Subject, SubjectCareer } from '../../types';
import { extractErrorMessages } from '../../utils/helpers';
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
      query: () => ({
        url: '/institutions',
        headers: {
          Accept: INSTITUTION_COLLECTION_CONTENT_TYPE
        }
      }),
      transformResponse: (response: any) => {
        const institutions: Institution[] = Array.isArray(response)
          ? response.map(mapApiInstitution)
          : [];

        return institutions;
      },
      keepUnusedDataFor: 86400,
    }),
    getInstitution: builder.query<Institution, InstitutionQueryArgs>({
      query: ({ institutionId, url }) => ({
        url: url || `/institutions/${institutionId}`,
        headers: {
          Accept: INSTITUTION_CONTENT_TYPE
        }
      }),
      transformResponse: (response: any) => mapApiInstitution(response),
      keepUnusedDataFor: 86400,
    }),
    getCareers: builder.query<Career[], InstitutionQueryArgs>({
      query: ({ institutionId, url }) => ({
        url: url || `/institutions/${institutionId}/careers`,
        headers: {
          Accept: CAREER_COLLECTION_CONTENT_TYPE
        }
      }),
      transformResponse: (response: any) => {
        const careers: Career[] = Array.isArray(response)
          ? response.map(mapApiCareer)
          : [];

        return careers;
      },
      keepUnusedDataFor: 86400,
    }),
    getCareer: builder.query<Career, CareerQueryArgs>({
      query: ({ institutionId, careerId, url }) => ({
        url: url || `/institutions/${institutionId}/careers/${careerId}`,
        headers: {
          Accept: CAREER_CONTENT_TYPE
        }
      }),
      transformResponse: (response: any) => mapApiCareer(response),
      keepUnusedDataFor: 86400,
    }),
    getSubject: builder.query<Subject, SubjectQueryArgs>({
      query: ({ subjectId, url }) => ({
        url: url || `/subjects/${subjectId}`,
        headers: {
            Accept: SUBJECT_CONTENT_TYPE
        }
      }),
      transformResponse: (response: any) => mapApiSubject(response),
      providesTags: ['Subjects'],
      keepUnusedDataFor: 86400,
    }),
    createSubject: builder.mutation<ApiResponse, CreateSubjectArgs>({
      async queryFn(
        { name, year, institutionId, careerId, subjectCareerUrl, url },
        _queryApi,
        _extraOptions,
        baseQuery,
      ) {
        let errorMessages: string[] = [];

        try {
          // Step 1: Create the subject
          const subjectResult = await baseQuery({
            url: url || '/subjects',
            method: 'POST',
            body: JSON.stringify({ name }),
            headers: {
              'Content-Type': SUBJECT_CONTENT_TYPE,
            },
          });

          if (subjectResult.error) {
            errorMessages = extractErrorMessages(subjectResult.error);
            return {
              data: {
                success: false,
                messages: errorMessages,
              },
            };
          }

          const subjectUrl = (subjectResult.meta as any)?.response.headers.get(
            'Location',
          );
          const subjectId = subjectUrl?.split('/').pop();

          // Step 2: Associate the subject with the career and year
          if (institutionId && careerId) {
            const subjectCareerResult = await baseQuery({
              url: subjectCareerUrl || `/institutions/${institutionId}/careers/${careerId}/subjectcareers`,
              method: 'POST',
              body: JSON.stringify({ subjectId, year }),
              headers: {
                'Content-Type': SUBJECT_CAREER_CREATE_CONTENT_TYPE,
              },
            });

            if (subjectCareerResult.error) {
              errorMessages = extractErrorMessages(subjectCareerResult.error);
              return {
                data: {
                  success: false,
                  messages: errorMessages,
                },
              };
            }
          }

          return {
            data: {
              success: true,
              messages: [],
            },
          };
        } catch (error) {
          console.error('Failed to create subject:', error);
          errorMessages.push('An unexpected error occurred.');
          return {
            data: {
              success: false,
              messages: errorMessages,
            },
          };
        }
      },
      invalidatesTags: ['Subjects'],
    }),
    updateSubject: builder.mutation<ApiResponse, UpdateSubjectArgs>({
      async queryFn(
        { name, year, institutionId, careerId, subjectId },
        _queryApi,
        _extraOptions,
        baseQuery,
      ) {
        let errorMessages: string[] = [];

        try {
          // Step 1: Update the subject
          if (name) {
            const subjectResult = await baseQuery({
              url: `/subjects/${subjectId}`,
              method: 'PUT',
              body: JSON.stringify({ name }),
              headers: {
                'Content-Type': SUBJECT_CONTENT_TYPE,
              },
            });

            if (subjectResult.error) {
              errorMessages = extractErrorMessages(subjectResult.error);
              return {
                data: { success: false, messages: errorMessages },
              };
            }
          }

          // Step 2: Associate the subject with the career and year
          if (year) {
            const subjectCareerResult = await baseQuery({
              url: `/institutions/${institutionId}/careers/${careerId}/subjectcareers/${subjectId}`,
              method: 'PUT',
              body: JSON.stringify({ year }),
              headers: {
                'Content-Type': SUBJECT_CAREER_CONTENT_TYPE,
              },
            });

            if (subjectCareerResult.error) {
              errorMessages = extractErrorMessages(subjectCareerResult.error);
              return {
                data: { success: false, messages: errorMessages },
              };
            }
          }

          return { data: { success: true, messages: [] } };
        } catch (error) {
          console.error('Failed to update subject:', error);
          errorMessages.push('An unexpected error occurred.');
          return { data: { success: false, messages: errorMessages } };
        }
      },
      invalidatesTags: ['Subjects'],
    }),
    linkSubjectCareer: builder.mutation<ApiResponse, LinkSubjectArgs>({
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
              body: JSON.stringify({ subjectId, year }),
              headers: {
                'Content-Type': SUBJECT_CAREER_CREATE_CONTENT_TYPE,
              },
            });
            if (subjectCareerResult.error) {
              let errorMessages = extractErrorMessages(subjectCareerResult.error);
              return { data: { success: false, messages: errorMessages } };
            }
          }
          return { data: { success: true, messages: [] } };
        } catch (error) {
          return { data: { success: false, messages: [] } };
        }
      },
      invalidatesTags: ['Subjects'],
    }),

    unlinkSubjectCareer: builder.mutation<ApiResponse, UnlinkSubjectArgs>({
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
            if (subjectCareerResult.error) {
              return { data: { success: false, messages: [] } };
            }
          }
          return { data: { success: true, messages: [] } };
        } catch (error) {
          console.error('Failed to unlink subject from career:', error);
          return { data: { success: false, messages: [] } };
        }
      },
      invalidatesTags: ['Subjects'],
    }),
    getSubjectsNotInCareer: builder.query<Subject[], CareerQueryArgs>({
      query: ({ careerId, url }) => ({
        url: url || `/subjects?notInCareer=${careerId}`,
        headers: {
          Accept: SUBJECT_COLLECTION_CONTENT_TYPE
        }
      }),
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
      query: ({ careerId, url }) => ({
        url: url || `/subjects?careerId=${careerId}`,
        headers: {
          Accept: SUBJECT_COLLECTION_CONTENT_TYPE
        }
      }),
      transformResponse: (response: any): Subject[] => {
        const subjects: Subject[] = Array.isArray(response)
          ? response.map(mapApiSubject)
          : [];
        return subjects;
      },
      providesTags: ['Subjects'],
      keepUnusedDataFor: 86400,
    }),
    getSubjectCareers: builder.query<SubjectCareer[], CareerQueryArgs>({
      query: ({ institutionId, careerId, url }) => ({
        url: url ||`/institutions/${institutionId}/careers/${careerId}/subjectcareers`,
        headers: {
          Accept: SUBJECT_CAREER_COLLECTION_CONTENT_TYPE
        }
      }),
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
        query: ({ careerId, year, url }) => ({
          url: url || `/subjects?careerId=${careerId}&year=${year}`,
          headers: {
            Accept: SUBJECT_COLLECTION_CONTENT_TYPE
          }
        }),
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
