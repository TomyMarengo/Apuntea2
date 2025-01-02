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
import { SelectChangeEvent } from '@mui/material';
import { useState, useEffect } from 'react';
import { useTranslation } from 'react-i18next';

import {
  useGetInstitutionsQuery,
  useGetCareersQuery,
  useGetSubjectsByCareerQuery,
} from '../../store/slices/institutionsApiSlice';

interface SearchFormProps {
  searchFields: {
    institutionId: string;
    careerId: string;
    subjectId: string;
    word: string;
    category: 'note' | 'directory';
    sortBy: string;
    asc: string;
    parentId?: string;
  };
  onSearch: (params: Record<string, string>) => void;
  hideInstitution?: boolean;
  hideCareer?: boolean;
  hideSubject?: boolean;
}

export default function SearchForm({
  searchFields,
  onSearch,
  hideInstitution = false,
  hideCareer = false,
  hideSubject = false,
}: SearchFormProps) {
  const { t } = useTranslation('searchForm');

  const [localInstitutionId, setLocalInstitutionId] = useState<string>(
    searchFields.institutionId,
  );
  const [localCareerId, setLocalCareerId] = useState<string>(
    searchFields.careerId,
  );
  const [localSubjectId, setLocalSubjectId] = useState<string>(
    searchFields.subjectId,
  );
  const [localWord, setLocalWord] = useState<string>(searchFields.word);

  useEffect(() => {
    setLocalInstitutionId(searchFields.institutionId);
    setLocalCareerId(searchFields.careerId);
    setLocalSubjectId(searchFields.subjectId);
    setLocalWord(searchFields.word);
  }, [
    searchFields.institutionId,
    searchFields.careerId,
    searchFields.subjectId,
    searchFields.word,
  ]);

  const { category, sortBy, asc } = searchFields;

  const { data: institutions, isFetching: isFetchingInstitutions } =
    useGetInstitutionsQuery(undefined);

  const { data: careers, isFetching: isFetchingCareers } = useGetCareersQuery(
    { institutionId: localInstitutionId },
    { skip: !localInstitutionId },
  );

  const { data: subjects, isFetching: isFetchingSubjects } =
    useGetSubjectsByCareerQuery(
      { careerId: localCareerId },
      { skip: !localCareerId },
    );

  useEffect(() => {
    const handler = setTimeout(() => {
      onSearch({
        institutionId: localInstitutionId,
        careerId: localCareerId,
        subjectId: localSubjectId,
        word: localWord,
        category,
        sortBy,
        asc,
      });
    }, 350);

    return () => {
      clearTimeout(handler);
    };
  }, [
    localWord,
    localInstitutionId,
    localCareerId,
    localSubjectId,
    category,
    sortBy,
    asc,
    onSearch,
  ]);

  useEffect(() => {
    if (category === 'directory' && sortBy === 'score') {
      onSearch({ sortBy: 'modified' });
    }
  }, [category, sortBy, onSearch]);

  const handleInstitutionChange = (e: SelectChangeEvent<string>) => {
    const newInstitutionId = e.target.value;
    setLocalInstitutionId(newInstitutionId);
    setLocalCareerId('');
    setLocalSubjectId('');

    onSearch({
      institutionId: newInstitutionId,
      careerId: '',
      subjectId: '',
      word: localWord,
      category,
      sortBy,
      asc,
    });
  };

  const handleCareerChange = (e: SelectChangeEvent<string>) => {
    const newCareerId = e.target.value;
    setLocalCareerId(newCareerId);
    setLocalSubjectId('');

    onSearch({
      institutionId: localInstitutionId,
      careerId: newCareerId,
      subjectId: '',
      word: localWord,
      category,
      sortBy,
      asc,
    });
  };

  const handleSubjectChange = (e: SelectChangeEvent<string>) => {
    const newSubjectId = e.target.value;
    setLocalSubjectId(newSubjectId);

    onSearch({
      institutionId: localInstitutionId,
      careerId: localCareerId,
      subjectId: newSubjectId,
      word: localWord,
      category,
      sortBy,
      asc,
    });
  };

  const handleCategoryChange = (newCategory: 'note' | 'directory') => {
    onSearch({ category: newCategory });
  };

  const handleSortByChange = (e: SelectChangeEvent<string>) => {
    onSearch({ sortBy: e.target.value as string });
  };

  const toggleAsc = () => {
    onSearch({ asc: asc === 'true' ? 'false' : 'true' });
  };

  const handleClearWord = () => {
    setLocalWord('');
    onSearch({
      institutionId: localInstitutionId,
      careerId: localCareerId,
      subjectId: localSubjectId,
      word: '',
      category,
      sortBy,
      asc,
    });
  };

  return (
    <Box
      sx={{
        display: 'flex',
        flexDirection: 'column',
        gap: 2,
        mb: 3,
      }}
    >
      {/* ROW 1: Institution → Career → Subject → Word */}
      <Box sx={{ display: 'flex', flexWrap: 'wrap', gap: 2 }}>
        {/* Select Institution */}
        {!hideInstitution && (
          <FormControl sx={{ minWidth: 180 }} disabled={isFetchingInstitutions}>
            <InputLabel>{t('institution')}</InputLabel>
            <Select
              label={t('institution')}
              value={localInstitutionId}
              onChange={handleInstitutionChange}
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
          </FormControl>
        )}

        {/* Select Career */}
        {!hideCareer && (
          <FormControl
            sx={{ minWidth: 180 }}
            disabled={!localInstitutionId || isFetchingCareers}
          >
            <InputLabel>{t('career')}</InputLabel>
            <Select
              label={t('career')}
              value={localCareerId}
              onChange={handleCareerChange}
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
          </FormControl>
        )}

        {/* Select Subject */}
        {!hideSubject && (
          <FormControl
            sx={{ minWidth: 180 }}
            disabled={!localCareerId || isFetchingSubjects}
          >
            <InputLabel>{t('subject')}</InputLabel>
            <Select
              label={t('subject')}
              value={localSubjectId}
              onChange={handleSubjectChange}
            >
              <MenuItem value="">
                <em>{t('noSubject')}</em>
              </MenuItem>
              {subjects?.map((sub: any) => (
                <MenuItem key={sub.id} value={String(sub.id)}>
                  {sub.name}
                </MenuItem>
              ))}
            </Select>
          </FormControl>
        )}

        {/* Word Input */}
        <TextField
          label={t('word')}
          variant="outlined"
          value={localWord}
          onChange={(e) => setLocalWord(e.target.value)}
          sx={{ flex: 1, minWidth: 220 }}
          InputProps={{
            endAdornment: localWord && (
              <InputAdornment position="end">
                <IconButton onClick={handleClearWord}>
                  <Close />
                </IconButton>
              </InputAdornment>
            ),
          }}
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
            onClick={() => handleCategoryChange('directory')}
          >
            {t('folders')}
          </Button>
          <Button
            variant={category === 'note' ? 'contained' : 'outlined'}
            onClick={() => handleCategoryChange('note')}
            sx={{ ml: 1 }}
          >
            {t('notes')}
          </Button>
        </Box>

        {/* Select Sort By */}
        <FormControl sx={{ minWidth: 150 }}>
          <InputLabel>{t('sortBy')}</InputLabel>
          <Select
            label={t('sortBy')}
            value={sortBy}
            onChange={handleSortByChange}
          >
            <MenuItem value="name">{t('name')}</MenuItem>
            <MenuItem value="modified">{t('lastModifiedAt')}</MenuItem>
            {category === 'note' && (
              <MenuItem value="score">{t('score')}</MenuItem>
            )}
            <MenuItem value="date">{t('createdAt')}</MenuItem>
          </Select>
        </FormControl>

        <Button variant="outlined" onClick={toggleAsc}>
          {asc === 'true' ? t('asc') : t('desc')}
        </Button>
      </Box>
    </Box>
  );
}
