import EditIcon from '@mui/icons-material/Edit';
import {
  Box,
  Typography,
  CircularProgress,
  Alert,
  IconButton,
} from '@mui/material';
import { useState } from 'react';
import { Helmet } from 'react-helmet-async';
import { useTranslation } from 'react-i18next';
import { useSelector } from 'react-redux';
import { useParams } from 'react-router-dom';

import EditDirectoryDialog from './dialogs/EditDirectoryDialog';
import CreateDirectoryFab from '../../components/CreateDirectoryFab';
import CreateNoteFab from '../../components/CreateNoteFab';
import DirectoryBreadcrumbs from '../../components/DirectoryBreadcrumbs';
import PaginationBar from '../../components/PaginationBar';
import useSearch from '../../hooks/useSearch';
import { selectCurrentUser } from '../../store/slices/authSlice';
import { useGetDirectoryQuery } from '../../store/slices/directoriesApiSlice';
import { useGetUserQuery } from '../../store/slices/usersApiSlice';
import SearchForm from '../Search/SearchForm';
import SearchResultsTable from '../Search/SearchResultsTable';

export default function DirectoryPage() {
  const { directoryId } = useParams<{ directoryId: string }>();
  const { t } = useTranslation('directoryPage');
  const user = useSelector(selectCurrentUser);
  const [openEditDialog, setOpenEditDialog] = useState(false);

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

  const isOwner = user?.id === ownerData?.id;

  const {
    control,
    watchedValues,
    setValue,
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
  let pageTitle = t('titlePage');
  if (isLoadingCurrentDirectory || isLoadingOwner || isLoading) {
    pageTitle = t('loading');
  } else if (isErrorCurrentDirectory || isErrorOwner) {
    pageTitle = t('errorFetchingDirectory');
  }

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
          currentDirectory && (
            <>
              {/* Directory Title */}
              <Box sx={{ display: 'flex', alignItems: 'center' }}>
                <Typography variant="h4">{currentDirectory.name}</Typography>
                {isOwner && (
                  <IconButton onClick={handleEditClick}>
                    <EditIcon />
                  </IconButton>
                )}
              </Box>

              {/* Directory Breadcrumbs */}
              <DirectoryBreadcrumbs currentDirectory={currentDirectory} />

              <Box sx={{ mt: 3 }}>
                {/* Search Component */}
                <SearchForm
                  control={control}
                  watch={watchedValues}
                  setValue={setValue}
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
                      setValue={setValue}
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

              {/* Edit Directory Dialog */}
              <EditDirectoryDialog
                open={openEditDialog}
                onClose={handleCloseDialog}
                directory={currentDirectory}
                showNameOnly
              />
            </>
          )}
      </Box>
    </>
  );
}
