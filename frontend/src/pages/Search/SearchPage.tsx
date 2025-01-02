// src/pages/Search/SearchPage.tsx

import { Box, CircularProgress, Typography } from '@mui/material';
import { Helmet } from 'react-helmet-async';
import { useTranslation } from 'react-i18next';
import { useNavigate } from 'react-router-dom';

import SearchForm from './SearchForm';
import SearchResultsTable from './SearchResultsTable';
import PaginationBar from '../../components/PaginationBar';
import useSearch from '../../hooks/useSearch';

export default function SearchPage() {
  const { t } = useTranslation('searchPage');
  const navigate = useNavigate();

  const {
    searchParams,
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

  const searchFields = {
    institutionId: searchParams.get('institutionId') || '',
    careerId: searchParams.get('careerId') || '',
    subjectId: searchParams.get('subjectId') || '',
    word: searchParams.get('word') || '',
    category: (searchParams.get('category') as 'note' | 'directory') || 'note',
    sortBy: searchParams.get('sortBy') || 'modified',
    asc: searchParams.get('asc') || 'true',
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

    newParams.set('page', '1');

    navigate({ search: newParams.toString() }, { replace: true });
  };

  return (
    <>
      <Helmet>
        <title>{t('titlePage')}</title>
      </Helmet>

      <Box sx={{ p: 2, maxWidth: 1200, margin: '0 auto' }}>
        <Typography variant="h4" sx={{ mb: 3 }}>
          {t('title')}
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
                {t('noContent')}
              </Typography>
            ) : (
              <SearchResultsTable
                showNotes={showNotes}
                showDirectories={showDirectories}
                notes={notes}
                directories={directories}
              />
            )}

            {((showNotes && notes.length > 0) ||
              (showDirectories && directories.length > 0)) && (
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
    </>
  );
}
