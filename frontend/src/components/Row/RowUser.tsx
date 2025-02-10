// src/components/Row/RowUser.tsx

import BlockIcon from '@mui/icons-material/Block';
import CheckCircleIcon from '@mui/icons-material/CheckCircle';
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
import { useState } from 'react';
import { FC } from 'react';
import { useTranslation } from 'react-i18next';
import { Link } from 'react-router-dom';
import { toast } from 'react-toastify';

import { useUpdateUserStatusMutation } from '../../store/slices/usersApiSlice';
import { User, UserStatus } from '../../types';

interface RowUserProps {
  user: User;
}

const RowUser: FC<RowUserProps> = ({ user }) => {
  const { t } = useTranslation('rowUser');
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
        banReason,
      }).unwrap();
      setOpenBanModal(false);
      toast.success(t('toast.ban.success'));
    } catch (error) {
      console.error('Failed to ban user:', error);
      toast.error(t('toast.ban.error'));
    }
  };

  const handleUnbanConfirm = async () => {
    try {
      await updateUserStatus({
        userId: user.id,
        status: UserStatus.ACTIVE,
      }).unwrap();
      setOpenUnbanModal(false);
      toast.success(t('toast.unban.success'));
    } catch (error) {
      console.error('Failed to unban user:', error);
      toast.error(t('toast.unban.error'));
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
        <TableCell>{t(`status.${user.status?.toLowerCase()}`)}</TableCell>
        <TableCell align="right">
          {user.status === UserStatus.ACTIVE ? (
            <Tooltip title={t('actions.ban')}>
              <IconButton onClick={handleBanClick} color="error">
                <BlockIcon />
              </IconButton>
            </Tooltip>
          ) : (
            <Tooltip title={t('actions.unban')}>
              <IconButton onClick={handleUnbanClick} color="success">
                <CheckCircleIcon />
              </IconButton>
            </Tooltip>
          )}
        </TableCell>
      </TableRow>

      {/* Ban Modal */}
      <Dialog open={openBanModal} onClose={() => setOpenBanModal(false)}>
        <DialogTitle>{t('modals.ban.title')}</DialogTitle>
        <DialogContent>
          <TextField
            autoFocus
            margin="dense"
            label={t('modals.ban.reasonLabel')}
            type="text"
            fullWidth
            variant="outlined"
            value={banReason}
            onChange={(e) => setBanReason(e.target.value)}
          />
        </DialogContent>
        <DialogActions>
          <Button onClick={() => setOpenBanModal(false)}>
            {t('modals.cancel')}
          </Button>
          <Button onClick={handleBanConfirm} disabled={!banReason}>
            {t('modals.confirm')}
          </Button>
        </DialogActions>
      </Dialog>

      {/* Unban Modal */}
      <Dialog open={openUnbanModal} onClose={() => setOpenUnbanModal(false)}>
        <DialogTitle>{t('modals.unban.title')}</DialogTitle>
        <DialogContent>{t('modals.unban.confirmation')}</DialogContent>
        <DialogActions>
          <Button onClick={() => setOpenUnbanModal(false)}>
            {t('modals.cancel')}
          </Button>
          <Button onClick={handleUnbanConfirm}>{t('modals.confirm')}</Button>
        </DialogActions>
      </Dialog>
    </>
  );
};

export default RowUser;
