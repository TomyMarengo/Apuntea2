// src/components/PaginationBar.tsx

import { Box, Pagination, Typography } from '@mui/material';
import { useTranslation } from 'react-i18next';
import { useSearchParams } from 'react-router-dom';

interface PaginationBarProps {
  currentPage: number;
  pageSize: number;
  totalPages: number;
  totalCount: number;
}

export default function PaginationBar({
  currentPage,
  pageSize,
  totalPages,
  totalCount,
}: PaginationBarProps) {
  const { t } = useTranslation();
  const [searchParams, setSearchParams] = useSearchParams();

  function handlePageChange(_: React.ChangeEvent<unknown>, page: number) {
    const newParams = new URLSearchParams(searchParams);
    newParams.set('page', String(page));
    setSearchParams(newParams);
  }

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
        onChange={handlePageChange}
      />
      <Typography variant="body2">
        {t('paginationBar.totalCount', { from, to, totalCount })}
      </Typography>
    </Box>
  );
}
