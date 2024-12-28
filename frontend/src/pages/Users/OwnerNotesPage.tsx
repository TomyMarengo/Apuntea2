// src/pages/Users/OwnerNotesPage.tsx

import { useParams } from 'react-router-dom';
import { useSearchNotesQuery } from '../../store/slices/searchApiSlice';
import { Box, Typography, CircularProgress, Alert, Grid } from '@mui/material';
import { useTranslation } from 'react-i18next';
import SearchResultsTable from '../Search/SearchResultsTable';

export default function OwnerNotesPage() {
  const { ownerId } = useParams<{ ownerId: string }>();
  const { t } = useTranslation();
  const { data, isLoading, isError } = useSearchNotesQuery({
    userId: ownerId,
  });

  if (isLoading) {
    return (
      <Box display="flex" justifyContent="center" mt={5}>
        <CircularProgress />
      </Box>
    );
  }

  if (isError || !data) {
    return (
      <Box display="flex" justifyContent="center" mt={5}>
        <Alert severity="error">{t('errorFetchingOwnerNotes')}</Alert>
      </Box>
    );
  }

  return (
    <Box p={3}>
      <Typography variant="h4" gutterBottom>
        {t('ownerNotes.title')}
      </Typography>
      <Typography variant="body1" gutterBottom>
        {t('ownerNotes.description')}
      </Typography>
      <SearchResultsTable
        showNotes={true}
        showDirectories={false}
        notes={data.notes}
        directories={[]}
      />
    </Box>
  );
}
