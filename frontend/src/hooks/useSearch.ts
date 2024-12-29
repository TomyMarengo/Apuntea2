// src/hooks/useSearch.ts

import { useEffect } from 'react';
import { useSearchParams, useNavigate } from 'react-router-dom';
import {
  useSearchNotesQuery,
  useSearchDirectoriesQuery,
} from '../store/slices/searchApiSlice';
import { useSelector } from 'react-redux';
import { selectCurrentUser } from '../store/slices/authSlice';

interface UseSearchReturn {
  searchParams: URLSearchParams;
  isLoadingData: boolean;
  notes: any[];
  directories: any[];
  showNotes: boolean;
  showDirectories: boolean;
  currentPage: number;
  pageSize: number;
  totalPages: number;
  totalCount: number;
}

export default function useSearch(userData: boolean = true): UseSearchReturn {
  const [searchParams, setSearchParams] = useSearchParams();
  const navigate = useNavigate();
  const user = useSelector(selectCurrentUser);

  // User data
  if (userData) {
    useEffect(() => {
      let updated = false;

      if (!searchParams.has('institutionId') && user?.institution?.id) {
        searchParams.set('institutionId', user.institution.id);
        updated = true;
      }

      if (!searchParams.has('careerId') && user?.career?.id) {
        searchParams.set('careerId', user.career.id);
        updated = true;
      }

      if (updated) {
        // Reset to first page on defaults set
        searchParams.set('page', '1');
        navigate({ search: searchParams.toString() }, { replace: true });
      }
      // eslint-disable-next-line react-hooks/exhaustive-deps
    }, [user, searchParams, setSearchParams]);
  }

  // Extract search parameters
  const pageParam = searchParams.get('page') || '1';
  const pageSizeParam = searchParams.get('pageSize') || '10';
  const ascParam = searchParams.get('asc') || 'true';
  const sortByParam = searchParams.get('sortBy') || 'modified';
  const categoryParam =
    (searchParams.get('category') as 'note' | 'directory') || 'note';
  const wordParam = searchParams.get('word');
  const institutionIdParam = searchParams.get('institutionId');
  const careerIdParam = searchParams.get('careerId');
  const subjectIdParam = searchParams.get('subjectId');
  const parentIdParam = searchParams.get('parentId');
  const userId = searchParams.get('userId');

  const currentPage = Number(pageParam);
  const pageSize = Number(pageSizeParam);

  const searchArgs: Record<string, any> = {
    page: currentPage,
    pageSize,
    asc: ascParam,
    sortBy: sortByParam,
    category: categoryParam,
  };

  if (institutionIdParam) searchArgs.institutionId = institutionIdParam;
  if (careerIdParam) searchArgs.careerId = careerIdParam;
  if (subjectIdParam) searchArgs.subjectId = subjectIdParam;
  if (wordParam) searchArgs.word = wordParam;
  if (parentIdParam) searchArgs.parentId = parentIdParam;
  if (userId) searchArgs.userId = userId;

  // Fetch notes or directories based on category
  const { data: dataNotes, isLoading: isLoadingNotes } = useSearchNotesQuery(
    searchArgs,
    { skip: categoryParam === 'directory' },
  );
  const { data: dataDirs, isLoading: isLoadingDirs } =
    useSearchDirectoriesQuery(searchArgs, {
      skip: categoryParam !== 'directory',
    });

  const {
    totalCount: totalCountNotes,
    totalPages: totalPagesNotes,
    notes,
  } = dataNotes || { totalCount: 0, totalPages: 0, notes: [] };
  const {
    totalCount: totalCountDirectories,
    totalPages: totalPagesDirectories,
    directories,
  } = dataDirs || { totalCount: 0, totalPages: 0, directories: [] };

  const showNotes = categoryParam !== 'directory';
  const showDirectories = categoryParam === 'directory';

  const isLoadingData = isLoadingNotes || isLoadingDirs;

  const safeCountNotes = Number(totalCountNotes || 0);
  const safePagesNotes = Number(totalPagesNotes || 0);
  const safeCountDirs = Number(totalCountDirectories || 0);
  const safePagesDirs = Number(totalPagesDirectories || 0);

  // Calculate totalPages based on category
  const totalPages =
    categoryParam === 'directory' ? safePagesDirs : safePagesNotes;
  const totalCount =
    categoryParam === 'directory' ? safeCountDirs : safeCountNotes;

  return {
    searchParams,
    isLoadingData,
    notes: notes || [],
    directories: directories || [],
    showNotes,
    showDirectories,
    currentPage,
    pageSize,
    totalPages,
    totalCount,
  };
}
