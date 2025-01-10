// src/pages/Admin/Careers/AdminCareersPage.tsx

import AddBoxIcon from '@mui/icons-material/AddBox';
import LibraryAddIcon from '@mui/icons-material/LibraryAdd';
import {
  Box,
  FormControl,
  InputLabel,
  Select,
  MenuItem,
  SelectChangeEvent,
  Button,
  IconButton,
  Tooltip,
  Typography,
  CircularProgress,
  Autocomplete,
  TextField,
} from '@mui/material';
import React, { useState, useEffect, useMemo } from 'react';
import { Helmet } from 'react-helmet-async';
import { useTranslation } from 'react-i18next';

import AddSubjectDialog from './dialogs/AddSubjectDialog';
import CreateSubjectDialog from './dialogs/CreateSubjectDialog';
import ResultsTable from '../../../components/ResultsTable';
import RowSubject from '../../../components/Row/RowSubject';
import {
  useGetInstitutionsQuery,
  useGetCareersQuery,
  useGetSubjectsByCareerQuery,
  useGetSubjectCareersQuery,
} from '../../../store/slices/institutionsApiSlice';
import {
  Institution,
  Career,
  Subject,
  SubjectCareer,
  SubjectWithCareer,
  ColumnSubject,
} from '../../../types';

// Utility function for creating a numeric array range
function range(start: number, end: number): number[] {
  return Array.from({ length: end - start + 1 }, (_, i) => start + i);
}

