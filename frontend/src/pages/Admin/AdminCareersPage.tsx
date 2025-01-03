// src/pages/AdminCareersPage.tsx

import AddBoxIcon from '@mui/icons-material/AddBox';
import LibraryAddIcon from '@mui/icons-material/LibraryAdd';
import {
  Box,
  FormControl,
  InputLabel,
  Select,
  MenuItem,
  SelectChangeEvent,
  IconButton,
  Tooltip,
  Typography,
  Dialog,
  DialogTitle,
  DialogContent,
  DialogActions,
  Button,
  TextField,
  CircularProgress,
  Autocomplete,
} from '@mui/material';
import React, { useState, useEffect, useMemo } from 'react';
import { Helmet } from 'react-helmet-async';
import { useTranslation } from 'react-i18next';
import { toast } from 'react-toastify';

import ResultsTable from '../../components/ResultsTable';
import RowSubject from '../../components/Row/RowSubject';
import {
  useGetInstitutionsQuery,
  useGetCareersQuery,
  useGetSubjectsByCareerQuery,
  useGetSubjectCareersQuery,
  useGetSubjectsNotInCareerQuery,
  useCreateSubjectMutation,
  useLinkSubjectCareerMutation,
} from '../../store/slices/institutionsApiSlice';
import {
  Institution,
  Career,
  Subject,
  SubjectCareer,
  SubjectWithCareer,
  ColumnSubject,
} from '../../types';

