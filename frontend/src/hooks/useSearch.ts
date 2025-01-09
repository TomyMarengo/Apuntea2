// src/hooks/useSearch.ts

import { zodResolver } from '@hookform/resolvers/zod';
import { useMemo, useEffect } from 'react';
import { useForm } from 'react-hook-form';
import { useSearchParams } from 'react-router-dom';

import { searchSchema, SearchFormValues } from '../pages/Search/searchSchema';
import {
  useLazySearchNotesQuery,
  useLazySearchDirectoriesQuery,
} from '../store/slices/searchApiSlice';
import { Note, Directory } from '../types';

interface UseSearchReturn {
  control: any;
  watchedValues: SearchFormValues;
  isLoading: boolean;
  isError: boolean;
  notes: Note[];
  directories: Directory[];
  totalCount: number;
  totalPages: number;
  currentPage: number;
  pageSize: number;
}

const useSearch = (parentId?: string): UseSearchReturn => {
  const [searchParams] = useSearchParams();

  const defaultValues = useMemo(
    () => ({
      institutionId: searchParams.get('institutionId') || '',
      careerId: searchParams.get('careerId') || '',
      subjectId: searchParams.get('subjectId') || '',
      userId: searchParams.get('userId') || '',
      word: searchParams.get('word') || '',
      category: searchParams.get('category') || 'note',
      sortBy: searchParams.get('sortBy') || 'modified',
      asc: searchParams.get('asc') || 'true',
      page: searchParams.get('page') || '1',
      pageSize: searchParams.get('pageSize') || '10',
      parentId: parentId || '',
    }),
    [searchParams, parentId],
  );

  const { control, watch, reset, formState } = useForm<SearchFormValues>({
    resolver: zodResolver(searchSchema),
    defaultValues,
  });

  console.log('Form state:', formState);

  const watchedValues = watch();

  const [
    getNotes,
    { data: notesData, isError: isErrorNotes, isLoading: isLoadingNotes },
  ] = useLazySearchNotesQuery();

  const [
    getDirectories,
    { data: dirsData, isError: isErrorDirs, isLoading: isLoadingDirs },
  ] = useLazySearchDirectoriesQuery();

  const fetchData = async (data: SearchFormValues) => {
    if (data.category === 'directory') {
      await getDirectories(data);
    } else {
      await getNotes(data);
    }
  };

  useEffect(() => {
    console.log('Fetch!');
    reset(defaultValues);
    fetchData(defaultValues);
  }, [searchParams, parentId]);

  const isLoading = isLoadingNotes || isLoadingDirs;
  const isError = isErrorNotes || isErrorDirs;

  const totalCount =
    (watchedValues.category === 'directory'
      ? dirsData?.totalCount
      : notesData?.totalCount) || 0;
  const totalPages =
    (watchedValues.category === 'directory'
      ? dirsData?.totalPages
      : notesData?.totalPages) || 0;
  const currentPage = Number(watchedValues.page);
  const pageSize = Number(watchedValues.pageSize);

  const notes = notesData?.notes || [];
  const directories = dirsData?.directories || [];

  return {
    control,
    watchedValues,
    isLoading,
    isError,
    notes,
    directories,
    totalCount,
    totalPages,
    currentPage,
    pageSize,
  };
};

export default useSearch;