const AdminCareersPage: React.FC = () => {
  const { t } = useTranslation('adminCareersPage');
  const [selectedInstitutionId, setSelectedInstitutionId] = useState('');
  const [selectedCareerId, setSelectedCareerId] = useState('');

  // Filter / Sort states
  const [yearFilter, setYearFilter] = useState<number | 'ALL'>('ALL');
  const [sortBy, setSortBy] = useState<'year' | 'name'>('year');
  const [sortAsc, setSortAsc] = useState(true);

  // Dialog modals
  const [openAddModal, setOpenAddModal] = useState(false);
  const [openCreateModal, setOpenCreateModal] = useState(false);

  // --- API Calls ---
  const { data: institutions } = useGetInstitutionsQuery();

  const { data: careers, isLoading: isLoadingCareers } = useGetCareersQuery(
    { institutionId: selectedInstitutionId },
    { skip: !selectedInstitutionId },
  );

  const {
    data: subjects,
    refetch: refetchSubjects,
    isLoading: loadingSubjects,
  } = useGetSubjectsByCareerQuery(
    { careerId: selectedCareerId },
    { skip: !selectedCareerId },
  );

  const {
    data: subjectCareers,
    refetch: refetchSubjectCareers,
    isLoading: loadingSubjectCareers,
  } = useGetSubjectCareersQuery(
    { institutionId: selectedInstitutionId, careerId: selectedCareerId },
    { skip: !selectedInstitutionId || !selectedCareerId },
  );

  useEffect(() => {
    if (selectedCareerId) {
      refetchSubjects();
      refetchSubjectCareers();
    }
  }, [selectedCareerId, refetchSubjects, refetchSubjectCareers]);

  // --------------- COMBINE SUBJECTS + SUBJECTCAREERS ---------------
  const combinedSubjects: SubjectWithCareer[] = useMemo(() => {
    if (
      !subjects ||
      !subjectCareers ||
      loadingSubjectCareers ||
      loadingSubjects
    )
      return [];

    // Create a map from subjectId -> SubjectWithCareer
    const scMap = new Map<string, Subject>();
    subjects.forEach((subject: Subject) => {
      if (subject.id) {
        scMap.set(subject.selfUrl, subject);
      }
    });

    // Build the merged array
    let merged: SubjectWithCareer[] = subjectCareers.map(
      (subjectcareer: SubjectCareer) => {
        const matchingSubject = scMap.get(subjectcareer.subjectUrl || '');
        return {
          subjectId: matchingSubject?.id || '',
          name: matchingSubject?.name || '',
          year: subjectcareer?.year ?? 0,
          subjectUrl: subjectcareer?.subjectUrl || '',
          subjectCareerUrl: subjectcareer?.selfUrl || '',
          careerUrl: subjectcareer?.careerUrl || '',
          rootDirectoryUrl: matchingSubject?.rootDirectoryUrl || '',
        };
      },
    );

    // Filter by year
    if (yearFilter !== 'ALL') {
      merged = merged.filter((m) => m.year === yearFilter);
    }

    // Sort
    merged.sort((a, b) => {
      if (sortBy === 'year') {
        const diff = a.year - b.year;
        return sortAsc ? diff : -diff;
      } else {
        const diff = a.name.localeCompare(b.name);
        return sortAsc ? diff : -diff;
      }
    });

    return merged;
  }, [
    subjects,
    subjectCareers,
    yearFilter,
    sortBy,
    sortAsc,
    loadingSubjects,
    loadingSubjectCareers,
  ]);

  // Compute the maximum year from subjectCareers
  const maxYearInCareer = useMemo(() => {
    if (!subjectCareers?.length) return 0;
    return subjectCareers.reduce((acc, sc) => Math.max(acc, sc.year), 0);
  }, [subjectCareers]);

  // Handlers for filters and sorting
  const handleYearFilterChange = (event: SelectChangeEvent) => {
    const val = event.target.value;
    setYearFilter(val === 'ALL' ? 'ALL' : Number(val));
  };

  const handleSortChange = (event: SelectChangeEvent) => {
    setSortBy(event.target.value as 'year' | 'name');
  };

  const handleToggleSortOrder = () => {
    setSortAsc((prev) => !prev);
  };

  // Handlers for dialogs
  const handleOpenAddModal = () => {
    setOpenAddModal(true);
  };

  const handleOpenCreateModal = () => {
    setOpenCreateModal(true);
  };

  // Determine the page title based on the state
  let pageTitle = t('titlePage');
  if (loadingSubjects || loadingSubjectCareers) {
    pageTitle = t('loadingSubjects');
  }

  return (
    <>
      <Helmet>
        <title>{pageTitle}</title>
      </Helmet>
      <Box sx={{ px: 5, py: 3 }}>
        <Box sx={{ display: 'flex', gap: 2 }}>
          <Autocomplete
            options={institutions || []}
            getOptionLabel={(option: Institution) => option.name || ''}
            value={
              institutions
                ? institutions.find(
                    (inst: Institution) => inst.id === selectedInstitutionId,
                  ) || null
                : null
            }
            onChange={(_, newValue) => {
              setSelectedInstitutionId(newValue?.id ?? '');
              setSelectedCareerId('');
            }}
            renderInput={(params) => (
              <TextField
                {...params}
                label={t('institution')}
                variant="outlined"
                margin="normal"
              />
            )}
            isOptionEqualToValue={(option, value) => option.id === value.id}
            sx={{ mr: 2, mb: 2, maxWidth: 200, width: '100%' }}
          />

          {/* Career Autocomplete */}
          <Autocomplete
            options={careers || []}
            getOptionLabel={(option: Career) => option.name || ''}
            value={
              careers
                ? careers.find((car: Career) => car.id === selectedCareerId) ||
                  null
                : null
            }
            onChange={(_, newValue) => {
              setSelectedCareerId(newValue?.id ?? '');
            }}
            renderInput={(params) => (
              <TextField
                {...params}
                label={t('career')}
                variant="outlined"
                margin="normal"
                disabled={!selectedInstitutionId || isLoadingCareers}
              />
            )}
            isOptionEqualToValue={(option, value) => option.id === value.id}
            sx={{ mr: 2, mb: 2, maxWidth: 200, width: '100%' }}
          />
        </Box>

        {selectedCareerId && (
          <Box
            sx={{
              display: 'flex',
              alignItems: 'center',
              justifyContent: 'space-between',
              mb: 3,
              mt: 2,
            }}
          >
            {/* Left side: Filter + Sort */}
            <Box sx={{ display: 'flex', gap: 2 }}>
              {/* Filter by Year */}
              <FormControl sx={{ minWidth: 140 }}>
                <InputLabel>{t('filterYear')}</InputLabel>
                <Select
                  label={t('filterYear')}
                  value={yearFilter === 'ALL' ? 'ALL' : String(yearFilter)}
                  onChange={handleYearFilterChange}
                >
                  <MenuItem value="ALL">{t('yearOptions.all')}</MenuItem>
                  {maxYearInCareer > 0 &&
                    range(1, maxYearInCareer).map((yr) => (
                      <MenuItem value={yr} key={yr}>
                        {yr}
                      </MenuItem>
                    ))}
                </Select>
              </FormControl>

              <Box sx={{ display: 'flex' }}>
                {/* Sort by year or name */}
                <FormControl sx={{ minWidth: 140 }}>
                  <InputLabel>{t('sortBy')}</InputLabel>
                  <Select
                    label={t('sortBy')}
                    value={sortBy}
                    onChange={handleSortChange}
                    sx={{
                      borderRadius: '4px 0 0 4px',
                    }}
                  >
                    <MenuItem value="year">{t('sortOptions.year')}</MenuItem>
                    <MenuItem value="name">{t('sortOptions.name')}</MenuItem>
                  </Select>
                </FormControl>

                {/* Asc/Desc Button */}
                <Button
                  variant="outlined"
                  onClick={handleToggleSortOrder}
                  sx={{
                    borderRadius: '0 4px 4px 0',
                    minWidth: '60px',
                    padding: '0 16px',
                    display: 'flex',
                    alignItems: 'center',
                  }}
                >
                  {sortAsc ? t('asc') : t('desc')}
                </Button>
              </Box>
            </Box>

            {/* Right side: Action Icons */}
            <Box sx={{ display: 'flex', gap: 2 }}>
              <Tooltip title={t('addSubject')}>
                <IconButton color="primary" onClick={handleOpenAddModal}>
                  <LibraryAddIcon />
                </IconButton>
              </Tooltip>

              <Tooltip title={t('createSubject')}>
                <IconButton color="success" onClick={handleOpenCreateModal}>
                  <AddBoxIcon />
                </IconButton>
              </Tooltip>
            </Box>
          </Box>
        )}

        {/* Loading indicator for the main table */}
        {(loadingSubjects || loadingSubjectCareers) && selectedCareerId && (
          <Box sx={{ display: 'flex', alignItems: 'center', gap: 1, mb: 2 }}>
            <CircularProgress size={24} />
            <Typography variant="body2">{t('loadingSubjects')}</Typography>
          </Box>
        )}

        {/* Results Table */}
        {selectedCareerId &&
          combinedSubjects.length > 0 &&
          !loadingSubjects &&
          !loadingSubjectCareers && (
            <ResultsTable columns={ColumnSubject}>
              {combinedSubjects.map(
                (item) =>
                  item.subjectId && (
                    <RowSubject
                      key={item.subjectId}
                      data={item}
                      institutionId={selectedInstitutionId}
                      careerId={selectedCareerId}
                    />
                  ),
              )}
            </ResultsTable>
          )}

        {/* If we have a valid career but no subjects */}
        {selectedCareerId &&
          combinedSubjects.length === 0 &&
          !loadingSubjects &&
          !loadingSubjectCareers && (
            <Typography variant="body1" sx={{ mt: 2 }}>
              {t('noSubjects')}
            </Typography>
          )}

        {/* Add Subject Dialog */}
        <AddSubjectDialog
          open={openAddModal}
          onClose={() => setOpenAddModal(false)}
          selectedInstitutionId={selectedInstitutionId}
          selectedCareerId={selectedCareerId}
          refetchSubjects={refetchSubjects}
          refetchSubjectCareers={refetchSubjectCareers}
        />

        {/* Create Subject Dialog */}
        <CreateSubjectDialog
          open={openCreateModal}
          onClose={() => setOpenCreateModal(false)}
          selectedInstitutionId={selectedInstitutionId}
          selectedCareerId={selectedCareerId}
          refetchSubjects={refetchSubjects}
          refetchSubjectCareers={refetchSubjectCareers}
        />
      </Box>
    </>
  );
};

export default AdminCareersPage;
