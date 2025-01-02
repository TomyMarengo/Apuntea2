// src/components/PaginationBar.tsx

import { Box, Pagination, Typography } from '@mui/material';
import { UseFormSetValue } from 'react-hook-form';
import { useTranslation } from 'react-i18next';

import { SearchFormValues } from '../pages/Search/searchSchema';

interface PaginationBarProps {
  currentPage: number;
  pageSize: number;
  totalPages: number;
  totalCount: number;
  setValue: UseFormSetValue<SearchFormValues>;
}

export default function PaginationBar({
  currentPage,
  pageSize,
  totalPages,
  totalCount,
  setValue,
}: PaginationBarProps) {
  const { t } = useTranslation('paginationBar');

  const from = (currentPage - 1) * pageSize + 1;
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
        onChange={(_, page) => {
          setValue('page', page.toString());
        }}
      />
      <Typography variant="body2">
        {t('totalCount', { from, to, totalCount })}
      </Typography>
    </Box>
  );
}
