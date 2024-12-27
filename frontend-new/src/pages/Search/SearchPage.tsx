// src/pages/Search/SearchPage.tsx

import { Box, CircularProgress, Typography } from '@mui/material';
import { useTranslation } from 'react-i18next';
import SearchForm from './SearchForm';
import SearchResultsTable from './SearchResultsTable';
import PaginationBar from '../../components/PaginationBar';
import useSearch from '../../hooks/useSearch';

/**
 * Search Page:
 *  1) Check user default institution/career from useUserData.
 *  2) If the URL has no institutionId or careerId, set them from user data.
 *  3) Then useSearch reads from the final URL params to fetch notes/dirs.
 *  4) Display the triple chain selects + results table + pagination.
 */
export default function SearchPage() {
  const { t } = useTranslation();

  // 2) Now useSearch reads from final URL param
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
  } = useSearch();

  // Extract individual search fields from searchParams
  const searchFields = {
    institutionId: searchParams.get('institutionId') || '',
    careerId: searchParams.get('careerId') || '',
    subjectId: searchParams.get('subjectId') || '',
    word: searchParams.get('word') || '',
    category: (searchParams.get('category') as 'note' | 'directory') || 'note',
    sortBy: searchParams.get('sortBy') || 'modified',
    asc: searchParams.get('asc') === 'true',
    parentId: searchParams.get('parentId') || '',
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

    // Reset to first page on search/filter change
    newParams.set('page', '1');

    setSearchParams(newParams);
  };

  return (
    <Box sx={{ p: 2, maxWidth: 1200, margin: '0 auto' }}>
      <Typography variant="h4" sx={{ mb: 3 }}>
        {t('searchPage.title')}
      </Typography>

      <SearchForm searchFields={searchFields} onSearch={handleSearchChange} />
      {isLoadingData ? (
        <Box sx={{ display: 'flex', justifyContent: 'center' }}>
          <CircularProgress />
        </Box>
      ) : (
        <>
          {(showNotes && notes.length === 0) ||
          (showDirectories && directories.length === 0) ? (
            <Typography variant="body1" sx={{ mt: 4, textAlign: 'center' }}>
              {t('searchPage.noContent')}
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
