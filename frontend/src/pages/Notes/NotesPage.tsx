// src/pages/Notes/NotesPage.tsx

import {
  Box,
  CircularProgress,
  Typography,
  Select,
  MenuItem,
  FormControl,
  InputLabel,
  SelectChangeEvent,
} from '@mui/material';
import { useEffect, useMemo } from 'react';
import { Helmet } from 'react-helmet-async';
import { useForm, Controller } from 'react-hook-form';
import { useTranslation } from 'react-i18next';
import { useSelector } from 'react-redux';
import { useNavigate, useSearchParams } from 'react-router-dom';

import { selectCurrentUser } from '../../store/slices/authSlice';
import UserNotes from '../Users/UserNotes';

export default function NotesPage() {
  const { t } = useTranslation('notesPage');
  const [searchParams] = useSearchParams();
  const navigate = useNavigate();

  // Current logged user
  const user = useSelector(selectCurrentUser);
  const userId = user?.id;

  const defaultValues = useMemo(
    () => ({
      filter: searchParams.get('filter') || 'myNotes',
      page: searchParams.get('page') || '1',
    }),
    [searchParams],
  );

  const { control, watch, reset } = useForm({
    defaultValues,
  });

  const watchedValues = watch();
  const filter = watchedValues.filter;

  useEffect(() => {
    reset(defaultValues);
  }, [searchParams]);

  const onFilterChange = (e: SelectChangeEvent<string>) => {
    const newParams = new URLSearchParams(window.location.search);
    newParams.set('filter', e.target.value as string);
    navigate(`?${newParams.toString()}`);
  };

  const notesUserId = watchedValues.filter === 'myNotes' ? userId : undefined;

  return (
    <>
      <Helmet>
        <title>{t('titlePage')}</title>
      </Helmet>
      {!userId || !user?.career ? (
        <Box display="flex" justifyContent="center" alignItems="center">
          <CircularProgress />
        </Box>
      ) : (
        <Box sx={{ p: 3 }}>
          <Box
            sx={{
              display: 'flex',
              justifyContent: 'space-between',
              alignItems: 'center',
              mb: 3,
              gap: 2,
            }}
          >
            <Typography variant="h5">{t('title')}</Typography>

            <FormControl variant="outlined" sx={{ minWidth: 200 }}>
              <InputLabel id="filter-select-label">
                {t('filterLabel')}
              </InputLabel>
              <Controller
                name="filter"
                control={control}
                render={({ field }) => (
                  <Select
                    {...field}
                    labelId="filter-select-label"
                    value={filter}
                    onChange={onFilterChange}
                    label={t('filterLabel')}
                  >
                    <MenuItem value="myNotes">{t('myNotes')}</MenuItem>
                    <MenuItem value="allNotes">{t('allNotes')}</MenuItem>
                  </Select>
                )}
              />
            </FormControl>
          </Box>
          {/* Title */}
          <UserNotes userId={notesUserId} career={user?.career} />
        </Box>
      )}
    </>
  );
}
