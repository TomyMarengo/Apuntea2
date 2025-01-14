// src/components/PaginationBar.tsx

import { Box, Pagination, Typography } from '@mui/material';
import React from 'react';
import { UseFormSetValue } from 'react-hook-form';
import { useTranslation } from 'react-i18next';
import { useSearchParams } from 'react-router-dom';

import { SearchFormValues } from '../pages/Search/searchSchema';

interface PaginationBarProps {
  currentPage: number;
  pageSize: number;
  totalPages: number;
  totalCount: number;
  setValue?: UseFormSetValue<SearchFormValues>;
}

export default function PaginationBar({
  currentPage,
  pageSize,
  totalPages,
  totalCount,
  setValue,
}: PaginationBarProps) {
  const { t } = useTranslation('paginationBar');
  const [searchParams, setSearchParams] = useSearchParams();

  function handlePageChange(_: React.ChangeEvent<unknown>, page: number) {
    if (!setValue) {
      const newParams = new URLSearchParams(searchParams);
      newParams.set('page', String(page));
      setSearchParams(newParams);
    } else {
      setValue('page', page.toString());
    }
  }

  const from = Math.min(
    totalCount - pageSize,
    (currentPage - 1) * pageSize + 1,
  );
  const to = Math.min(currentPage * pageSize, totalCount);

  return (
    <Box
      sx={{
        mt: 3,
        display: 'flex',
        flexDirection: 'column',
        alignItems: 'center',
        gap: 1,
      }}
    >
      <Pagination
        count={totalPages}
        page={currentPage}
        onChange={handlePageChange}
      />
      <Typography variant="body2">
        {t('totalCount', { from, to, totalCount })}
      </Typography>
    </Box>
  );
}
