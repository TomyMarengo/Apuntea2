// src/pages/Users/UserNotes.tsx

import {
  Box,
  Button,
  Typography,
  CircularProgress,
  Stack,
} from '@mui/material';
import React, { useMemo, useEffect } from 'react';
import { useTranslation } from 'react-i18next';
import { useSearchParams, useNavigate } from 'react-router-dom';

import PaginationBar from '../../components/PaginationBar';
import SubjectDirectoryCard from '../../components/SubjectDirectoryCard';
import {
  useGetSubjectsByCareerQuery,
  useGetSubjectCareersQuery,
} from '../../store/slices/institutionsApiSlice';
import { Career, Subject, SubjectCareer, SubjectWithCareer } from '../../types';

interface UserNotesProps {
  userId: string;
  career: Career;
}

const DEFAULT_PAGE_SIZE = 10;

const UserNotes: React.FC<UserNotesProps> = ({ userId, career }) => {
  const { t } = useTranslation('userNotes');
  const navigate = useNavigate();

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

  const {
    data: subjects,
    isLoading: subjectsLoading,
    isError: subjectsError,
  } = useGetSubjectsByCareerQuery(
    { url: career?.subjectsUrl },
    {
      skip: !career?.subjectsUrl,
    },
  );

  const {
    data: subjectCareers,
    isLoading: scLoading,
    isError: scError,
  } = useGetSubjectCareersQuery(
    {
      url: career?.subjectCareersUrl,
    },
    {
      skip: !career?.subjectCareersUrl,
    },
  );

  // If loading or error
  const isLoading = subjectsLoading || scLoading;
  const isError = subjectsError || scError;

  // --------------- COMBINE SUBJECTS + SUBJECTCAREERS ---------------
  const combined: SubjectWithCareer[] = useMemo(() => {
    if (!subjects || !subjectCareers || subjectsLoading || scLoading) return [];

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
    return merged;
  }, [subjects, subjectCareers, subjectsLoading, scLoading]);

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

  return (
    <Box sx={{ p: 2 }}>
      {isLoading ? (
        <Box sx={{ p: 2, display: 'flex', justifyContent: 'center' }}>
          <CircularProgress />
        </Box>
      ) : isError ? (
        <Typography color="error">{t('errorFetching')}</Typography>
      ) : (
        <>
          {/* Title */}
          <Typography variant="h5" sx={{ mb: 3 }}>
            {t('notes')}
          </Typography>

          {/* Row with year buttons */}
          <Stack direction="row" spacing={2} sx={{ mb: 4 }}>
            {uniqueYears.length === 0 ? (
              <Typography variant="body1">{t('noSubjects')}</Typography>
            ) : (
              uniqueYears.map((yr) => (
                <Button
                  key={yr}
                  variant={yr === selectedYear ? 'contained' : 'outlined'}
                  onClick={() => handleYearChange(yr)}
                >
                  {t('yearButton', { year: yr })}
                </Button>
              ))
            )}
          </Stack>

          {/* Grid of subjects for the selected year */}
          {filteredSubjects.length === 0 ? (
            <Typography>{t('noSubjectsForYear')}</Typography>
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
                <SubjectDirectoryCard
                  key={sub.subjectId}
                  subject={sub}
                  userId={userId}
                />
              ))}
            </Box>
          )}

          {filteredSubjects.length > 0 && (
            <PaginationBar
              currentPage={page}
              pageSize={pageSize}
              totalPages={totalPages}
              totalCount={totalCount}
            />
          )}
        </>
      )}
    </Box>
  );
};

export default UserNotes;
