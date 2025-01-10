// src/pages/Directories/DirectoryPage.tsx

import EditIcon from '@mui/icons-material/Edit';
import {
  Box,
  Typography,
  CircularProgress,
  Alert,
  IconButton,
} from '@mui/material';
import { useEffect, useState } from 'react';
import { Helmet } from 'react-helmet-async';
import { useTranslation } from 'react-i18next';
import { useSelector } from 'react-redux';
import { useParams, useNavigate } from 'react-router-dom';

import EditDirectoryDialog from './dialogs/EditDirectoryDialog';
import CreateDirectoryFab from '../../components/CreateDirectoryFab';
import CreateNoteFab from '../../components/CreateNoteFab';
import DirectoryBreadcrumbs from '../../components/DirectoryBreadcrumbs';
import PaginationBar from '../../components/PaginationBar';
import useSearch from '../../hooks/useSearch';
import { selectCurrentUser } from '../../store/slices/authSlice';
import { useLazyGetDirectoryQuery } from '../../store/slices/directoriesApiSlice';
import { useLazyGetUserQuery } from '../../store/slices/usersApiSlice';
import SearchForm from '../Search/SearchForm';
import SearchResultsTable from '../Search/SearchResultsTable';

export default function DirectoryPage() {
  const { directoryId } = useParams<{ directoryId: string }>();
  const { t } = useTranslation('directoryPage');
  const navigate = useNavigate();
  const user = useSelector(selectCurrentUser);
  const [openEditDialog, setOpenEditDialog] = useState(false);
  const [
    getDirectory,
    {
      data: directory,
      isError: isErrorCurrentDirectory,
      isLoading: isLoadingCurrentDirectory,
    },
  ] = useLazyGetDirectoryQuery();
  const [
    getUser,
    { data: owner, isError: isErrorOwner, isLoading: isLoadingOwner },
  ] = useLazyGetUserQuery();

  useEffect(() => {
    if (directoryId) {
      getDirectory({ directoryId });
    }
  }, [directoryId, getDirectory]);

  useEffect(() => {
    const fetchUser = async () => {
      if (directory?.ownerUrl) {
        await getUser({ url: directory?.ownerUrl });
      }
    };
    fetchUser();
  }, [directory, getUser]);

  const {
    control,
    watchedValues,
    isLoading,
    notes,
    directories,
    totalCount,
    totalPages,
    currentPage,
    pageSize,
  } = useSearch(directoryId!);

  const showNotes = watchedValues.category !== 'directory';
  const showDirectories = watchedValues.category === 'directory';

  const handleEditClick = () => {
    setOpenEditDialog(true);
  };

  const handleCloseDialog = () => {
    setOpenEditDialog(false);
  };

  // Determine the page title based on the state
  let pageTitle = t('loading');
  if (isErrorCurrentDirectory || isErrorOwner) {
    pageTitle = t('errorFetchingDirectory');
  } else if (directory) {
    pageTitle = t('titlePage', { directoryName: directory.name });
  }

  const onSuccessDirectoryCreation = () => {
    if (watchedValues.category !== 'directory') {
      navigate(`/directories/${directoryId}?category=directory`);
    }
  };

  const onSuccessNoteCreation = () => {
    if (watchedValues.category === 'directory') {
      navigate(`/directories/${directoryId}`);
    }
  };

  return (
    <>
      <Helmet>
        <title>{pageTitle}</title>
      </Helmet>
      <Box sx={{ p: 2, maxWidth: 1200, margin: '0 auto' }}>
        {/* If loading or there is an error */}
        {(isLoadingCurrentDirectory || isLoadingOwner || isLoading) && (
          <Box sx={{ display: 'flex', justifyContent: 'center', mt: 5 }}>
            <CircularProgress />
          </Box>
        )}

        {(isErrorCurrentDirectory || isErrorOwner) && (
          <Box sx={{ display: 'flex', justifyContent: 'center', mt: 5 }}>
            <Alert severity="error">{t('errorFetchingDirectory')}</Alert>
          </Box>
        )}

        {/* Main content when not loading and no error */}
        {!isLoadingCurrentDirectory &&
          !isLoadingOwner &&
          !isLoading &&
          !isErrorCurrentDirectory &&
          !isErrorOwner &&
          directory && (
            <>
              {/* Directory Title */}
              <Box sx={{ display: 'flex', alignItems: 'center' }}>
                <Typography variant="h4">{directory.name}</Typography>
                {owner && owner.id === user.id && (
                  <IconButton onClick={handleEditClick}>
                    <EditIcon />
                  </IconButton>
                )}
              </Box>

              {/* Directory Breadcrumbs */}
              <DirectoryBreadcrumbs currentDirectory={directory} />

              <Box sx={{ mt: 3 }}>
                {/* Search Component */}
                <SearchForm
                  control={control}
                  watch={watchedValues}
                  hideInstitution
                  hideCareer
                  hideSubject
                />
              </Box>

              {/* Search Results */}
              {(showNotes || showDirectories) && (
                <>
                  {(showNotes && notes.length === 0) ||
                  (showDirectories && directories.length === 0) ? (
                    <Typography
                      variant="body1"
                      sx={{ mt: 4, textAlign: 'center' }}
                    >
                      {t('noResults')}
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
                (owner?.id === user.id || !directory.parentUrl) && (
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
                    <CreateNoteFab
                      parentId={directoryId}
                      onSuccess={onSuccessNoteCreation}
                    />
                    <CreateDirectoryFab
                      parentId={directoryId}
                      onSuccess={onSuccessDirectoryCreation}
                    />
                  </Box>
                )}

              {/* Edit Directory Dialog */}
              <EditDirectoryDialog
                open={openEditDialog}
                onClose={handleCloseDialog}
                directory={directory}
                showNameOnly
              />
            </>
          )}
      </Box>
    </>
  );
}
