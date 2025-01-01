// src/pages/Users/UserNotes.tsx

import React, { useMemo, useEffect } from 'react';
import {
  Box,
  Button,
  Typography,
  CircularProgress,
  Stack,
} from '@mui/material';
import { useTranslation } from 'react-i18next';
import { useSearchParams, useNavigate } from 'react-router-dom';
import {
  useGetSubjectsByCareerQuery,
  useGetSubjectCareersQuery,
} from '../../store/slices/institutionsApiSlice';
import { Subject, SubjectCareer, User } from '../../types';
import PaginationBar from '../../components/PaginationBar';
import SubjectDirectoryCard from '../../components/SubjectDirectoryCard';

interface UserNotesProps {
  user: User;
}

const DEFAULT_PAGE_SIZE = 10;

const UserNotes: React.FC<UserNotesProps> = ({ user }) => {
  const { t } = useTranslation('userNotes');
  const navigate = useNavigate();

  const [searchParams, setSearchParams] = useSearchParams();
  const yearParam = searchParams.get('year');
  const selectedYear = yearParam ? parseInt(yearParam, 10) : null;

  const pageParam = searchParams.get('page');
  const page = pageParam ? parseInt(pageParam, 10) : 1;

  const pageSizeParam = searchParams.get('pageSize');
  const pageSize = pageSizeParam
    ? parseInt(pageSizeParam, 10)
    : DEFAULT_PAGE_SIZE;

  const careerId = user.career?.id;
  const institutionId = user.institution?.id;

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

  const isLoading = subjectsLoading || scLoading;
  const isError = subjectsError || scError;

  const combined = useMemo(() => {
    if (!subjects || !subjectCareers) return [];

    const subjectYearMap = new Map<string, number>();
    subjectCareers.forEach((sc: SubjectCareer) => {
      const subjectId = sc.subjectUrl.split('/').pop() || '';
      subjectYearMap.set(subjectId, sc.year);
    });

    const merged = subjects
      .map((sub: Subject) => {
        const subId = sub.id || '';
        return {
          ...sub,
          year: subjectYearMap.get(subId) || 0,
        };
      })
      .filter((item) => item.year > 0);

    return merged;
  }, [subjects, subjectCareers]);

  const uniqueYears = useMemo(() => {
    const yearsSet = new Set<number>();
    combined.forEach((c) => yearsSet.add(c.year));
    return Array.from(yearsSet).sort((a, b) => a - b);
  }, [combined]);

  useEffect(() => {
    if (selectedYear === null && uniqueYears.length > 0) {
      const firstYear = uniqueYears[0];
      navigate({ search: `?year=${firstYear}&page=1` }, { replace: true });
    }
  }, [uniqueYears, selectedYear, navigate]);

  const filteredSubjects = useMemo(() => {
    if (selectedYear === null) return [];
    return combined.filter((item) => item.year === selectedYear);
  }, [combined, selectedYear]);

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
                  key={sub.id}
                  subject={sub}
                  userId={user.id}
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
