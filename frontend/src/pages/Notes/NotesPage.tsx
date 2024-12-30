// src/pages/Notes/NotesPage.tsx

import { useMemo, useEffect } from 'react';
import {
  Box,
  Button,
  Typography,
  CircularProgress,
  Stack,
} from '@mui/material';
import { useTranslation } from 'react-i18next';
import { useSelector } from 'react-redux';
import { useSearchParams } from 'react-router-dom';
import { useNavigate } from 'react-router-dom';

import {
  useGetSubjectsByCareerQuery,
  useGetSubjectCareersQuery,
} from '../../store/slices/institutionsApiSlice';

import { Subject, SubjectCareer } from '../../types';
import PaginationBar from '../../components/PaginationBar';
import { selectCurrentUser } from '../../store/slices/authSlice';
import SubjectDirectoryCard from '../../components/SubjectDirectoryCard';

const DEFAULT_PAGE_SIZE = 10;

export default function NotesPage() {
  const { t } = useTranslation();
  const navigate = useNavigate();

  // Current logged user
  const user = useSelector(selectCurrentUser);
  const userId = user?.id;
  const careerId = user?.career?.id;
  const institutionId = user?.institution?.id;

  // URL Search Params
  const [searchParams, setSearchParams] = useSearchParams();
  const yearParam = searchParams.get('year');
  const selectedYear = yearParam ? parseInt(yearParam, 10) : null;

  const pageParam = searchParams.get('page');
  const page = pageParam ? parseInt(pageParam, 10) : 1;

  const pageSizeParam = searchParams.get('pageSize');
  const pageSize = pageSizeParam
    ? parseInt(pageSizeParam, 10)
    : DEFAULT_PAGE_SIZE;

  // Early return if no user
  if (!userId || !careerId || !institutionId) {
    return (
      <Box sx={{ p: 2 }}>
        <Typography variant="h6" color="error">
          {t('notesPage.mustLogin')}
        </Typography>
      </Box>
    );
  }

  // Fetch the subjects for the user's career
  const {
    data: subjects,
    isLoading: subjectsLoading,
    isError: subjectsError,
  } = useGetSubjectsByCareerQuery(
    { careerId },
    {
      skip: !careerId,
    },
  );

  // Fetch the subject-careers to get the year of each subject
  const {
    data: subjectCareers,
    isLoading: scLoading,
    isError: scError,
  } = useGetSubjectCareersQuery(
    {
      institutionId,
      careerId,
    },
    {
      skip: !careerId || !institutionId,
    },
  );

  // If loading or error
  const isLoading = subjectsLoading || scLoading;
  const isError = subjectsError || scError;

  // Combine the subjects + subjectCareers to know which subject is in which year
  const combined = useMemo(() => {
    if (!subjects || !subjectCareers) return [];

    // Convert subjectCareers into a Map: subjectId -> year
    const subjectYearMap = new Map<string, number>();
    subjectCareers.forEach((sc: SubjectCareer) => {
      // subjectUrl is something like .../subjects/{subjectId}
      const subjectId = sc.subjectUrl.split('/').pop() || '';
      subjectYearMap.set(subjectId, sc.year);
    });

    // Build a combined array of subjects + their year
    const merged = subjects
      .map((sub: Subject) => {
        const subId = sub.id || '';
        return {
          ...sub,
          year: subjectYearMap.get(subId) || 0,
        };
      })
      // Filter out any subject that doesn't have a year
      .filter((item) => item.year > 0);

    return merged;
  }, [subjects, subjectCareers]);

  // Figure out the unique years
  const uniqueYears = useMemo(() => {
    const yearsSet = new Set<number>();
    combined.forEach((c) => yearsSet.add(c.year));
    return Array.from(yearsSet).sort((a, b) => a - b);
  }, [combined]);

  // Initialize selected year to the first year if not set
  useEffect(() => {
    if (selectedYear === null && uniqueYears.length > 0) {
      const firstYear = uniqueYears[0];
      navigate({ search: `?year=${firstYear}&page=1` }, { replace: true });
    }
  }, [uniqueYears, selectedYear, setSearchParams]);

  // Filter subjects by the selected year
  const filteredSubjects = useMemo(() => {
    if (selectedYear === null) return [];
    return combined.filter((item) => item.year === selectedYear);
  }, [combined, selectedYear]);

  // Calculate total pages for pagination
  const totalCount = filteredSubjects.length;
  const totalPages = Math.ceil(totalCount / pageSize);
  const startIndex = (page - 1) * pageSize;
  const endIndex = startIndex + pageSize;
  const subjectsPage = filteredSubjects.slice(startIndex, endIndex);

  // Handler for changing the selected year
  const handleYearChange = (yr: number) => {
    setSearchParams((prev) => {
      const newParams = new URLSearchParams(prev);
      newParams.set('year', String(yr));
      newParams.set('page', '1'); // Reset to first page when year changes
      return newParams;
    });
  };

  if (isLoading) {
    return (
      <Box sx={{ p: 2, display: 'flex', justifyContent: 'center' }}>
        <CircularProgress />
      </Box>
    );
  }

  if (isError) {
    return (
      <Box sx={{ p: 2 }}>
        <Typography color="error">{t('notesPage.errorFetching')}</Typography>
      </Box>
    );
  }

  return (
    <Box sx={{ p: 2 }}>
      {/* Title */}
      <Typography variant="h4" sx={{ mb: 3 }}>
        {t('notesPage.title')}
      </Typography>

      {/* Row with Year Buttons */}
      <Stack direction="row" spacing={2} sx={{ mb: 4 }}>
        {uniqueYears.length === 0 ? (
          <Typography variant="body1">{t('notesPage.noSubjects')}</Typography>
        ) : (
          uniqueYears.map((yr) => (
            <Button
              key={yr}
              variant={yr === selectedYear ? 'contained' : 'outlined'}
              onClick={() => handleYearChange(yr)}
            >
              {t('notesPage.yearButton', { year: yr })}
            </Button>
          ))
        )}
      </Stack>

      {/* Grid of subjects for the selected year */}
      {filteredSubjects.length === 0 ? (
        <Typography>{t('notesPage.noSubjectsForYear')}</Typography>
      ) : (
        <Box
          sx={{
            display: 'grid',
            gridTemplateColumns: 'repeat(auto-fill, minmax(160px, 200px))',
            gap: 2,
            justifyContent: 'center',
          }}
        >
          {subjectsPage.map((sub) => (
            <SubjectDirectoryCard key={sub.id} subject={sub} userId={userId} />
          ))}
        </Box>
      )}

      {/* Pagination for the subjects */}
      {filteredSubjects.length > 0 && (
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
