// src/pages/Directories/ParentDirectoryPage.tsx

import { useEffect } from 'react';
import { useParams, Link } from 'react-router-dom';
import { useSelector } from 'react-redux';
import { selectCurrentUser } from '../../store/slices/authSlice';
import { useGetDirectoryQuery } from '../../store/slices/directoriesApiSlice';
import { useGetUserQuery } from '../../store/slices/usersApiSlice';
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
import { useNavigate } from 'react-router-dom';
import CreateNoteFab from '../../components/CreateNoteFab';
import CreateDirectoryFab from '../../components/CreateDirectoryFab';

export default function DirectoryPage() {
  const { directoryId } = useParams();
  const { t } = useTranslation();
  const navigate = useNavigate();
  const user = useSelector(selectCurrentUser);

  const {
    data: directory,
    isLoading,
    isError,
  } = useGetDirectoryQuery({ directoryId }, { skip: !directoryId });

  const {
    data: ownerData,
    isLoading: isLoadingOwner,
    isError: isErrorOwner,
  } = useGetUserQuery(
    { url: directory?.ownerUrl },
    { skip: !directory?.ownerUrl },
  );

  const skipParent = !directory?.parentUrl;
  const {
    data: fetchedParentDirectory,
    isLoading: isLoadingParent,
    isError: isErrorParent,
  } = useGetDirectoryQuery(
    {
      url: directory?.parentUrl,
    },
    { skip: skipParent, refetchOnMountOrArgChange: true },
  );
  const parentDirectory = skipParent ? undefined : fetchedParentDirectory;

  useEffect(() => {
    if (directoryId) {
      const newParams = new URLSearchParams(searchParams);
      newParams.set('parentId', directoryId);
      navigate({ search: newParams.toString() }, { replace: true });
    }
  }, [directoryId]);

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

  if (isLoading || isLoadingData || isLoadingParent) {
    return (
      <Box display="flex" justifyContent="center" mt={5}>
        <CircularProgress />
      </Box>
    );
  }

  if (isError || isErrorParent || !directory) {
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
        {directory.name}
      </Typography>
      {parentDirectory && (
        <Box mt={2}>
          <MuiLink
            component={Link}
            to={`/directories/${parentDirectory.id}`}
            underline="hover"
          >
            {parentDirectory.name}
          </MuiLink>
        </Box>
      )}

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
      {directoryId &&
        user &&
        (ownerData?.id === user.id || !directory.parentUrl) && (
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
    </Box>
  );
}
