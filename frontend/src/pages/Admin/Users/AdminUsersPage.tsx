// src/pages/Admin/Users/AdminUsersPage.tsx

import {
  Box,
  TextField,
  Select,
  MenuItem,
  InputLabel,
  FormControl,
  TableRow,
  TableCell,
  SelectChangeEvent,
  Typography,
  CircularProgress,
} from '@mui/material';
import React from 'react';
import { useState, useEffect } from 'react';
import { Helmet } from 'react-helmet-async';
import { useTranslation } from 'react-i18next';
import { useSearchParams, useNavigate } from 'react-router-dom';

import PaginationBar from '../../../components/PaginationBar';
import ResultsTable from '../../../components/ResultsTable';
import RowUser from '../../../components/Row/RowUser';
import useDebounce from '../../../hooks/useDebounce';
import { useGetUsersQuery } from '../../../store/slices/usersApiSlice';
import { UserStatus, ColumnUser } from '../../../types';

const AdminUsersPage: React.FC = () => {
  const { t } = useTranslation('adminUsersPage');
  const [searchParams] = useSearchParams();
  const navigate = useNavigate();

  // Extract query parameters from the URL
  const queryFilter = searchParams.get('query') || '';
  const statusFilter =
    (searchParams.get('status') as UserStatus | 'ALL') || 'ALL';
  const page = parseInt(searchParams.get('page') || '1', 10);
  const pageSize = 10; // Configurable if needed

  // Local state for the search input
  const [searchInput, setSearchInput] = useState(queryFilter);

  // Apply the debounce hook with a 500ms delay
  const debouncedSearchInput = useDebounce(searchInput, 500);

  // Effect to update the URL when the debounced search input changes
  useEffect(() => {
    if (debouncedSearchInput !== queryFilter) {
      const params = new URLSearchParams(searchParams);

      if (debouncedSearchInput) {
        params.set('query', debouncedSearchInput);
      } else {
        params.delete('query');
      }
      params.set('page', '1');

      navigate({ search: params.toString() }, { replace: true });
    }
  }, [debouncedSearchInput, queryFilter, searchParams, navigate]);

  // Sync the local search input state when the URL's query parameter changes externally
  useEffect(() => {
    setSearchInput(queryFilter);
  }, [queryFilter]);

  // Fetch users with the current filters and pagination
  const { data, isLoading, isError } = useGetUsersQuery({
    query: debouncedSearchInput || undefined, // Use debounced input for fetching
    status: statusFilter !== 'ALL' ? statusFilter : undefined,
    page,
    pageSize,
  });

  // Handle changes in the search input field
  const handleQueryChange = (e: React.ChangeEvent<HTMLInputElement>) => {
    setSearchInput(e.target.value);
  };

  // Handle changes in the status filter
  const handleStatusChange = (e: SelectChangeEvent<UserStatus | 'ALL'>) => {
    const newStatus = e.target.value as string;
    const params = new URLSearchParams(searchParams);
    if (newStatus !== 'ALL') {
      params.set('status', newStatus);
    } else {
      params.delete('status');
    }
    params.set('page', '1');
    navigate({ search: params.toString() }, { replace: true });
  };

  // Determine the page title based on the state
  let pageTitle = t('titlePage');
  if (isLoading) {
    pageTitle = t('loading');
  } else if (isError) {
    pageTitle = t('error');
  }

  return (
    <>
      <Helmet>
        <title>{pageTitle}</title>
      </Helmet>
      <Box sx={{ p: 3 }}>
        {/* Search and Filter Controls */}
        <Box
          sx={{
            display: 'flex',
            gap: 2,
            mb: 3,
            flexDirection: { xs: 'column', sm: 'row' },
          }}
        >
          {/* Search Input Field */}
          <TextField
            label={t('searchPlaceholder')}
            variant="outlined"
            value={searchInput}
            onChange={handleQueryChange}
            fullWidth
          />

          {/* Status Filter Dropdown */}
          <FormControl variant="outlined" sx={{ minWidth: 200 }}>
            <InputLabel id="status-filter-label">
              {t('statusFilter')}
            </InputLabel>
            <Select
              labelId="status-filter-label"
              value={statusFilter}
              onChange={handleStatusChange}
              label={t('statusFilter')}
            >
              <MenuItem value="ALL">{t('statusOptions.all')}</MenuItem>
              <MenuItem value={UserStatus.ACTIVE}>
                {t('statusOptions.active')}
              </MenuItem>
              <MenuItem value={UserStatus.BANNED}>
                {t('statusOptions.banned')}
              </MenuItem>
            </Select>
          </FormControl>
        </Box>

        {/* Results Table */}
        <ResultsTable columns={ColumnUser}>
          {/* Loading State */}
          {isLoading && (
            <TableRow>
              <TableCell colSpan={4}>
                <Box sx={{ textAlign: 'center', py: 5 }}>
                  <CircularProgress />
                </Box>
              </TableCell>
            </TableRow>
          )}

          {/* Error State */}
          {isError && (
            <TableRow>
              <TableCell colSpan={4}>
                <Box sx={{ textAlign: 'center', py: 5 }}>
                  <Typography color="error">{t('error')}</Typography>
                </Box>
              </TableCell>
            </TableRow>
          )}

          {/* Display Users */}
          {data &&
            data.users.map((user) => <RowUser key={user.id} user={user} />)}

          {/* No Results State */}
          {data && data.users.length === 0 && !isLoading && !isError && (
            <TableRow>
              <TableCell colSpan={4}>
                <Box sx={{ textAlign: 'center', py: 5 }}>
                  <Typography>{t('noResults')}</Typography>
                </Box>
              </TableCell>
            </TableRow>
          )}
        </ResultsTable>

        {/* Pagination Bar */}
        {data && data.users.length > 0 && (
          <PaginationBar
            currentPage={page}
            pageSize={pageSize}
            totalPages={data.totalPages}
            totalCount={data.totalCount}
          />
        )}
      </Box>
    </>
  );
};

export default AdminUsersPage;
