// src/pages/AdminUserPage.tsx

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
import RowUser from '../../components/RowUser';
import PaginationBar from '../../components/PaginationBar';
import { useGetUsersQuery } from '../../store/slices/usersApiSlice';
import { UserStatus } from '../../types';
import { useSearchParams } from 'react-router-dom';

const AdminUserPage: React.FC = () => {
  const { t } = useTranslation();
  const [searchParams, setSearchParams] = useSearchParams();

  // Extract query parameters
  const emailFilter = searchParams.get('email') || '';
  const statusFilter =
    (searchParams.get('status') as UserStatus | 'ALL') || 'ALL';
  const page = parseInt(searchParams.get('page') || '1', 10);
  const pageSize = 10; // You can make this configurable if needed

  const { data, refetch, isLoading, isError } = useGetUsersQuery({
    email: emailFilter || undefined,
    status: statusFilter !== 'ALL' ? statusFilter : undefined,
    page,
    pageSize,
  });

  // Update the URL when filters change
  const handleEmailChange = (e: React.ChangeEvent<HTMLInputElement>) => {
    const newEmail = e.target.value;
    const params = new URLSearchParams(searchParams);
    if (newEmail) {
      params.set('email', newEmail);
    } else {
      params.delete('email');
    }
    params.set('page', '1'); // Reset to first page on filter change
    setSearchParams(params);
  };

  const handleStatusChange = (e: SelectChangeEvent<UserStatus | 'ALL'>) => {
    const newStatus = e.target.value as string;
    const params = new URLSearchParams(searchParams);
    if (newStatus !== 'ALL') {
      params.set('status', newStatus);
    } else {
      params.delete('status');
    }
    params.set('page', '1'); // Reset to first page on filter change
    setSearchParams(params);
  };

  const columns: Column[] = [
    { id: 'username', label: t('adminUserPage.columns.username') },
    { id: 'email', label: t('adminUserPage.columns.email') },
    { id: 'status', label: t('adminUserPage.columns.status') },
    {
      id: 'actions',
      label: t('adminUserPage.columns.actions'),
      align: 'right',
    },
  ];

  return (
    <Box sx={{ p: 3 }}>
      <Box
        sx={{
          display: 'flex',
          gap: 2,
          mb: 3,
          flexDirection: { xs: 'column', sm: 'row' },
        }}
      >
        <TextField
          label={t('adminUserPage.searchPlaceholder')}
          variant="outlined"
          value={emailFilter}
          onChange={handleEmailChange}
          fullWidth
        />
        <FormControl variant="outlined" sx={{ minWidth: 200 }}>
          <InputLabel id="status-filter-label">
            {t('adminUserPage.statusFilter')}
          </InputLabel>
          <Select
            labelId="status-filter-label"
            value={statusFilter}
            onChange={handleStatusChange}
            label={t('adminUserPage.statusFilter')}
          >
            <MenuItem value="ALL">
              {t('adminUserPage.statusOptions.all')}
            </MenuItem>
            <MenuItem value={UserStatus.ACTIVE}>
              {t('adminUserPage.statusOptions.active')}
            </MenuItem>
            <MenuItem value={UserStatus.BANNED}>
              {t('adminUserPage.statusOptions.banned')}
            </MenuItem>
          </Select>
        </FormControl>
      </Box>

      <ResultsTable columns={columns}>
        {isLoading && (
          <TableRow>
            <TableCell colSpan={4}>
              <Box sx={{ textAlign: 'center', py: 5 }}>
                {t('adminUserPage.loading')}
              </Box>
            </TableCell>
          </TableRow>
        )}
        {isError && (
          <TableRow>
            <TableCell colSpan={4}>
              <Box sx={{ textAlign: 'center', py: 5 }}>
                {t('adminUserPage.error')}
              </Box>
            </TableCell>
          </TableRow>
        )}
        {data &&
          data.users.map((user) => <RowUser key={user.id} user={user} />)}
      </ResultsTable>

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

export default AdminUserPage;
