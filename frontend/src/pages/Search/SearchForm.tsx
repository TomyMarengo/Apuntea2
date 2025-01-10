// src/pages/Search/SearchForm.tsx

import { Close } from '@mui/icons-material';
import {
  Box,
  TextField,
  FormControl,
  InputLabel,
  Select,
  MenuItem,
  IconButton,
  InputAdornment,
  Button,
  Autocomplete,
  SelectChangeEvent,
} from '@mui/material';
import { useEffect, useCallback, useRef } from 'react';
import { Controller, Control } from 'react-hook-form';
import { useTranslation } from 'react-i18next';
import { useNavigate } from 'react-router-dom';

import { SearchFormValues } from './searchSchema';
import {
  useGetInstitutionsQuery,
  useGetCareersQuery,
  useGetSubjectsByCareerQuery,
} from '../../store/slices/institutionsApiSlice';
import { NoteCategory, Institution, Career, Subject } from '../../types';

interface SearchFormProps {
  control: Control<SearchFormValues>;
  watch: SearchFormValues;
  hideInstitution?: boolean;
  hideCareer?: boolean;
  hideSubject?: boolean;
}

export default function SearchForm({
  control,
  watch,
  hideInstitution = false,
  hideCareer = false,
  hideSubject = false,
}: SearchFormProps) {
  const { t } = useTranslation('searchForm');

  const { institutionId, careerId, subjectId, word, category, sortBy, asc } =
    watch;

  const navigate = useNavigate();

  // Handler for category changes
  const onCategoryChange = (category: string) => {
    const newParams = new URLSearchParams(window.location.search);
    newParams.set('category', category);
    if (category === 'directory' && sortBy === 'score') {
      newParams.set('sortBy', 'modified');
    }
    newParams.set('page', '1');
    navigate(`?${newParams.toString()}`);
  };

  // Handler to toggle ascending/descending
  const onToggleAsc = () => {
    const newParams = new URLSearchParams(window.location.search);
    newParams.set('asc', asc === 'true' ? 'false' : 'true');
    newParams.set('page', '1');
    navigate(`?${newParams.toString()}`);
  };

  // Handler to clear the word input
  const onClearWord = () => {
    const newParams = new URLSearchParams(window.location.search);
    newParams.delete('word');
    newParams.set('page', '1');
    navigate(`?${newParams.toString()}`);
  };

  // Handler for institution changes
  const onInstitutionChange = (institutionId: string) => {
    const newParams = new URLSearchParams(window.location.search);
    if (!institutionId) {
      newParams.delete('institutionId');
    } else {
      newParams.set('institutionId', institutionId);
    }
    newParams.delete('careerId');
    newParams.delete('subjectId');
    newParams.set('page', '1');
    navigate(`?${newParams.toString()}`);
  };

  // Handler for career changes
  const onCareerChange = (careerId: string) => {
    const newParams = new URLSearchParams(window.location.search);
    if (!careerId) {
      newParams.delete('careerId');
    } else {
      newParams.set('careerId', careerId);
    }
    newParams.delete('subjectId');
    newParams.set('page', '1');
    navigate(`?${newParams.toString()}`);
  };

  // Handler for subject changes
  const onSubjectChange = (subjectId: string) => {
    const newParams = new URLSearchParams(window.location.search);
    if (!subjectId) {
      newParams.delete('subjectId');
    } else {
      newParams.set('subjectId', subjectId);
    }
    newParams.set('page', '1');
    navigate(`?${newParams.toString()}`);
  };

  // Ref to hold the debounce timeout
  const debounceTimeout = useRef<NodeJS.Timeout | null>(null);

  // Debounced handler for word changes
  const onWordChange = useCallback(
    (value: string) => {
      if (debounceTimeout.current) {
        clearTimeout(debounceTimeout.current);
      }

      debounceTimeout.current = setTimeout(() => {
        const newParams = new URLSearchParams(window.location.search);
        newParams.set('word', value);
        newParams.set('page', '1');
        navigate(`?${newParams.toString()}`);
      }, 500); // 500ms debounce delay
    },
    [navigate],
  );

  // Handler for sortBy changes
  const onSortByChange = (e: SelectChangeEvent<string>) => {
    const newParams = new URLSearchParams(window.location.search);
    newParams.set('sortBy', e.target.value as string);
    newParams.set('page', '1');
    navigate(`?${newParams.toString()}`);
  };

  // Fetch data based on form values
  const { data: institutions, isFetching: isFetchingInstitutions } =
    useGetInstitutionsQuery();

  const { data: careers, isFetching: isFetchingCareers } = useGetCareersQuery(
    { institutionId },
    { skip: !institutionId || hideInstitution },
  );

  const { data: subjects, isFetching: isFetchingSubjects } =
    useGetSubjectsByCareerQuery(
      { careerId },
      { skip: !careerId || hideCareer },
    );

  // Cleanup the debounce timeout on unmount
  useEffect(() => {
    return () => {
      if (debounceTimeout.current) {
        clearTimeout(debounceTimeout.current);
      }
    };
  }, []);

  return (
    <Box
      component="form"
      sx={{
        display: 'flex',
        flexDirection: 'column',
        gap: 2,
        mb: 3,
      }}
      noValidate
      autoComplete="off"
    >
      {/* ROW 1: Institution → Career → Subject → Word */}
      <Box sx={{ display: 'flex', flexWrap: 'wrap', gap: 2 }}>
        {/* Select Institution */}
        {!hideInstitution && (
          <Autocomplete
            options={institutions || []}
            getOptionLabel={(option: Institution) => option.name || ''}
            value={
              institutions
                ? institutions.find(
                    (inst: Institution) => inst.id === institutionId,
                  ) || null
                : null
            }
            onChange={(_, newValue) => {
              onInstitutionChange(newValue ? String(newValue.id) : '');
            }}
            renderInput={(params) => (
              <TextField
                {...params}
                label={t('institution')}
                variant="outlined"
                disabled={isFetchingInstitutions}
              />
            )}
            sx={{ minWidth: 180 }}
            isOptionEqualToValue={(option, value) => option.id === value.id}
          />
        )}

        {/* Select Career */}
        {!hideCareer && (
          <Autocomplete
            options={careers || []}
            getOptionLabel={(option: Career) => option.name || ''}
            disabled={
              !institutionId || isFetchingCareers || careers?.length === 0
            }
            value={
              careers
                ? careers.find((car: Career) => car.id === careerId) || null
                : null
            }
            onChange={(_, newValue) => {
              onCareerChange(newValue ? String(newValue.id) : '');
            }}
            renderInput={(params) => (
              <TextField {...params} label={t('career')} variant="outlined" />
            )}
            sx={{ minWidth: 180 }}
            isOptionEqualToValue={(option, value) => option.id === value.id}
          />
        )}

        {/* Select Subject */}
        {!hideSubject && (
          <Autocomplete
            options={subjects || []}
            getOptionLabel={(option: Subject) => option.name || ''}
            disabled={!careerId || isFetchingSubjects || subjects?.length === 0}
            value={
              subjects
                ? subjects.find((sub: Subject) => sub.id === subjectId) || null
                : null
            }
            onChange={(_, newValue) => {
              onSubjectChange(newValue ? String(newValue.id) : '');
            }}
            renderInput={(params) => (
              <TextField {...params} label={t('subject')} />
            )}
            sx={{ minWidth: 180 }}
            isOptionEqualToValue={(option, value) => option.id === value.id}
          />
        )}

        {/* Word Input */}
        <Controller
          name="word"
          control={control}
          render={({ field }) => (
            <TextField
              {...field}
              label={t('word')}
              variant="outlined"
              sx={{ flex: 1, minWidth: 220 }}
              onChange={(e) => {
                field.onChange(e);
                onWordChange(e.target.value);
              }}
              InputProps={{
                endAdornment: word && (
                  <InputAdornment position="end">
                    <IconButton onClick={onClearWord} tabIndex={-1}>
                      <Close />
                    </IconButton>
                  </InputAdornment>
                ),
              }}
            />
          )}
        />
      </Box>

      {/* ROW 2: Category, Sort By, Ascending/Descending */}
      <Box
        sx={{ display: 'flex', flexWrap: 'wrap', gap: 2, alignItems: 'center' }}
      >
        {/* Category Buttons */}
        <Box sx={{ display: 'flex' }}>
          <Button
            variant={category === 'directory' ? 'contained' : 'outlined'}
            onClick={() => onCategoryChange('directory')}
          >
            {t('folders')}
          </Button>
          <Button
            variant={category !== 'directory' ? 'contained' : 'outlined'}
            onClick={() => onCategoryChange('note')}
            sx={{ ml: 1 }}
          >
            {t('notes')}
          </Button>
        </Box>

        {/* Select Sort By */}
        <FormControl sx={{ minWidth: 150 }}>
          <InputLabel>{t('sortBy')}</InputLabel>
          <Controller
            name="sortBy"
            control={control}
            render={({ field }) => (
              <Select {...field} label={t('sortBy')} onChange={onSortByChange}>
                <MenuItem value="name">{t('name')}</MenuItem>
                <MenuItem value="modified">{t('lastModifiedAt')}</MenuItem>
                {category !== 'directory' && (
                  <MenuItem value="score">{t('score')}</MenuItem>
                )}
              </Select>
            )}
          />
        </FormControl>

        <Button variant="outlined" onClick={onToggleAsc}>
          {asc === 'true' ? t('asc') : t('desc')}
        </Button>

        {/* Select Category */}
        {category !== 'directory' && (
          <FormControl sx={{ minWidth: 150 }}>
            <InputLabel>{t('categoryBy')}</InputLabel>
            <Controller
              name="category"
              control={control}
              render={({ field }) => (
                <Select {...field} label={t('categoryBy')}>
                  <MenuItem key="all" value="note">
                    {t('all')}
                  </MenuItem>
                  {Object.values(NoteCategory).map((noteCategory) => (
                    <MenuItem
                      key={noteCategory}
                      value={noteCategory}
                      onClick={() => onCategoryChange(noteCategory)}
                    >
                      {t(noteCategory.toLowerCase())}
                    </MenuItem>
                  ))}
                </Select>
              )}
            />
          </FormControl>
        )}
      </Box>
    </Box>
  );
}
