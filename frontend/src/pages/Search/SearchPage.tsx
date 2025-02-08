// src/pages/Search/SearchPage.tsx

import { Box, CircularProgress, Typography } from '@mui/material';
import { Helmet } from 'react-helmet-async';
import { useTranslation } from 'react-i18next';

import SearchForm from './SearchForm';
import SearchResultsTable from './SearchResultsTable';
import PaginationBar from '../../components/PaginationBar';
import useSearch from '../../hooks/useSearch';

export default function SearchPage() {
  const { t } = useTranslation('searchPage');
  const {
    control,
    watchedValues,
    isLoading,
    notes,
    directories,
    totalCount,
    totalNotes,
    totalDirectories,
    totalPages,
    currentPage,
    pageSize,
  } = useSearch();

  const showNotes = watchedValues.category !== 'directory';
  const showDirectories = watchedValues.category === 'directory';

  return (
    <>
      <Helmet>
        <title>{t('titlePage')}</title>
      </Helmet>

      <Box sx={{ p: 2, maxWidth: 1200, margin: '0 auto' }}>
        <Typography variant="h4" sx={{ mb: 3 }}>
          {t('title')}
        </Typography>

        <SearchForm
          control={control}
          watch={watchedValues}
          totalNotes={totalNotes}
          totalDirectories={totalDirectories}
        />

        {isLoading ? (
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
