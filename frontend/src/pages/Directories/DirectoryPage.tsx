// src/pages/Directories/DirectoryPage.tsx

import { useEffect } from 'react';
import { useParams, useNavigate } from 'react-router-dom';
import { useSelector } from 'react-redux';
import { selectCurrentUser } from '../../store/slices/authSlice';
import { useGetDirectoryQuery } from '../../store/slices/directoriesApiSlice';
import { useGetUserQuery } from '../../store/slices/usersApiSlice';
import { Box, Typography, CircularProgress, Alert } from '@mui/material';
import { useTranslation } from 'react-i18next';
import SearchForm from '../Search/SearchForm';
import SearchResultsTable from '../Search/SearchResultsTable';
import PaginationBar from '../../components/PaginationBar';
import useSearch from '../../hooks/useSearch';
import CreateNoteFab from '../../components/CreateNoteFab';
import CreateDirectoryFab from '../../components/CreateDirectoryFab';
import DirectoryBreadcrumbs from '../../components/DirectoryBreadcrumbs';
import { Helmet } from 'react-helmet-async';

export default function DirectoryPage() {
  const { directoryId } = useParams<{ directoryId: string }>();
  const { t } = useTranslation();
  const navigate = useNavigate();
  const user = useSelector(selectCurrentUser);

  // Fetch the current directory
  const {
    data: currentDirectory,
    isLoading: isLoadingCurrentDirectory,
    isError: isErrorCurrentDirectory,
  } = useGetDirectoryQuery({ directoryId }, { skip: !directoryId });

  const ownerUrl = currentDirectory?.ownerUrl || '';
  const {
    data: ownerData,
    isLoading: isLoadingOwner,
    isError: isErrorOwner,
  } = useGetUserQuery({ url: ownerUrl }, { skip: !ownerUrl });

  useEffect(() => {
    if (directoryId) {
      const newParams = new URLSearchParams(window.location.search);
      newParams.set('parentId', directoryId);
      navigate({ search: newParams.toString() }, { replace: true });
    }
  }, [directoryId, navigate]);

  // Use useSearch with parentId
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
  } = useSearch(false); // No userData needed

  // Extract individual search fields from searchParams
  const searchFields = {
    institutionId: '', // Hidden in this context
    careerId: '', // Hidden in this context
    subjectId: '', // Hidden in this context
    parentId: directoryId || '', // Added to filter by parentId
    word: searchParams.get('word') || '',
    category: (searchParams.get('category') as 'note' | 'directory') || 'note',
    sortBy: searchParams.get('sortBy') || 'modified',
    asc: searchParams.get('asc') || 'true',
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
    if (directoryId) {
      newParams.set('parentId', directoryId);
    } else {
      newParams.delete('parentId');
    }

    // Reset to the first page when changing search/filter
    newParams.set('page', '1');

    navigate({ search: newParams.toString() }, { replace: true });
  };

  let pageTitle = t('directoryPage.titlePage');
  if (isLoadingCurrentDirectory || isLoadingData || isLoadingOwner) {
    pageTitle = t('directoryPage.loading');
  } else if (isErrorCurrentDirectory || isErrorOwner) {
    pageTitle = t('directoryPage.errorFetchingDirectory');
  }

  return (
    <>
      <Helmet>
        <title>{pageTitle}</title>
      </Helmet>
      <Box sx={{ p: 2, maxWidth: 1200, margin: '0 auto' }}>
        {/* If loading or there is an error */}
        {(isLoadingCurrentDirectory || isLoadingData || isLoadingOwner) && (
          <Box sx={{ display: 'flex', justifyContent: 'center', mt: 5 }}>
            <CircularProgress />
          </Box>
        )}

        {(isErrorCurrentDirectory || isErrorOwner) && (
          <Box sx={{ display: 'flex', justifyContent: 'center', mt: 5 }}>
            <Alert severity="error">
              {t('directoryPage.errorFetchingDirectory')}
            </Alert>
          </Box>
        )}

        {/* Main content when not loading and no error */}
        {!isLoadingCurrentDirectory &&
          !isLoadingData &&
          !isLoadingOwner &&
          !isErrorCurrentDirectory &&
          !isErrorOwner &&
          currentDirectory && (
            <>
              {/* Directory Title */}
              <Typography variant="h4">{currentDirectory.name}</Typography>

              {/* Directory Breadcrumbs */}
              <DirectoryBreadcrumbs currentDirectory={currentDirectory} />

              <Box sx={{ mt: 3 }}>
                {/* Search Component */}
                <SearchForm
                  searchFields={searchFields}
                  onSearch={handleSearchChange}
                  hideInstitution
                  hideCareer
                  hideSubject
                />
              </Box>

              {/* Search Results */}
              {(showNotes || showDirectories) && (
                <>
                  {notes.length === 0 && directories.length === 0 ? (
                    <Typography
                      variant="body1"
                      sx={{ mt: 4, textAlign: 'center' }}
                    >
                      {t('directoryPage.search.noContent')}
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

              {/* FAB buttons to create notes or directories */}
              {directoryId &&
                user &&
                (ownerData?.id === user.id || !currentDirectory.parentUrl) && (
                  <Box
                    sx={{
                      display: 'flex',
                      flexDirection: 'column',
                      alignItems: 'flex-end',
                      position: 'fixed',
                      bottom: 32,
                      right: 32,
                      zIndex: 1300,
                      gap: 2,
                    }}
                  >
                    <CreateNoteFab parentId={directoryId} />
                    <CreateDirectoryFab parentId={directoryId} />
                  </Box>
                )}
            </>
          )}
      </Box>
    </>
  );
}
