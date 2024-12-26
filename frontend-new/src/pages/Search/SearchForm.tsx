import { useState, useEffect } from 'react';
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
import { VisibilityOff } from '@mui/icons-material';
import { useTranslation } from 'react-i18next';
import {
  useGetInstitutionsQuery,
  useGetCareersQuery,
  useGetSubjectsByCareerQuery,
} from '../../store/slices/institutionsApiSlice';
import { SelectChangeEvent } from '@mui/material';

interface SearchFormProps {
  searchFields: {
    institutionId: string;
    careerId: string;
    subjectId: string;
    word: string;
    category: 'note' | 'directory';
    sortBy: string;
    asc: boolean;
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
  const { t } = useTranslation();

  // Local state for search-related fields initialized from props
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

  // Update local search fields when props change
  useEffect(() => {
    setLocalInstitutionId(searchFields.institutionId);
    setLocalCareerId(searchFields.careerId);
    setLocalSubjectId(searchFields.subjectId);
    setLocalWord(searchFields.word);
    // eslint-disable-next-line react-hooks/exhaustive-deps
  }, [
    searchFields.institutionId,
    searchFields.careerId,
    searchFields.subjectId,
    searchFields.word,
  ]);

  // Local state for filter-related fields controlled via props
  const { category, sortBy, asc } = searchFields;

  // Fetch data
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

  // Debounce for the word input
  useEffect(() => {
    const handler = setTimeout(() => {
      onSearch({
        institutionId: localInstitutionId,
        careerId: localCareerId,
        subjectId: localSubjectId,
        word: localWord,
        category,
        sortBy,
        asc: String(asc),
      });
    }, 350);

    return () => {
      clearTimeout(handler);
    };
    // eslint-disable-next-line react-hooks/exhaustive-deps
  }, [localWord]);

  // Handle category change side-effect
  useEffect(() => {
    if (category === 'directory' && sortBy === 'score') {
      onSearch({ sortBy: 'modified' });
    }
    // eslint-disable-next-line react-hooks/exhaustive-deps
  }, [category, sortBy]);

  // Handle Institution Change
  const handleInstitutionChange = (e: SelectChangeEvent<string>) => {
    const newInstitutionId = e.target.value;
    setLocalInstitutionId(newInstitutionId);
    setLocalCareerId('');
    setLocalSubjectId('');

    // Update search parameters
    onSearch({
      institutionId: newInstitutionId,
      careerId: '',
      subjectId: '',
      word: localWord,
      category,
      sortBy,
      asc: String(asc),
    });
  };

  // Handle Career Change
  const handleCareerChange = (e: SelectChangeEvent<string>) => {
    const newCareerId = e.target.value;
    setLocalCareerId(newCareerId);
    setLocalSubjectId('');

    // Update search parameters
    onSearch({
      institutionId: localInstitutionId,
      careerId: newCareerId,
      subjectId: '',
      word: localWord,
      category,
      sortBy,
      asc: String(asc),
    });
  };

  // Handle Subject Change
  const handleSubjectChange = (e: SelectChangeEvent<string>) => {
    const newSubjectId = e.target.value;
    setLocalSubjectId(newSubjectId);

    // Update search parameters
    onSearch({
      institutionId: localInstitutionId,
      careerId: localCareerId,
      subjectId: newSubjectId,
      word: localWord,
      category,
      sortBy,
      asc: String(asc),
    });
  };

  // Handle Category Change
  const handleCategoryChange = (newCategory: 'note' | 'directory') => {
    onSearch({ category: newCategory });
  };

  // Handle Sort By Change
  const handleSortByChange = (e: SelectChangeEvent<string>) => {
    onSearch({ sortBy: e.target.value as string });
  };

  // Toggle ascending/descending
  const toggleAsc = () => {
    onSearch({ asc: String(!asc) });
  };

  // Clear word input
  const handleClearWord = () => {
    setLocalWord('');
    // Optionally: trigger a search with the cleared word
    onSearch({
      institutionId: localInstitutionId,
      careerId: localCareerId,
      subjectId: localSubjectId,
      word: '',
      category,
      sortBy,
      asc: String(asc),
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
            <InputLabel>{t('searchForm.institution')}</InputLabel>
            <Select
              label={t('searchForm.institution')}
              value={localInstitutionId}
              onChange={handleInstitutionChange}
            >
              <MenuItem value="">
                <em>{t('searchForm.selectInstitution')}</em>
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
            <InputLabel>{t('searchForm.career')}</InputLabel>
            <Select
              label={t('searchForm.career')}
              value={localCareerId}
              onChange={handleCareerChange}
            >
              <MenuItem value="">
                <em>{t('searchForm.selectCareer')}</em>
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
            <InputLabel>{t('searchForm.subject')}</InputLabel>
            <Select
              label={t('searchForm.subject')}
              value={localSubjectId}
              onChange={handleSubjectChange}
            >
              <MenuItem value="">
                <em>{t('searchForm.selectSubject')}</em>
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
          label={t('searchForm.word')}
          variant="outlined"
          value={localWord}
          onChange={(e) => setLocalWord(e.target.value)}
          sx={{ flex: 1, minWidth: 220 }}
          InputProps={{
            endAdornment: localWord && (
              <InputAdornment position="end">
                <IconButton onClick={handleClearWord}>
                  <VisibilityOff />
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
            {t('searchForm.folders')}
          </Button>
          <Button
            variant={category === 'note' ? 'contained' : 'outlined'}
            onClick={() => handleCategoryChange('note')}
            sx={{ ml: 1 }}
          >
            {t('searchForm.notes')}
          </Button>
        </Box>

        {/* Select Sort By */}
        <FormControl sx={{ minWidth: 150 }}>
          <InputLabel>{t('searchForm.sortBy')}</InputLabel>
          <Select
            label={t('searchForm.sortBy')}
            value={sortBy}
            onChange={handleSortByChange}
          >
            <MenuItem value="name">{t('searchForm.name')}</MenuItem>
            <MenuItem value="modified">{t('searchForm.lastModifiedAt')}</MenuItem>
            <MenuItem value="score">{t('searchForm.score')}</MenuItem>
            <MenuItem value="date">{t('searchForm.createdAt')}</MenuItem>
          </Select>
        </FormControl>

        {/* Ascending/Descending Button */}
        <Button variant="outlined" onClick={toggleAsc}>
          {asc ? t('searchForm.asc') : t('searchForm.desc')}
        </Button>
      </Box>
    </Box>
  );
}