import 'react-toastify/dist/ReactToastify.css';

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
  const [sortBy, setSortBy] = useState<'year' | 'name'>('year'); // default sort by year
  const [sortAsc, setSortAsc] = useState(true); // default ascending

  // Modals
  const [openAddModal, setOpenAddModal] = useState(false);
  const [openCreateModal, setOpenCreateModal] = useState(false);

  // States for "Add Subject" modal
  const [selectedSubjectId, setSelectedSubjectId] = useState('');
  const [addSubjectYear, setAddSubjectYear] = useState(1);

  // States for "Create Subject" modal
  const [newSubjectName, setNewSubjectName] = useState('');
  const [newSubjectYear, setNewSubjectYear] = useState(1);

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

  const {
    data: subjectsNotInCareer,
    refetch: refetchNotInCareer,
    isLoading: loadingNotInCareer,
  } = useGetSubjectsNotInCareerQuery(
    {
      careerId: selectedCareerId,
      url: careers?.find((c) => c.id === selectedCareerId)
        ?.subjectsNotInCareerUrl,
    },
    { skip: !selectedCareerId },
  );

  const [linkSubjectCareer, { isLoading: linkingSubject }] =
    useLinkSubjectCareerMutation();
  const [createSubject, { isLoading: creatingSubject }] =
    useCreateSubjectMutation();

  // Filter by year
  const handleYearFilterChange = (event: SelectChangeEvent) => {
    const val = event.target.value;
    setYearFilter(val === 'ALL' ? 'ALL' : Number(val));
  };

  // Sort by year or name
  const handleSortChange = (event: SelectChangeEvent) => {
    setSortBy(event.target.value as 'year' | 'name');
  };

  // Toggle ascending/descending
  const handleToggleSortOrder = () => {
    setSortAsc((prev) => !prev);
  };

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

  // Action: Add an existing subject
  const handleOpenAddModal = () => {
    setSelectedSubjectId('');
    setAddSubjectYear(1);
    refetchNotInCareer();
    setOpenAddModal(true);
  };

  const handleConfirmAddSubject = async () => {
    if (!selectedInstitutionId || !selectedCareerId || !selectedSubjectId)
      return;
    try {
      await linkSubjectCareer({
        institutionId: selectedInstitutionId,
        careerId: selectedCareerId,
        subjectId: selectedSubjectId,
        year: addSubjectYear,
      }).unwrap();

      toast.success(t('toast.addSubject.success'));
      // Refresh
      refetchSubjects();
      refetchSubjectCareers();
    } catch (error) {
      console.error('Failed to link subject:', error);
      toast.error(t('toast.addSubject.error'));
    } finally {
      setOpenAddModal(false);
    }
  };

  // Action: Create new subject
  const handleOpenCreateModal = () => {
    setNewSubjectName('');
    setNewSubjectYear(1);
    setOpenCreateModal(true);
  };

  const handleConfirmCreateSubject = async () => {
    if (!selectedInstitutionId || !selectedCareerId || !newSubjectName) return;
    try {
      await createSubject({
        institutionId: selectedInstitutionId,
        careerId: selectedCareerId,
        name: newSubjectName,
        year: newSubjectYear,
      }).unwrap();

      toast.success(t('toast.createSubject.success'));
      // Refresh
      refetchSubjects();
      refetchSubjectCareers();
    } catch (error) {
      console.error('Failed to create subject:', error);
      toast.error(t('toast.createSubject.error'));
    } finally {
      setOpenCreateModal(false);
    }
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

              {/* Sort by year or name */}
              <FormControl sx={{ minWidth: 140 }}>
                <InputLabel>{t('sortBy')}</InputLabel>
                <Select
                  label={t('sortBy')}
                  value={sortBy}
                  onChange={handleSortChange}
                >
                  <MenuItem value="year">{t('sortOptions.year')}</MenuItem>
                  <MenuItem value="name">{t('sortOptions.name')}</MenuItem>
                </Select>
              </FormControl>

              <Tooltip title={t('sortOrder')}>
                <IconButton onClick={handleToggleSortOrder}>
                  {sortAsc ? '↑' : '↓'}
                </IconButton>
              </Tooltip>
            </Box>

            {/* Right side: Action Icons */}
            <Box sx={{ display: 'flex', gap: 2 }}>
              <Tooltip title={t('addSubjectModal.title')}>
                <IconButton color="primary" onClick={handleOpenAddModal}>
                  <LibraryAddIcon />
                </IconButton>
              </Tooltip>

              <Tooltip title={t('createSubjectModal.title')}>
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

        {/* Add Subject Modal */}
        <Dialog
          open={openAddModal}
          onClose={() => setOpenAddModal(false)}
          maxWidth="xs"
          fullWidth
        >
          <DialogTitle>{t('addSubjectModal.title')}</DialogTitle>
          <DialogContent>
            {loadingNotInCareer ? (
              <Box sx={{ textAlign: 'center', py: 3 }}>
                <CircularProgress />
              </Box>
            ) : (
              <>
                <Autocomplete
                  options={subjectsNotInCareer || []}
                  getOptionLabel={(option: Subject) => option.name || ''}
                  value={
                    subjectsNotInCareer
                      ? subjectsNotInCareer.find(
                          (sub) => sub.id === selectedSubjectId,
                        ) || null
                      : null
                  }
                  onChange={(_, newValue) => {
                    setSelectedSubjectId(newValue ? newValue.id! : '');
                  }}
                  renderInput={(params) => (
                    <TextField
                      {...params}
                      label={t('addSubjectModal.selectSubject')}
                      variant="outlined"
                      margin="normal"
                      fullWidth
                    />
                  )}
                  sx={{ mt: 2, width: '100%' }}
                />

                <TextField
                  sx={{ mt: 2 }}
                  type="number"
                  fullWidth
                  label={t('addSubjectModal.yearLabel')}
                  value={addSubjectYear}
                  onChange={(e) => setAddSubjectYear(Number(e.target.value))}
                  inputProps={{ min: 1 }}
                />
              </>
            )}
          </DialogContent>
          <DialogActions>
            <Button onClick={() => setOpenAddModal(false)}>
              {t('addSubjectModal.cancel')}
            </Button>
            <Button
              onClick={handleConfirmAddSubject}
              disabled={
                !selectedSubjectId || linkingSubject || loadingNotInCareer
              }
            >
              {linkingSubject ? (
                <CircularProgress size={20} />
              ) : (
                t('addSubjectModal.confirm')
              )}
            </Button>
          </DialogActions>
        </Dialog>

        {/* Create Subject Modal */}
        <Dialog
          open={openCreateModal}
          onClose={() => setOpenCreateModal(false)}
          maxWidth="xs"
          fullWidth
        >
          <DialogTitle>{t('createSubjectModal.title')}</DialogTitle>
          <DialogContent>
            <TextField
              sx={{ mt: 2 }}
              fullWidth
              label={t('createSubjectModal.nameLabel')}
              value={newSubjectName}
              onChange={(e) => setNewSubjectName(e.target.value)}
            />
            <TextField
              sx={{ mt: 2 }}
              type="number"
              fullWidth
              label={t('createSubjectModal.yearLabel')}
              value={newSubjectYear}
              onChange={(e) => setNewSubjectYear(Number(e.target.value))}
            />
          </DialogContent>
          <DialogActions>
            <Button onClick={() => setOpenCreateModal(false)}>
              {t('createSubjectModal.cancel')}
            </Button>
            <Button
              onClick={handleConfirmCreateSubject}
              disabled={!newSubjectName || creatingSubject}
            >
              {creatingSubject ? (
                <CircularProgress size={20} />
              ) : (
                t('createSubjectModal.confirm')
              )}
            </Button>
          </DialogActions>
        </Dialog>
      </Box>
    </>
  );
};

export default AdminCareersPage;
