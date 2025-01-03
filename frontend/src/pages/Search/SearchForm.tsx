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
} from '@mui/material';
import { Controller, Control, UseFormSetValue } from 'react-hook-form';
import { useTranslation } from 'react-i18next';

import { SearchFormValues } from './searchSchema';
import {
  useGetInstitutionsQuery,
  useGetCareersQuery,
  useGetSubjectsByCareerQuery,
} from '../../store/slices/institutionsApiSlice';
import { NoteCategory } from '../../types';

interface SearchFormProps {
  control: Control<SearchFormValues>;
  watch: SearchFormValues;
  setValue: UseFormSetValue<SearchFormValues>;
  hideInstitution?: boolean;
  hideCareer?: boolean;
  hideSubject?: boolean;
}

export default function SearchForm({
  control,
  watch,
  setValue,
  hideInstitution = false,
  hideCareer = false,
  hideSubject = false,
}: SearchFormProps) {
  const { t } = useTranslation('searchForm');

  const { institutionId, careerId, word, category, sortBy, asc } = watch;

  const onCategoryChange = (category: string) => {
    setValue('category', category);
    if (category === 'directory' && sortBy === 'score') {
      setValue('sortBy', 'modified');
    }
    setValue('page', '1');
  };

  const onToggleAsc = () => {
    setValue('asc', asc === 'true' ? 'false' : 'true');
    setValue('page', '1');
  };

  const onClearWord = () => {
    setValue('word', '');
    setValue('page', '1');
  };

  const onInstitutionChange = (institutionId: string) => {
    setValue('institutionId', institutionId);
    setValue('careerId', '');
    setValue('subjectId', '');
    setValue('page', '1');
  };

  const onCareerChange = (careerId: string) => {
    setValue('careerId', careerId);
    setValue('subjectId', '');
    setValue('page', '1');
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
          <FormControl sx={{ minWidth: 180 }} disabled={isFetchingInstitutions}>
            <InputLabel>{t('institution')}</InputLabel>
            <Controller
              name="institutionId"
              control={control}
              render={({ field }) => (
                <Select
                  {...field}
                  label={t('institution')}
                  onChange={(e) => onInstitutionChange(e.target.value)}
                >
                  <MenuItem value="">
                    <em>{t('noInstitution')}</em>
                  </MenuItem>
                  {institutions?.map((inst: any) => (
                    <MenuItem key={inst.id} value={String(inst.id)}>
                      {inst.name}
                    </MenuItem>
                  ))}
                </Select>
              )}
            />
          </FormControl>
        )}

        {/* Select Career */}
        {!hideCareer && (
          <FormControl
            sx={{ minWidth: 180 }}
            disabled={!institutionId || isFetchingCareers}
          >
            <InputLabel>{t('career')}</InputLabel>
            <Controller
              name="careerId"
              control={control}
              render={({ field }) => (
                <Select
                  {...field}
                  label={t('career')}
                  onChange={(e) => onCareerChange(e.target.value)}
                >
                  <MenuItem value="">
                    <em>{t('noCareer')}</em>
                  </MenuItem>
                  {careers?.map((car: any) => (
                    <MenuItem key={car.id} value={String(car.id)}>
                      {car.name}
                    </MenuItem>
                  ))}
                </Select>
              )}
            />
          </FormControl>
        )}

        {/* Select Subject */}
        {!hideSubject && (
          <FormControl
            sx={{ minWidth: 180 }}
            disabled={!careerId || isFetchingSubjects}
          >
            <InputLabel>{t('subject')}</InputLabel>
            <Controller
              name="subjectId"
              control={control}
              render={({ field }) => (
                <Select {...field} label={t('subject')}>
                  <MenuItem value="">
                    <em>{t('noSubject')}</em>
                  </MenuItem>
                  {subjects?.map((sub: any) => (
                    <MenuItem key={sub.id} value={String(sub.id)}>
                      {sub.name}
                    </MenuItem>
                  ))}
                </Select>
              )}
            />
          </FormControl>
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
              <Select {...field} label={t('sortBy')}>
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
