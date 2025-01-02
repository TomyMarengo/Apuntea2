// src/pages/Favorites/FavoritesPage.tsx

import RefreshIcon from '@mui/icons-material/Refresh';
import {
  Box,
  Button,
  Typography,
  CircularProgress,
  Stack,
  IconButton,
} from '@mui/material';
import { Helmet } from 'react-helmet-async';
import { useTranslation } from 'react-i18next';
import { useSelector } from 'react-redux';
import { useSearchParams } from 'react-router-dom';

import FavoriteDirectoryCard from '../../components/FavoriteDirectoryCard';
import FavoriteNoteCard from '../../components/FavoriteNoteCard';
import PaginationBar from '../../components/PaginationBar';
import { selectCurrentUser } from '../../store/slices/authSlice';
import { useGetUserDirectoriesFavoritesQuery } from '../../store/slices/directoriesApiSlice';
import { useGetUserNotesFavoritesQuery } from '../../store/slices/notesApiSlice';
import { Note, Directory } from '../../types';

const DEFAULT_PAGE_SIZE = 10;

export default function FavoritesPage() {
  // Initialize translation with the 'favoritesPage' namespace
  const { t } = useTranslation('favoritesPage');
  const user = useSelector(selectCurrentUser);
  const userId = user?.id;

  const [searchParams, setSearchParams] = useSearchParams();
  const favtype = searchParams.get('favtype') || 'notes'; // 'notes', 'directories', or 'subjects'
  const page = parseInt(searchParams.get('page') || '1', 10);
  const pageSize = parseInt(
    searchParams.get('pageSize') || String(DEFAULT_PAGE_SIZE),
    10,
  );

  // Query arguments based on favorite type
  const notesQueryArgs = { userId, page, pageSize };
  const dirsQueryArgs = { userId, page, pageSize, rdir: 'false' };
  const subjectsQueryArgs = { userId, page, pageSize, rdir: 'true' };

  // Fetch favorite notes
  const {
    data: notesResult,
    isLoading: notesLoading,
    isError: notesError,
    refetch: refetchNotes,
  } = useGetUserNotesFavoritesQuery(notesQueryArgs, {
    skip: favtype !== 'notes',
  });

  // Fetch favorite directories
  const {
    data: dirsResult,
    isLoading: dirsLoading,
    isError: dirsError,
    refetch: refetchDirs,
  } = useGetUserDirectoriesFavoritesQuery(dirsQueryArgs, {
    skip: favtype !== 'directories',
  });

  // Fetch favorite subjects
  const {
    data: subjectsResult,
    isLoading: subjectsLoading,
    isError: subjectsError,
    refetch: refetchSubjects,
  } = useGetUserDirectoriesFavoritesQuery(subjectsQueryArgs, {
    skip: favtype !== 'subjects',
  });

  // Manage loading and error states
  const isLoading =
    (favtype === 'notes' && notesLoading) ||
    (favtype === 'directories' && dirsLoading) ||
    (favtype === 'subjects' && subjectsLoading);

  const isError =
    (favtype === 'notes' && notesError) ||
    (favtype === 'directories' && dirsError) ||
    (favtype === 'subjects' && subjectsError);

  // Determine items and pagination based on favorite type
  let items: any[] = [];
  let totalCount = 0;
  let totalPages = 1;

  if (favtype === 'notes' && notesResult) {
    items = notesResult.notes;
    totalCount = notesResult.totalCount;
    totalPages = notesResult.totalPages;
  } else if (favtype === 'directories' && dirsResult) {
    items = dirsResult.directories;
    totalCount = dirsResult.totalCount;
    totalPages = dirsResult.totalPages;
  } else if (favtype === 'subjects' && subjectsResult) {
    items = subjectsResult.directories;
    totalCount = subjectsResult.totalCount;
    totalPages = subjectsResult.totalPages;
  }

  // Toggle favorite type and update URL parameters
  const handleFavTypeChange = (newType: string) => {
    const newParams = new URLSearchParams(searchParams);
    newParams.set('favtype', newType);
    newParams.set('page', '1');
    setSearchParams(newParams);
  };

  // Refresh the current favorite type data
  const handleRefresh = () => {
    if (favtype === 'notes') {
      refetchNotes();
    } else if (favtype === 'directories') {
      refetchDirs();
    } else if (favtype === 'subjects') {
      refetchSubjects();
    }
  };

  // Determine the page title based on the state
  let pageTitle = t('titlePage');
  if (isLoading) {
    pageTitle = t('loading');
  } else if (isError) {
    pageTitle = t('errorFetching');
  }

  return (
    <>
      <Helmet>
        <title>{pageTitle}</title>
      </Helmet>
      <Box sx={{ p: 2 }}>
        {/* Title */}
        <Typography variant="h4" sx={{ mb: 3 }}>
          {t('title')}
        </Typography>

        {/* Button row: notes / directories / subjects + refresh */}
        <Stack direction="row" spacing={2} sx={{ mb: 3 }}>
          <Button
            variant={favtype === 'notes' ? 'contained' : 'outlined'}
            onClick={() => handleFavTypeChange('notes')}
          >
            {t('notes')}
          </Button>
          <Button
            variant={favtype === 'directories' ? 'contained' : 'outlined'}
            onClick={() => handleFavTypeChange('directories')}
          >
            {t('directories')}
          </Button>
          <Button
            variant={favtype === 'subjects' ? 'contained' : 'outlined'}
            onClick={() => handleFavTypeChange('subjects')}
          >
            {t('subjects')}
          </Button>

          {/* Refresh button aligned to the end */}
          <IconButton onClick={handleRefresh} sx={{ ml: 'auto' }}>
            <RefreshIcon />
          </IconButton>
        </Stack>

        {/* Loading message */}
        {isLoading && (
          <Box sx={{ textAlign: 'center', my: 4 }}>
            <CircularProgress />
          </Box>
        )}

        {/* Error message */}
        {isError && (
          <Box sx={{ textAlign: 'center', my: 4 }}>
            <Typography color="error">{t('errorFetching')}</Typography>
          </Box>
        )}

        {/* No favorites */}
        {!isLoading && !isError && items.length === 0 && (
          <Typography textAlign="center" sx={{ mt: 4 }}>
            {t('noFavorites')}
          </Typography>
        )}

        {/* Items */}
        {!isLoading && !isError && items.length > 0 && (
          <>
            <Box
              sx={{
                display: 'grid',
                gridTemplateColumns: 'repeat(auto-fill, minmax(240px, 1fr))',
                gap: 2,
                mb: 4,
              }}
            >
              {favtype === 'notes'
                ? items.map((note: Note) => (
                    <FavoriteNoteCard
                      key={note.id}
                      note={note}
                      userId={userId}
                    />
                  ))
                : items.map((dir: Directory) => (
                    <FavoriteDirectoryCard
                      key={dir.id}
                      directory={dir}
                      userId={userId}
                    />
                  ))}
            </Box>

            {/* Pagination */}
            <PaginationBar
              currentPage={page}
              pageSize={pageSize}
              totalPages={totalPages}
              totalCount={totalCount}
            />
          </>
        )}
      </Box>
    </>
  );
}
