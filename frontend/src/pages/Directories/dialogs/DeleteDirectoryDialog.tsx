// src/pages/Directories/dialogs/DeleteDirectoryDialog.tsx

import {
  Dialog,
  DialogTitle,
  DialogContent,
  DialogActions,
  Button,
  TextField,
  Typography,
} from '@mui/material';
import React, { useState } from 'react';
import { useTranslation } from 'react-i18next';
import { toast } from 'react-toastify';

import { useDeleteDirectoryMutation } from '../../../store/slices/directoriesApiSlice';
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
  const { t } = useTranslation('deleteDirectoryDialog');
  const [deleteDirectory] = useDeleteDirectoryMutation();
  const [reason, setReason] = useState('');

  const handleConfirm = async () => {
    try {
      const result = await deleteDirectory({
        directoryId: directory.id,
        reason: shouldShowReason ? reason : undefined,
      }).unwrap();
      if (result) {
        toast.success(t('deleteSuccess'));
        onClose();
      } else {
        toast.error(t('deleteError'));
      }
    } catch (error) {
      console.error('Failed to delete directory:', error);
      toast.error(t('deleteError'));
    }
  };

  return (
    <Dialog open={open} onClose={onClose} fullWidth maxWidth="xs">
      <DialogTitle>{t('deleteDirectoryTitle')}</DialogTitle>
      <DialogContent>
        <Typography>{t('deleteConfirmMessage')}</Typography>
        {shouldShowReason && (
          <TextField
            label={t('reason')}
            value={reason}
            onChange={(e) => setReason(e.target.value)}
            fullWidth
            margin="normal"
          />
        )}
      </DialogContent>
      <DialogActions>
        <Button onClick={onClose}>{t('cancel')}</Button>
        <Button onClick={handleConfirm} variant="contained" color="error">
          {t('delete')}
        </Button>
      </DialogActions>
    </Dialog>
  );
};

export default DeleteDirectoryDialog;
