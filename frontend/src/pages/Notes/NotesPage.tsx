// src/pages/Notes/NotesPage.tsx

import {
  Box,
  Button,
  Typography,
  CircularProgress,
  Stack,
} from '@mui/material';
import { useMemo, useEffect } from 'react';
import { Helmet } from 'react-helmet-async';
import { useTranslation } from 'react-i18next';
import { useSelector } from 'react-redux';
import { useSearchParams, useNavigate } from 'react-router-dom';

import PaginationBar from '../../components/PaginationBar';
import SubjectDirectoryCard from '../../components/SubjectDirectoryCard';
import { selectCurrentUser } from '../../store/slices/authSlice';
import {
  useGetSubjectsByCareerQuery,
  useGetSubjectCareersQuery,
} from '../../store/slices/institutionsApiSlice';
import { Subject, SubjectCareer } from '../../types';

const DEFAULT_PAGE_SIZE = 10;

export default function NotesPage() {
  const { t } = useTranslation('notesPage');
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
      const subjectId = sc.subjectUrl.split('/').pop() || '';
      subjectYearMap.set(subjectId, sc.year);
    });

    return subjects
      .map((sub: Subject) => ({
        ...sub,
        year: subjectYearMap.get(sub.id || '') || 0,
      }))
      .filter((item) => item.year > 0);
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
  }, [uniqueYears, selectedYear, setSearchParams, navigate]);

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

  const handleYearChange = (yr: number) => {
    setSearchParams((prev) => {
      const newParams = new URLSearchParams(prev);
      newParams.set('year', String(yr));
      newParams.set('page', '1');
      return newParams;
    });
  };

  let pageTitle = t('titlePage');
  if (isLoading) {
    pageTitle = t('loading');
  } else if (isError) {
    pageTitle = t('errorFetching');
  }

  return (
    <>
      <Helmet>
        <title>{pageTitle}</title>
      </Helmet>
      <Box sx={{ p: 2 }}>
        <Typography variant="h4" sx={{ mb: 3 }}>
          {t('titlePage')}
        </Typography>
        <Stack direction="row" spacing={2} sx={{ mb: 4 }}>
          {uniqueYears.length > 0 &&
            uniqueYears.map((yr) => (
              <Button
                key={yr}
                variant={yr === selectedYear ? 'contained' : 'outlined'}
                onClick={() => handleYearChange(yr)}
              >
                {t('yearButton', { year: yr })}
              </Button>
            ))}
        </Stack>
        {isLoading ? (
          <Box sx={{ display: 'flex', justifyContent: 'center' }}>
            <CircularProgress />
          </Box>
        ) : isError ? (
          <Typography color="error">{t('errorFetching')}</Typography>
        ) : filteredSubjects.length === 0 ? (
          <Typography>{t('noSubjectsForYear')}</Typography>
        ) : (
          <>
            <Box
              sx={{
                display: 'grid',
                gridTemplateColumns: 'repeat(auto-fill, minmax(160px, 200px))',
                gap: 2,
                justifyContent: 'center',
              }}
            >
              {subjectsPage.map((sub) => (
                <SubjectDirectoryCard
                  key={sub.id}
                  subject={sub}
                  userId={userId}
                />
              ))}
            </Box>
            <PaginationBar
              currentPage={page}
              pageSize={pageSize}
              totalPages={totalPages}
              totalCount={totalCount}
            />
          </>
        )}
      </Box>
    </>
  );
}
