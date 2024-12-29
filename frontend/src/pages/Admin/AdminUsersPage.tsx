// src/pages/Admin/AdminUsersPage.tsx

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
} from '@mui/material';
import { useTranslation } from 'react-i18next';
import ResultsTable, { Column } from '../../components/ResultsTable';
import RowUser from '../../components/Row/RowUser';
import PaginationBar from '../../components/PaginationBar';
import { useGetUsersQuery } from '../../store/slices/usersApiSlice';
import { UserStatus } from '../../types';
import { useSearchParams, useNavigate } from 'react-router-dom';
import { useState, useEffect } from 'react';
import useDebounce from '../../hooks/useDebounce';

const AdminUsersPage: React.FC = () => {
  const { t } = useTranslation();
  const [searchParams, setSearchParams] = useSearchParams();
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
  }, [debouncedSearchInput, queryFilter, searchParams]);

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

  // Define the table columns
  const columns: Column[] = [
    { id: 'username', label: t('adminUsersPage.columns.username') },
    { id: 'email', label: t('adminUsersPage.columns.email') },
    { id: 'status', label: t('adminUsersPage.columns.status') },
    {
      id: 'actions',
      label: t('adminUsersPage.columns.actions'),
      align: 'right',
    },
  ];

  return (
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
          label={t('adminUsersPage.searchPlaceholder')}
          variant="outlined"
          value={searchInput}
          onChange={handleQueryChange}
          fullWidth
        />

        {/* Status Filter Dropdown */}
        <FormControl variant="outlined" sx={{ minWidth: 200 }}>
          <InputLabel id="status-filter-label">
            {t('adminUsersPage.statusFilter')}
          </InputLabel>
          <Select
            labelId="status-filter-label"
            value={statusFilter}
            onChange={handleStatusChange}
            label={t('adminUsersPage.statusFilter')}
          >
            <MenuItem value="ALL">
              {t('adminUsersPage.statusOptions.all')}
            </MenuItem>
            <MenuItem value={UserStatus.ACTIVE}>
              {t('adminUsersPage.statusOptions.active')}
            </MenuItem>
            <MenuItem value={UserStatus.BANNED}>
              {t('adminUsersPage.statusOptions.banned')}
            </MenuItem>
          </Select>
        </FormControl>
      </Box>

      {/* Results Table */}
      <ResultsTable columns={columns}>
        {isLoading && (
          <TableRow>
            <TableCell colSpan={4}>
              <Box sx={{ textAlign: 'center', py: 5 }}>
                {t('adminUsersPage.loading')}
              </Box>
            </TableCell>
          </TableRow>
        )}
        {isError && (
          <TableRow>
            <TableCell colSpan={4}>
              <Box sx={{ textAlign: 'center', py: 5 }}>
                {t('adminUsersPage.error')}
              </Box>
            </TableCell>
          </TableRow>
        )}
        {data &&
          data.users.map((user) => <RowUser key={user.id} user={user} />)}
      </ResultsTable>

      {/* Pagination Bar */}
      {data && (
        <PaginationBar
          currentPage={page}
          pageSize={pageSize}
          totalPages={data.totalPages}
          totalCount={data.totalCount}
        />
      )}
    </Box>
  );
};

export default AdminUsersPage;
