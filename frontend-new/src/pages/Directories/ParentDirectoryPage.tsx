// src/pages/Directories/ParentDirectoryPage.tsx

import { useEffect } from 'react';
import { useParams, Link } from 'react-router-dom';
import { useGetDirectoryQuery } from '../../store/slices/directoriesApiSlice';
import {
  Box,
  Typography,
  CircularProgress,
  Alert,
  Link as MuiLink,
} from '@mui/material';
import { useTranslation } from 'react-i18next';
import SearchForm from '../Search/SearchForm';
import SearchResultsTable from '../Search/SearchResultsTable';
import PaginationBar from '../../components/PaginationBar';
import useSearch from '../../hooks/useSearch';

export default function ParentDirectoryPage() {
  const { id } = useParams<{ id: string }>();
  const { t } = useTranslation();
  const {
    data: directory,
    isLoading,
    isError,
  } = useGetDirectoryQuery({ directoryId: id });

  useEffect(() => {
    if (id) {
      const newParams = new URLSearchParams(searchParams);
      newParams.set('parentId', id);
      setSearchParams(newParams);
    }
  }, [id]);

  // Use useSearch with parentId
  const {
    searchParams,
    setSearchParams,
    notes,
    directories,
    isLoadingData,
    showNotes,
    showDirectories,
    currentPage,
    pageSize,
    totalPages,
    totalCount,
  } = useSearch(false); // No userData needed

  // Extract individual search fields from searchParams
  const searchFields = {
    institutionId: '', // Hidden in this context
    careerId: '', // Hidden in this context
    subjectId: '', // Hidden in this context
    parentId: id || '', // Added to filter by parentId
    word: searchParams.get('word') || '',
    category: (searchParams.get('category') as 'note' | 'directory') || 'note',
    sortBy: searchParams.get('sortBy') || 'modified',
    asc: searchParams.get('asc') === 'true',
  };

  const handleSearchChange = (params: Record<string, string>) => {
    const newParams = new URLSearchParams(searchParams);

    Object.entries(params).forEach(([key, value]) => {
      if (value !== undefined && value !== null && value !== '') {
        newParams.set(key, value);
      } else {
        newParams.delete(key);
      }
    });

    // Ensure to keep the parentId
    if (id) {
      newParams.set('parentId', id);
    } else {
      newParams.delete('parentId');
    }

    // Reset to the first page when changing search/filter
    newParams.set('page', '1');

    setSearchParams(newParams);
  };

  if (isLoading || isLoadingData) {
    return (
      <Box display="flex" justifyContent="center" mt={5}>
        <CircularProgress />
      </Box>
    );
  }

  if (isError || !directory) {
    return (
      <Box display="flex" justifyContent="center" mt={5}>
        <Alert severity="error">
          {t('parentDirectoryPage.errorFetchingDirectory')}
        </Alert>
      </Box>
    );
  }

  return (
    <Box sx={{ p: 2, maxWidth: 1200, margin: '0 auto' }}>
      <Typography variant="h4" sx={{ mb: 3 }}>
        {t('parentDirectoryPage.title')}
      </Typography>
      <Typography variant="body1" sx={{ mb: 3 }}>
        {t('parentDirectoryPage.description')}
      </Typography>
      <Box mt={2}>
        <MuiLink
          component={Link}
          to={`/directories/${directory.parentUrl}`}
          underline="hover"
        >
          {directory.name}
        </MuiLink>
      </Box>

      {/* Search Component */}
      <SearchForm
        searchFields={searchFields}
        onSearch={handleSearchChange}
        hideInstitution
        hideCareer
        hideSubject
      />

      {/* Search Results */}
      {isLoadingData ? (
        <Box sx={{ display: 'flex', justifyContent: 'center', mt: 3 }}>
          <CircularProgress />
        </Box>
      ) : (
        <>
          {(showNotes && notes.length === 0) ||
          (showDirectories && directories.length === 0) ? (
            <Typography variant="body1" sx={{ mt: 4, textAlign: 'center' }}>
              {t('parentDirectoryPage.search.noContent')}
            </Typography>
          ) : (
            <SearchResultsTable
              showNotes={showNotes}
              showDirectories={showDirectories}
              notes={notes}
              directories={directories}
            />
          )}

          {totalPages > 1 && (
            <PaginationBar
              currentPage={currentPage}
              pageSize={pageSize}
              totalPages={totalPages}
              totalCount={totalCount}
            />
          )}
        </>
      )}
    </Box>
  );
}
