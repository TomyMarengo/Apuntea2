// src/pages/AdminCareersPage.tsx

import React, { useState, useMemo } from 'react';
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
} from '@mui/material';
import { useTranslation } from 'react-i18next';
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
} from '../../types';
import ResultsTable, { Column } from '../../components/ResultsTable';
import RowSubject from '../../components/Row/RowSubject';

import AddBoxIcon from '@mui/icons-material/AddBox';
import LibraryAddIcon from '@mui/icons-material/LibraryAdd';
import { toast } from 'react-toastify';
import 'react-toastify/dist/ReactToastify.css';

// Utility function for creating a numeric array range
function range(start: number, end: number): number[] {
  return Array.from({ length: end - start + 1 }, (_, i) => start + i);
}

const AdminCareersPage: React.FC = () => {
  const { t } = useTranslation();
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
  const { data: careers } = useGetCareersQuery(
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

  // Institution / Career selection handlers
  const handleInstitutionChange = (event: SelectChangeEvent) => {
    setSelectedInstitutionId(event.target.value);
    setSelectedCareerId('');
  };

  const handleCareerChange = (event: SelectChangeEvent) => {
    setSelectedCareerId(event.target.value);
  };

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

  // --------------- COMBINE SUBJECTS + SUBJECTCAREERS ---------------
  const combinedSubjects: SubjectWithCareer[] = useMemo(() => {
    if (!subjects || !subjectCareers) return [];

    // Create a map from subjectId -> SubjectWithCareer
    const scMap = new Map<string, Subject>();
    subjects.forEach((subject: Subject) => {
      if (subject.id) {
        scMap.set(subject.id, subject);
      }
    });

    // Build the merged array
    let merged: SubjectWithCareer[] = subjectCareers.map(
      (subjectcareer: SubjectCareer) => {
        const matchingSubject = scMap.get(
          subjectcareer.subjectUrl.split('/').pop() || '',
        );
        return {
          subjectId: matchingSubject?.id || '',
          name: matchingSubject?.name || '',
          year: subjectcareer?.year ?? 0,
          subjectUrl: subjectcareer?.subjectUrl || '',
          subjectCareerUrl: subjectcareer?.selfUrl || '',
          careerUrl: subjectcareer?.careerUrl || '',
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
  }, [subjects, subjectCareers, yearFilter, sortBy, sortAsc]);

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

      toast.success(t('adminCareersPage.toast.addSubject.success'));
      // Refresh
      refetchSubjects();
      refetchSubjectCareers();
    } catch (error) {
      console.error('Failed to link subject:', error);
      toast.error(t('adminCareersPage.toast.addSubject.error'));
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

      toast.success(t('adminCareersPage.toast.createSubject.success'));
      // Refresh
      refetchSubjects();
      refetchSubjectCareers();
    } catch (error) {
      console.error('Failed to create subject:', error);
      toast.error(t('adminCareersPage.toast.createSubject.error'));
    } finally {
      setOpenCreateModal(false);
    }
  };

  // Columns for the table
  const columns: Column[] = [
    { id: 'name', label: t('adminCareersPage.columns.name') },
    { id: 'year', label: t('adminCareersPage.columns.year') },
    {
      id: 'actions',
      label: t('adminCareersPage.columns.actions'),
      align: 'right',
    },
  ];

  // Main table loading state
  const isLoadingAny = loadingSubjects || loadingSubjectCareers;

  return (
    <Box sx={{ p: 3 }}>
      <Typography variant="h5" sx={{ mb: 2 }}>
        {t('adminCareersPage.title')}
      </Typography>

      {/* Institution Select */}
      <FormControl sx={{ mr: 2, mb: 2, minWidth: 200 }}>
        <InputLabel>{t('adminCareersPage.institution')}</InputLabel>
        <Select
          label={t('adminCareersPage.institution')}
          value={selectedInstitutionId}
          onChange={handleInstitutionChange}
        >
          <MenuItem value="">
            <em>{t('adminCareersPage.selectInstitution')}</em>
          </MenuItem>
          {institutions?.map((inst: Institution) => (
            <MenuItem key={inst.id} value={inst.id}>
              {inst.name}
            </MenuItem>
          ))}
        </Select>
      </FormControl>

      {/* Career Select */}
      <FormControl
        sx={{ mr: 2, mb: 2, minWidth: 200 }}
        disabled={!selectedInstitutionId}
      >
        <InputLabel>{t('adminCareersPage.career')}</InputLabel>
        <Select
          label={t('adminCareersPage.career')}
          value={selectedCareerId}
          onChange={handleCareerChange}
        >
          <MenuItem value="">
            <em>{t('adminCareersPage.selectCareer')}</em>
          </MenuItem>
          {careers?.map((car: Career) => (
            <MenuItem key={car.id} value={car.id}>
              {car.name}
            </MenuItem>
          ))}
        </Select>
      </FormControl>

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
              <InputLabel>{t('adminCareersPage.filterYear')}</InputLabel>
              <Select
                label={t('adminCareersPage.filterYear')}
                value={yearFilter === 'ALL' ? 'ALL' : String(yearFilter)}
                onChange={handleYearFilterChange}
              >
                <MenuItem value="ALL">
                  {t('adminCareersPage.yearOptions.all')}
                </MenuItem>
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
              <InputLabel>{t('adminCareersPage.sortBy')}</InputLabel>
              <Select
                label={t('adminCareersPage.sortBy')}
                value={sortBy}
                onChange={handleSortChange}
              >
                <MenuItem value="year">
                  {t('adminCareersPage.sortOptions.year')}
                </MenuItem>
                <MenuItem value="name">
                  {t('adminCareersPage.sortOptions.name')}
                </MenuItem>
              </Select>
            </FormControl>

            <Tooltip title={t('adminCareersPage.sortOrder')}>
              <IconButton onClick={handleToggleSortOrder} sx={{ mt: 1 }}>
                {sortAsc ? '↑' : '↓'}
              </IconButton>
            </Tooltip>
          </Box>

          {/* Right side: Action Icons */}
          <Box sx={{ display: 'flex', gap: 2 }}>
            <Tooltip title={t('adminCareersPage.addSubjectModal.title')}>
              <IconButton color="primary" onClick={handleOpenAddModal}>
                <LibraryAddIcon />
              </IconButton>
            </Tooltip>

            <Tooltip title={t('adminCareersPage.createSubjectModal.title')}>
              <IconButton color="success" onClick={handleOpenCreateModal}>
                <AddBoxIcon />
              </IconButton>
            </Tooltip>
          </Box>
        </Box>
      )}

      {/* Loading indicator for the main table */}
      {isLoadingAny && selectedCareerId && (
        <Box sx={{ display: 'flex', alignItems: 'center', gap: 1, mb: 2 }}>
          <CircularProgress size={24} />
          <Typography variant="body2">
            {t('adminCareersPage.loadingSubjects')}
          </Typography>
        </Box>
      )}

      {/* Results Table */}
      {selectedCareerId && combinedSubjects.length > 0 && !isLoadingAny && (
        <ResultsTable columns={columns}>
          {combinedSubjects.map((item) => (
            <RowSubject
              key={item.subjectId}
              data={item}
              institutionId={selectedInstitutionId}
              careerId={selectedCareerId}
            />
          ))}
        </ResultsTable>
      )}

      {/* If we have a valid career but no subjects */}
      {selectedCareerId && combinedSubjects.length === 0 && !isLoadingAny && (
        <Typography variant="body1" sx={{ mt: 2 }}>
          {t('adminCareersPage.noSubjects')}
        </Typography>
      )}

      {/* Add Subject Modal */}
      <Dialog
        open={openAddModal}
        onClose={() => setOpenAddModal(false)}
        maxWidth="xs"
        fullWidth
      >
        <DialogTitle>{t('adminCareersPage.addSubjectModal.title')}</DialogTitle>
        <DialogContent>
          {loadingNotInCareer ? (
            <Box sx={{ textAlign: 'center', py: 3 }}>
              <CircularProgress />
            </Box>
          ) : (
            <>
              <FormControl sx={{ mt: 2, width: '100%' }}>
                <InputLabel>
                  {t('adminCareersPage.addSubjectModal.selectSubject')}
                </InputLabel>
                <Select
                  label={t('adminCareersPage.addSubjectModal.selectSubject')}
                  value={selectedSubjectId}
                  onChange={(e) => setSelectedSubjectId(e.target.value)}
                >
                  <MenuItem value="">
                    <em>{t('adminCareersPage.addSubjectModal.none')}</em>
                  </MenuItem>
                  {subjectsNotInCareer?.map((sub) => (
                    <MenuItem key={sub.id} value={sub.id}>
                      {sub.name}
                    </MenuItem>
                  ))}
                </Select>
              </FormControl>

              <TextField
                sx={{ mt: 2 }}
                type="number"
                fullWidth
                label={t('adminCareersPage.addSubjectModal.yearLabel')}
                value={addSubjectYear}
                onChange={(e) => setAddSubjectYear(Number(e.target.value))}
              />
            </>
          )}
        </DialogContent>
        <DialogActions>
          <Button onClick={() => setOpenAddModal(false)}>
            {t('adminCareersPage.addSubjectModal.cancel')}
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
              t('adminCareersPage.addSubjectModal.confirm')
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
        <DialogTitle>
          {t('adminCareersPage.createSubjectModal.title')}
        </DialogTitle>
        <DialogContent>
          <TextField
            sx={{ mt: 2 }}
            fullWidth
            label={t('adminCareersPage.createSubjectModal.nameLabel')}
            value={newSubjectName}
            onChange={(e) => setNewSubjectName(e.target.value)}
          />
          <TextField
            sx={{ mt: 2 }}
            type="number"
            fullWidth
            label={t('adminCareersPage.createSubjectModal.yearLabel')}
            value={newSubjectYear}
            onChange={(e) => setNewSubjectYear(Number(e.target.value))}
          />
        </DialogContent>
        <DialogActions>
          <Button onClick={() => setOpenCreateModal(false)}>
            {t('adminCareersPage.createSubjectModal.cancel')}
          </Button>
          <Button
            onClick={handleConfirmCreateSubject}
            disabled={!newSubjectName || creatingSubject}
          >
            {creatingSubject ? (
              <CircularProgress size={20} />
            ) : (
              t('adminCareersPage.createSubjectModal.confirm')
            )}
          </Button>
        </DialogActions>
      </Dialog>
    </Box>
  );
};

export default AdminCareersPage;
