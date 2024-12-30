// src/pages/Favorites/FavoritesPage.tsx

import {
  Box,
  Button,
  Typography,
  CircularProgress,
  Stack,
  IconButton,
} from '@mui/material';
import RefreshIcon from '@mui/icons-material/Refresh';
import { useSearchParams } from 'react-router-dom';
import { useTranslation } from 'react-i18next';
import { useSelector } from 'react-redux';
import { selectCurrentUser } from '../../store/slices/authSlice';
import PaginationBar from '../../components/PaginationBar';
import FavoriteNoteCard from '../../components/FavoriteNoteCard';
import FavoriteDirectoryCard from '../../components/FavoriteDirectoryCard';
import { useGetUserNotesFavoritesQuery } from '../../store/slices/notesApiSlice';
import { useGetUserDirectoriesFavoritesQuery } from '../../store/slices/directoriesApiSlice';
import { Note, Directory } from '../../types';

const DEFAULT_PAGE_SIZE = 10;

export default function FavoritesPage() {
  const { t } = useTranslation();
  const user = useSelector(selectCurrentUser);
  const userId = user?.id;

  const [searchParams, setSearchParams] = useSearchParams();
  const favtype = searchParams.get('favtype') || 'notes'; // 'notes', 'directories', or 'subjects'
  const page = parseInt(searchParams.get('page') || '1', 10);
  const pageSize = parseInt(
    searchParams.get('pageSize') || String(DEFAULT_PAGE_SIZE),
    10,
  );

  if (!userId) {
    return (
      <Box sx={{ p: 2 }}>
        <Typography variant="h6" color="error">
          {t('favoritesPage.mustLogin')}
        </Typography>
      </Box>
    );
  }

  // Query args
  const notesQueryArgs = { userId, page, pageSize };
  const dirsQueryArgs = { userId, page, pageSize, rdir: 'false' };
  const subjectsQueryArgs = { userId, page, pageSize, rdir: 'true' };

  // Favorite notes
  const {
    data: notesResult,
    isLoading: notesLoading,
    isError: notesError,
    refetch: refetchNotes,
  } = useGetUserNotesFavoritesQuery(notesQueryArgs, {
    skip: favtype !== 'notes',
  });

  // Favorite directories
  const {
    data: dirsResult,
    isLoading: dirsLoading,
    isError: dirsError,
    refetch: refetchDirs,
  } = useGetUserDirectoriesFavoritesQuery(dirsQueryArgs, {
    skip: favtype !== 'directories',
  });

  // Favorite subjects
  const {
    data: subjectsResult,
    isLoading: subjectsLoading,
    isError: subjectsError,
    refetch: refetchSubjects,
  } = useGetUserDirectoriesFavoritesQuery(subjectsQueryArgs, {
    skip: favtype !== 'subjects',
  });

  // Gestión de estados de carga y error
  const isLoading =
    (favtype === 'notes' && notesLoading) ||
    (favtype === 'directories' && dirsLoading) ||
    (favtype === 'subjects' && subjectsLoading);

  const isError =
    (favtype === 'notes' && notesError) ||
    (favtype === 'directories' && dirsError) ||
    (favtype === 'subjects' && subjectsError);

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

  // Toggle favorite type
  const handleFavTypeChange = (newType: string) => {
    const newParams = new URLSearchParams(searchParams);
    newParams.set('favtype', newType);
    newParams.set('page', '1');
    setSearchParams(newParams);
  };

  // Refresh current type
  const handleRefresh = () => {
    if (favtype === 'notes') {
      refetchNotes();
    } else if (favtype === 'directories') {
      refetchDirs();
    } else if (favtype === 'subjects') {
      refetchSubjects();
    }
  };

  if (isLoading) {
    return (
      <Box sx={{ p: 2 }}>
        <CircularProgress />
      </Box>
    );
  }
  if (isError) {
    return (
      <Box sx={{ p: 2 }}>
        <Typography color="error">
          {t('favoritesPage.errorFetching')}
        </Typography>
      </Box>
    );
  }

  return (
    <Box sx={{ p: 2 }}>
      {/* Title */}
      <Typography variant="h4" sx={{ mb: 3 }}>
        {t('favoritesPage.title')}
      </Typography>

      {/* Row of buttons: notes / directories / subjects + refresh */}
      <Stack direction="row" spacing={2} sx={{ mb: 3 }}>
        <Button
          variant={favtype === 'notes' ? 'contained' : 'outlined'}
          onClick={() => handleFavTypeChange('notes')}
        >
          {t('favoritesPage.notes')}
        </Button>
        <Button
          variant={favtype === 'directories' ? 'contained' : 'outlined'}
          onClick={() => handleFavTypeChange('directories')}
        >
          {t('favoritesPage.directories')}
        </Button>
        <Button
          variant={favtype === 'subjects' ? 'contained' : 'outlined'}
          onClick={() => handleFavTypeChange('subjects')}
        >
          {t('favoritesPage.subjects')}
        </Button>

        {/* Refresh button */}
        <IconButton onClick={handleRefresh} sx={{ ml: 'auto' }}>
          <RefreshIcon />
        </IconButton>
      </Stack>

      {/* No favorites */}
      {favtype === 'notes' && items.length === 0 && (
        <Typography>{t('favoritesPage.noFavorites')}</Typography>
      )}
      {favtype === 'directories' && items.length === 0 && (
        <Typography>{t('favoritesPage.noFavorites')}</Typography>
      )}
      {favtype === 'subjects' && items.length === 0 && (
        <Typography>{t('favoritesPage.noFavorites')}</Typography>
      )}

      {/* Items */}
      {items.length > 0 && (
        <Box
          sx={{
            display: 'grid',
            gridTemplateColumns: 'repeat(auto-fill, minmax(120px, 180px))',
            gap: 2,
            justifyContent: 'center',
            mb: 4,
          }}
        >
          {favtype === 'notes'
            ? items.map((note: Note) => (
                <FavoriteNoteCard key={note.id} note={note} userId={userId} />
              ))
            : items.map((dir: Directory) => (
                <FavoriteDirectoryCard
                  key={dir.id}
                  directory={dir}
                  userId={userId}
                />
              ))}
        </Box>
      )}

      {/* Pagination */}
      {items.length > 0 && (
        <PaginationBar
          currentPage={page}
          pageSize={pageSize}
          totalPages={totalPages}
          totalCount={totalCount}
        />
      )}
    </Box>
  );
}
