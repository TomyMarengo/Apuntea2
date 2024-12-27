// src/components/RowUser.tsx

import {
  IconButton,
  Tooltip,
  TableRow,
  TableCell,
  Box,
  Dialog,
  DialogTitle,
  DialogContent,
  DialogActions,
  TextField,
  Button,
} from '@mui/material';
import { FC, useState } from 'react';
import { User, UserStatus } from '../types';
import { useUpdateUserStatusMutation } from '../store/slices/usersApiSlice';
import { useTranslation } from 'react-i18next';
import BlockIcon from '@mui/icons-material/Block';
import CheckCircleIcon from '@mui/icons-material/CheckCircle';

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
    } catch (error) {
      console.error('Failed to ban user:', error);
      // Optionally, handle error with a toast or similar
    }
  };

  const handleUnbanConfirm = async () => {
    try {
      await updateUserStatus({
        userId: user.id,
        status: UserStatus.ACTIVE,
      }).unwrap();
      setOpenUnbanModal(false);
    } catch (error) {
      console.error('Failed to unban user:', error);
      // Optionally, handle error with a toast or similar
    }
  };

  return (
    <>
      <TableRow hover>
        <TableCell>{user.username || '-'}</TableCell>
        <TableCell>{user.email}</TableCell>
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
