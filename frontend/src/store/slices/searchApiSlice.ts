// src/store/slices/searchApiSlice.ts

import { apiSlice } from './apiSlice';
import { isUuid } from '../../utils/helpers';
import { Note, Directory } from '../../types';
import { mapApiNote, mapApiDirectory } from '../../utils/mappers';

interface NotesSearchResponse {
  notes: any[];
  totalCount?: number;
  totalPages?: number;
}

interface DirectoriesSearchResponse {
  directories: any[];
  totalCount?: number;
  totalPages?: number;
}

interface SearchArgs {
  institutionId?: string;
  careerId?: string;
  subjectId?: string;
  word?: string;
  page?: string | number;
  asc?: string;
  sortBy?: string;
  category?: string;
  pageSize?: string | number;
  parentId?: string;
  userId?: string;
  url?: string;
}

export const searchApiSlice = apiSlice.injectEndpoints({
  endpoints: (builder) => ({
    searchNotes: builder.query<NotesSearchResponse, SearchArgs>({
      query: ({
        institutionId,
        careerId,
        subjectId,
        word,
        page,
        asc,
        sortBy,
        category,
        pageSize,
        parentId,
        userId,
        url,
      }) => {
        const queryParams: Record<string, any> = {
          institutionId,
          careerId,
          subjectId,
          word,
          page,
          asc,
          sortBy,
          pageSize,
          parentId,
          userId,
        };

        if (category && category !== 'note') {
          queryParams.category = category;
        }

        const filteredParams = Object.fromEntries(
          Object.entries(queryParams).filter(
            ([_, value]) => value != null && value !== '' && value != undefined,
          ),
        );

        // Validate UUIDs
        if (
          filteredParams.institutionId &&
          !isUuid(filteredParams.institutionId)
        ) {
          throw new Error('errors.institutionId');
        }
        if (filteredParams.careerId && !isUuid(filteredParams.careerId)) {
          throw new Error('errors.careerId');
        }
        if (filteredParams.subjectId && !isUuid(filteredParams.subjectId)) {
          throw new Error('errors.subjectId');
        }

        const queryString = new URLSearchParams(filteredParams).toString();
        return url || `/notes?${queryString}`;
      },
      transformResponse: (response: any, meta: any): NotesSearchResponse => {
        const totalCount = meta.response.headers.get('X-Total-Count') || '0';
        const totalPages = meta.response.headers.get('X-Total-Pages') || '0';
        const notes: Note[] = Array.isArray(response)
          ? response.map(mapApiNote)
          : [];

        return {
          notes,
          totalCount: Number(totalCount),
          totalPages: Number(totalPages),
        };
      },
      providesTags: (result) =>
        result
          ? [
              ...result.notes.map(({ id }: { id: string }) => ({
                type: 'Notes' as const,
                id,
              })),
              { type: 'Notes', id: 'PARTIAL-LIST' },
            ]
          : [{ type: 'Notes', id: 'PARTIAL-LIST' }],
    }),
    searchDirectories: builder.query<DirectoriesSearchResponse, SearchArgs>({
      query: ({
        institutionId,
        careerId,
        subjectId,
        word,
        page,
        asc,
        sortBy,
        pageSize,
        parentId,
        userId,
        url,
      }) => {
        const queryParams: Record<string, any> = {
          institutionId,
          careerId,
          subjectId,
          word,
          page,
          asc,
          sortBy,
          pageSize,
          parentId,
          userId,
        };

        const filteredParams = Object.fromEntries(
          Object.entries(queryParams).filter(
            ([_, value]) => value != null && value !== '' && value != undefined,
          ),
        );

        // Validate UUIDs
        if (
          filteredParams.institutionId &&
          !isUuid(filteredParams.institutionId)
        ) {
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
        return url || `/directories?${queryString}`;
      },
      transformResponse: (
        response: any,
        meta: any,
      ): DirectoriesSearchResponse => {
        const totalCount = meta.response.headers.get('X-Total-Count') || '0';
        const totalPages = meta.response.headers.get('X-Total-Pages') || '0';
        const directories: Directory[] = Array.isArray(response)
          ? response.map(mapApiDirectory)
          : [];

        return {
          directories,
          totalCount: Number(totalCount),
          totalPages: Number(totalPages),
        };
      },
      providesTags: (result) =>
        result
          ? [
              ...result.directories.map(({ id }: { id: string }) => ({
                type: 'Directories' as const,
                id,
              })),
              { type: 'Directories', id: 'PARTIAL-LIST' },
            ]
          : [{ type: 'Directories', id: 'PARTIAL-LIST' }],
    }),
  }),
});

export const {
  useSearchNotesQuery,
  useLazySearchNotesQuery,
  useSearchDirectoriesQuery,
  useLazySearchDirectoriesQuery,
} = searchApiSlice;
