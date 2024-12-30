import React, { useState } from 'react';
import {
  Dialog,
  DialogTitle,
  DialogContent,
  DialogActions,
  Button,
  TextField,
  Typography,
} from '@mui/material';
import { useTranslation } from 'react-i18next';
import { useDeleteDirectoryMutation } from '../../../store/slices/directoriesApiSlice';
import { toast } from 'react-toastify';

import { Directory } from '../../../types';

interface DeleteDirectoryDialogProps {
  open: boolean;
  onClose: () => void;
  directory: Directory;
  shouldShowReason: boolean;
}

const DeleteDirectoryDialog: React.FC<DeleteDirectoryDialogProps> = ({
  open,
  onClose,
  directory,
  shouldShowReason,
}) => {
  const { t } = useTranslation();
  const [deleteDirectory] = useDeleteDirectoryMutation();
  const [reason, setReason] = useState('');

  const handleConfirm = async () => {
    try {
      const result = await deleteDirectory({
        directoryId: directory.id,
        reason: shouldShowReason ? reason : undefined,
      }).unwrap();
      if (result) {
        toast.success(t('directoryPage.deleteSuccess'));
        onClose();
      } else {
        toast.error(t('directoryPage.deleteError'));
      }
    } catch (err) {
      toast.error(t('directoryPage.deleteError'));
    }
  };

  return (
    <Dialog open={open} onClose={onClose} fullWidth maxWidth="xs">
      <DialogTitle>{t('directoryPage.deleteDirectoryTitle')}</DialogTitle>
      <DialogContent>
        <Typography>{t('directoryPage.deleteConfirmMessage')}</Typography>
        {shouldShowReason && (
          <TextField
            label={t('directoryPage.reason')}
            value={reason}
            onChange={(e) => setReason(e.target.value)}
            fullWidth
            margin="normal"
          />
        )}
      </DialogContent>
      <DialogActions>
        <Button onClick={onClose}>{t('directoryPage.cancel')}</Button>
        <Button onClick={handleConfirm} variant="contained" color="error">
          {t('directoryPage.delete')}
        </Button>
      </DialogActions>
    </Dialog>
  );
};

export default DeleteDirectoryDialog;
