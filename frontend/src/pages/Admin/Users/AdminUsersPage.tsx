// src/pages/Admin/Users/AdminUsersPage.tsx

import { zodResolver } from '@hookform/resolvers/zod';
import {
  Box,
  TextField,
  Select,
  MenuItem,
  InputLabel,
  FormControl,
  TableRow,
  TableCell,
  Typography,
  CircularProgress,
} from '@mui/material';
import React, { useEffect, useMemo, useRef, useCallback } from 'react';
import { Helmet } from 'react-helmet-async';
import { useForm, Controller } from 'react-hook-form';
import { useTranslation } from 'react-i18next';
import { useSearchParams, useNavigate } from 'react-router-dom';
import { z } from 'zod';

import PaginationBar from '../../../components/PaginationBar';
import ResultsTable from '../../../components/ResultsTable';
import RowUser from '../../../components/Row/RowUser';
import { useLazyGetUsersQuery } from '../../../store/slices/usersApiSlice';
import { UserStatus, ColumnUser } from '../../../types';

interface UsersFormValues {
  query: string;
  status: UserStatus | 'ALL';
  page: string;
  pageSize: string;
}

const searchSchema = z.object({
  query: z.string().optional(),
  status: z.string().optional(),
  page: z.string().optional(),
  pageSize: z.string().optional(),
});

const AdminUsersPage: React.FC = () => {
  const { t } = useTranslation('adminUsersPage');
  const [searchParams] = useSearchParams();
  const navigate = useNavigate();

  const defaultValues = useMemo(
    () => ({
      query: searchParams.get('query') || '',
      status: (searchParams.get('status') as UserStatus | 'ALL') || 'ALL',
      page: searchParams.get('page') || '1',
      pageSize: searchParams.get('pageSize') || '10',
    }),
    [searchParams],
  );

  const { control, watch, reset } = useForm<UsersFormValues>({
    resolver: zodResolver(searchSchema),
    defaultValues,
  });

  const watchedValues = watch();

  const [getUsers, { data, isLoading, isError }] = useLazyGetUsersQuery();

  const fetchUsers = async (data: UsersFormValues) => {
    const { query, status, page, pageSize } = data;
    await getUsers({
      query,
      status: status !== 'ALL' ? status : undefined,
      page: parseInt(page, 10),
      pageSize: parseInt(pageSize, 10),
    });
  };

  useEffect(() => {
    reset(defaultValues);
    fetchUsers(defaultValues);
  }, [searchParams]);

  const handleStatusChange = (value: string) => {
    const newParams = new URLSearchParams(searchParams);
    newParams.set('status', value);
    newParams.set('page', '1');
    navigate(`?${newParams.toString()}`);
  };

  const debounceTimeout = useRef<NodeJS.Timeout | null>(null);

  // Debounced handler for query changes
  const handleQueryChange = useCallback(
    (value: string) => {
      if (debounceTimeout.current) {
        clearTimeout(debounceTimeout.current);
      }

      debounceTimeout.current = setTimeout(() => {
        const newParams = new URLSearchParams(window.location.search);
        if (!value) {
          newParams.delete('query');
        } else {
          newParams.set('query', value);
        }
        newParams.set('page', '1');
        navigate(`?${newParams.toString()}`);
      }, 500);
    },
    [navigate],
  );

  // Cleanup the debounce timeout on unmount
  useEffect(() => {
    return () => {
      if (debounceTimeout.current) {
        clearTimeout(debounceTimeout.current);
      }
    };
  }, []);

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
          <Controller
            name="query"
            control={control}
            render={({ field }) => (
              <TextField
                {...field}
                label={t('searchPlaceholder')}
                variant="outlined"
                onChange={(e) => {
                  field.onChange(e);
                  handleQueryChange(e.target.value);
                }}
                fullWidth
              />
            )}
          />

          {/* Status Filter Dropdown */}
          <FormControl variant="outlined" sx={{ minWidth: 200 }}>
            <InputLabel id="status-filter-label">
              {t('statusFilter')}
            </InputLabel>
            <Controller
              name="status"
              control={control}
              render={({ field }) => (
                <Select
                  {...field}
                  labelId="status-filter-label"
                  label={t('statusFilter')}
                  onChange={(e) => {
                    field.onChange(e);
                    handleStatusChange(e.target.value as string);
                  }}
                >
                  <MenuItem value="ALL">{t('statusOptions.all')}</MenuItem>
                  <MenuItem value={UserStatus.ACTIVE}>
                    {t('statusOptions.active')}
                  </MenuItem>
                  <MenuItem value={UserStatus.BANNED}>
                    {t('statusOptions.banned')}
                  </MenuItem>
                </Select>
              )}
            />
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
            currentPage={Number(watchedValues.page)}
            pageSize={Number(watchedValues.pageSize)}
            totalPages={data.totalPages}
            totalCount={data.totalCount}
          />
        )}
      </Box>
    </>
  );
};

export default AdminUsersPage;
