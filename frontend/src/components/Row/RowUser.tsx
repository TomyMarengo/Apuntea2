// src/components/Row/RowUser.tsx

import {
  IconButton,
  Tooltip,
  TableRow,
  TableCell,
  Dialog,
  DialogTitle,
  DialogContent,
  DialogActions,
  TextField,
  Button,
  Link as MuiLink,
} from '@mui/material';
import { FC, useState } from 'react';
import { Link } from 'react-router-dom';
import { User, UserStatus } from '../../types';
import { useUpdateUserStatusMutation } from '../../store/slices/usersApiSlice';
import { useTranslation } from 'react-i18next';
import BlockIcon from '@mui/icons-material/Block';
import CheckCircleIcon from '@mui/icons-material/CheckCircle';
import { toast } from 'react-toastify';
import { Column } from '../../types';

export const ColumnUser: Column[] = [
  { id: 'username', label: 'adminUsersPage.columns.username' },
  { id: 'email', label: 'adminUsersPage.columns.email' },
  { id: 'status', label: 'adminUsersPage.columns.status' },
  {
    id: 'actions',
    label: 'adminUsersPage.columns.actions',
    align: 'right',
  },
];

interface RowUserProps {
  user: User;
}

const RowUser: FC<RowUserProps> = ({ user }) => {
  const { t } = useTranslation();
  const [updateUserStatus] = useUpdateUserStatusMutation();
  const [openBanModal, setOpenBanModal] = useState(false);
  const [openUnbanModal, setOpenUnbanModal] = useState(false);
  const [banReason, setBanReason] = useState('');

  const handleBanClick = () => {
    setOpenBanModal(true);
  };

  const handleUnbanClick = () => {
    setOpenUnbanModal(true);
  };

  const handleBanConfirm = async () => {
    try {
      await updateUserStatus({
        userId: user.id,
        status: UserStatus.BANNED,
        banReason, // Assuming your API accepts a banReason field
      }).unwrap();
      setOpenBanModal(false);
      toast.success(t('rowUser.toast.ban.success')); // Success toast
    } catch (error) {
      console.error('Failed to ban user:', error);
      toast.error(t('rowUser.toast.ban.failure')); // Error toast
    }
  };

  const handleUnbanConfirm = async () => {
    try {
      await updateUserStatus({
        userId: user.id,
        status: UserStatus.ACTIVE,
      }).unwrap();
      setOpenUnbanModal(false);
      toast.success(t('rowUser.toast.unban.success')); // Success toast
    } catch (error) {
      console.error('Failed to unban user:', error);
      toast.error(t('rowUser.toast.unban.failure')); // Error toast
    }
  };

  return (
    <>
      <TableRow hover>
        <TableCell>{user.username || '-'}</TableCell>
        <TableCell>
          <MuiLink component={Link} to={`/users/${user?.id}`} underline="hover">
            {user.email}
          </MuiLink>
        </TableCell>
        <TableCell>
          {t(`rowUser.status.${user.status?.toLowerCase()}`)}
        </TableCell>
        <TableCell align="right">
          {user.status === UserStatus.ACTIVE ? (
            <Tooltip title={t('rowUser.actions.ban')}>
              <IconButton onClick={handleBanClick} color="error">
                <BlockIcon />
              </IconButton>
            </Tooltip>
          ) : (
            <Tooltip title={t('rowUser.actions.unban')}>
              <IconButton onClick={handleUnbanClick} color="success">
                <CheckCircleIcon />
              </IconButton>
            </Tooltip>
          )}
        </TableCell>
      </TableRow>

      {/* Ban Modal */}
      <Dialog open={openBanModal} onClose={() => setOpenBanModal(false)}>
        <DialogTitle>{t('rowUser.modals.ban.title')}</DialogTitle>
        <DialogContent>
          <TextField
            autoFocus
            margin="dense"
            label={t('rowUser.modals.ban.reasonLabel')}
            type="text"
            fullWidth
            variant="outlined"
            value={banReason}
            onChange={(e) => setBanReason(e.target.value)}
          />
        </DialogContent>
        <DialogActions>
          <Button onClick={() => setOpenBanModal(false)}>
            {t('rowUser.modals.cancel')}
          </Button>
          <Button onClick={handleBanConfirm} disabled={!banReason}>
            {t('rowUser.modals.confirm')}
          </Button>
        </DialogActions>
      </Dialog>

      {/* Unban Modal */}
      <Dialog open={openUnbanModal} onClose={() => setOpenUnbanModal(false)}>
        <DialogTitle>{t('rowUser.modals.unban.title')}</DialogTitle>
        <DialogContent>{t('rowUser.modals.unban.confirmation')}</DialogContent>
        <DialogActions>
          <Button onClick={() => setOpenUnbanModal(false)}>
            {t('rowUser.modals.cancel')}
          </Button>
          <Button onClick={handleUnbanConfirm}>
            {t('rowUser.modals.confirm')}
          </Button>
        </DialogActions>
      </Dialog>
    </>
  );
};

export default RowUser;
