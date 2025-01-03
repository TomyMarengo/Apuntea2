// src/hooks/useSearch.ts

import { zodResolver } from '@hookform/resolvers/zod';
import { useEffect, useMemo, useRef } from 'react';
import { useForm, UseFormSetValue } from 'react-hook-form';
import { useNavigate, useSearchParams, useLocation } from 'react-router-dom';

import useDebounce from './useDebounce';
import { searchSchema, SearchFormValues } from '../pages/Search/searchSchema';
import {
  useSearchNotesQuery,
  useSearchDirectoriesQuery,
} from '../store/slices/searchApiSlice';
import { Note, Directory } from '../types';

interface UseSearchReturn {
  control: any;
  watchedValues: SearchFormValues;
  isLoading: boolean;
  setValue: UseFormSetValue<SearchFormValues>;
  notes: Note[];
  directories: Directory[];
  totalCount: number;
  totalPages: number;
  currentPage: number;
  pageSize: number;
}

const useSearch = (parentId?: string): UseSearchReturn => {
  const navigate = useNavigate();
  const [searchParams] = useSearchParams();
  const location = useLocation();
  const isFirstRender = useRef(true);

  const defaultValues: SearchFormValues = {
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
  };

  const { control, watch, setValue, reset } = useForm<SearchFormValues>({
    resolver: zodResolver(searchSchema),
    defaultValues,
  });

  useEffect(() => {
    reset(defaultValues);
  }, [location.search]);

  const watchedValues = watch();
  const debouncedWord = useDebounce(watchedValues.word, 500);

  useEffect(() => {
    if (isFirstRender.current) {
      isFirstRender.current = false;
      return;
    }

    const params = new URLSearchParams();

    if (watchedValues.institutionId)
      params.set('institutionId', watchedValues.institutionId);
    if (watchedValues.careerId) params.set('careerId', watchedValues.careerId);
    if (watchedValues.subjectId)
      params.set('subjectId', watchedValues.subjectId);
    if (watchedValues.userId) params.set('userId', watchedValues.userId);
    if (watchedValues.word) params.set('word', watchedValues.word);
    if (watchedValues.category) params.set('category', watchedValues.category);
    if (watchedValues.sortBy) params.set('sortBy', watchedValues.sortBy);
    if (watchedValues.asc) params.set('asc', watchedValues.asc);
    if (watchedValues.page) params.set('page', watchedValues.page);
    if (watchedValues.pageSize) params.set('pageSize', watchedValues.pageSize);

    const newSearch = params.toString();
    const currentSearch = location.search.startsWith('?')
      ? location.search.substring(1)
      : location.search;

    if (newSearch !== currentSearch) {
      navigate({ search: newSearch });
    }
  }, [
    watchedValues.institutionId,
    watchedValues.careerId,
    watchedValues.subjectId,
    watchedValues.userId,
    watchedValues.word,
    watchedValues.category,
    watchedValues.sortBy,
    watchedValues.asc,
    watchedValues.page,
    watchedValues.pageSize,
    navigate,
  ]);

  // Prepare search arguments
  const searchArgs = useMemo(() => {
    const args: Record<string, any> = {
      page: watchedValues.page,
      pageSize: watchedValues.pageSize,
      asc: watchedValues.asc,
      sortBy: watchedValues.sortBy,
      category: watchedValues.category,
    };

    if (parentId) args.parentId = parentId;
    if (watchedValues.institutionId)
      args.institutionId = watchedValues.institutionId;
    if (watchedValues.careerId) args.careerId = watchedValues.careerId;
    if (watchedValues.subjectId) args.subjectId = watchedValues.subjectId;
    if (watchedValues.userId) args.userId = watchedValues.userId;
    if (debouncedWord) args.word = debouncedWord;

    return args;
  }, [
    watchedValues.page,
    watchedValues.pageSize,
    watchedValues.asc,
    watchedValues.sortBy,
    watchedValues.category,
    watchedValues.institutionId,
    watchedValues.careerId,
    watchedValues.subjectId,
    watchedValues.userId,
    debouncedWord,
  ]);

  const { data: dataNotes, isLoading: isLoadingNotes } = useSearchNotesQuery(
    searchArgs,
    { skip: watchedValues.category === 'directory' },
  );

  const { data: dataDirs, isLoading: isLoadingDirs } =
    useSearchDirectoriesQuery(searchArgs, {
      skip: watchedValues.category !== 'directory',
    });

  const isLoading = isLoadingNotes || isLoadingDirs;

  const notesData = dataNotes || { totalCount: 0, totalPages: 0, notes: [] };
  const dirsData = dataDirs || {
    totalCount: 0,
    totalPages: 0,
    directories: [],
  };

  const totalCount =
    (watchedValues.category === 'directory'
      ? dirsData.totalCount
      : notesData.totalCount) || 0;
  const totalPages =
    (watchedValues.category === 'directory'
      ? dirsData.totalPages
      : notesData.totalPages) || 0;
  const currentPage = Number(watchedValues.page);
  const pageSize = Number(watchedValues.pageSize);

  const notes = notesData.notes || [];
  const directories = dirsData.directories || [];

  return {
    control,
    watchedValues,
    isLoading,
    setValue,
    notes,
    directories,
    totalCount,
    totalPages,
    currentPage,
    pageSize,
  };
};

export default useSearch;
