// src/pages/Directories/DirectoryPage.tsx

import { useEffect, useState } from 'react';
import { useParams, Link as RouterLink, useNavigate } from 'react-router-dom';
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
  Breadcrumbs,
  IconButton,
  Menu,
  MenuItem,
} from '@mui/material';
import NavigateNextIcon from '@mui/icons-material/NavigateNext';
import MoreHorizIcon from '@mui/icons-material/MoreHoriz';
import { useTranslation } from 'react-i18next';
import SearchForm from '../Search/SearchForm';
import SearchResultsTable from '../Search/SearchResultsTable';
import PaginationBar from '../../components/PaginationBar';
import useSearch from '../../hooks/useSearch';
import CreateNoteFab from '../../components/CreateNoteFab';
import CreateDirectoryFab from '../../components/CreateDirectoryFab';
import useDirectoryBreadcrumb from '../../hooks/useDirectoryBreadcrumb';
import { Directory } from '../../types';

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

  // Use the custom breadcrumb hook
  const {
    breadcrumb,
    loading: isBreadcrumbLoading,
    error: isBreadcrumbError,
  } = useDirectoryBreadcrumb({
    currentDirectory: currentDirectory as Directory,
  });

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

  // Breadcrumb state for menu
  const [anchorEl, setAnchorEl] = useState<null | HTMLElement>(null);
  const handleMenuOpen = (event: React.MouseEvent<HTMLElement>) => {
    setAnchorEl(event.currentTarget);
  };
  const handleMenuClose = () => {
    setAnchorEl(null);
  };

  if (
    isLoadingCurrentDirectory ||
    isBreadcrumbLoading ||
    isLoadingData ||
    isLoadingOwner
  ) {
    return (
      <Box display="flex" justifyContent="center" mt={5}>
        <CircularProgress />
      </Box>
    );
  }

  if (
    isErrorCurrentDirectory ||
    isBreadcrumbError ||
    isErrorOwner ||
    !currentDirectory
  ) {
    return (
      <Box display="flex" justifyContent="center" mt={5}>
        <Alert severity="error">
          {t('directoryPage.errorFetchingDirectory')}
        </Alert>
      </Box>
    );
  }

  return (
    <Box sx={{ p: 2, maxWidth: 1200, margin: '0 auto' }}>
      {/* Current Directory Name */}
      <Typography variant="h4" sx={{ mb: 3 }}>
        {currentDirectory.name}
      </Typography>

      {/* Breadcrumb */}
      <Box mb={3}>
        {breadcrumb.length == 0 && (
          <Typography color="text.primary">
            {t('directoryPage.breadcrumb.isRoot')}
          </Typography>
        )}
        {breadcrumb.length == 1 && (
          <Breadcrumbs
            separator={<NavigateNextIcon fontSize="small" />}
            aria-label="breadcrumb"
            sx={{ display: 'flex', alignItems: 'center' }}
          >
            {/* Root Ancestor */}
            <MuiLink
              component={RouterLink}
              to={`/directories/${breadcrumb[0].id}`}
              underline="hover"
              color="inherit"
            >
              {breadcrumb[0].name}
            </MuiLink>

            {/* Current Directory Name as Typography */}
            <Typography color="text.primary">
              {currentDirectory.name}
            </Typography>
          </Breadcrumbs>
        )}
        {breadcrumb.length > 1 && (
          <Breadcrumbs
            separator={<NavigateNextIcon fontSize="small" />}
            aria-label="breadcrumb"
            sx={{ display: 'flex', alignItems: 'center' }}
          >
            {/* Root Ancestor */}
            <MuiLink
              component={RouterLink}
              to={`/directories/${breadcrumb[0].id}`}
              underline="hover"
              color="inherit"
            >
              {breadcrumb[0].name}
            </MuiLink>

            {/* Ellipsis */}
            {breadcrumb.length !== 2 && (
              <>
                <IconButton
                  size="small"
                  onClick={handleMenuOpen}
                  aria-controls={anchorEl ? 'breadcrumb-menu' : undefined}
                  aria-haspopup="true"
                  aria-expanded={anchorEl ? 'true' : undefined}
                >
                  <MoreHorizIcon />
                </IconButton>

                <Menu
                  id="breadcrumb-menu"
                  anchorEl={anchorEl}
                  open={Boolean(anchorEl)}
                  onClose={handleMenuClose}
                >
                  {breadcrumb.slice(1, -1).map((dir) => (
                    <MenuItem
                      key={dir.id}
                      onClick={() => {
                        navigate(`/directories/${dir.id}`);
                        handleMenuClose();
                      }}
                    >
                      {dir.name}
                    </MenuItem>
                  ))}
                </Menu>
              </>
            )}

            {/* Immediate Parent */}
            <MuiLink
              component={RouterLink}
              to={`/directories/${breadcrumb[breadcrumb.length - 1].id}`}
              underline="hover"
              color="inherit"
            >
              {breadcrumb[breadcrumb.length - 1].name}
            </MuiLink>

            {/* Current Directory Name as Typography */}
            <Typography color="text.primary">
              {currentDirectory.name}
            </Typography>
          </Breadcrumbs>
        )}
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

      {/* FAB Buttons */}
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
    </Box>
  );
}
