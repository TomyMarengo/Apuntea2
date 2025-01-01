import React, { useEffect, useState } from 'react';
import {
  Dialog,
  DialogTitle,
  DialogContent,
  DialogActions,
  Button,
  TextField,
  FormControlLabel,
  Switch,
  Box,
  Typography,
  ToggleButton,
  ToggleButtonGroup,
} from '@mui/material';
import { useTranslation } from 'react-i18next';
import { useUpdateDirectoryMutation } from '../../../store/slices/directoriesApiSlice';
import { toast } from 'react-toastify';
import { Directory } from '../../../types';
import { FolderIconColor } from '../../../types';

interface DirectoryPageProps {
  open: boolean;
  onClose: () => void;
  directory: Directory;
  showNameOnly?: boolean;
}

const EditDirectoryDialog: React.FC<DirectoryPageProps> = ({
  open,
  onClose,
  directory,
  showNameOnly = false,
}) => {
  const { t } = useTranslation();
  const [updateDirectory] = useUpdateDirectoryMutation();

  const [name, setName] = useState(directory.name);
  const [visible, setVisible] = useState(directory.visible);
  const [iconColor, setIconColor] = useState(directory.iconColor);

  useEffect(() => {
    if (open) {
      setName(directory.name);
      setVisible(directory.visible);
      setIconColor(directory.iconColor);
    }
  }, [open, directory]);

  const handleSave = async () => {
    try {
      const result = await updateDirectory({
        directoryId: directory.id,
        name,
        visible,
        iconColor,
      }).unwrap();
      if (result) {
        toast.success(t('directoryPage.editSuccess'));
        onClose();
      }
    } catch (err) {
      toast.error(t('directoryPage.editFailed'));
    }
  };

  return (
    <Dialog open={open} onClose={onClose} fullWidth maxWidth="xs">
      <DialogTitle>{t('directoryPage.editDirectoryTitle')}</DialogTitle>
      <DialogContent>
        <TextField
          margin="normal"
          fullWidth
          label={t('directoryPage.name')}
          value={name}
          onChange={(e) => setName(e.target.value)}
        />
        {/* Selector de color agregado */}
        {!showNameOnly && (
          <>
            <Box sx={{ mb: 1 }}>
              <Typography variant="subtitle2" sx={{ mb: 1 }}>
                {t('directoryPage.iconColor')}
              </Typography>
              <ToggleButtonGroup
                value={iconColor}
                exclusive
                onChange={(_, value) => {
                  if (value !== null) {
                    setIconColor(value);
                  }
                }}
                aria-label="icon color"
                sx={{ gap: 1 }}
              >
                {Object.entries(FolderIconColor).map(([label, value]) => (
                  <ToggleButton
                    key={value}
                    value={value}
                    aria-label={label}
                    sx={{
                      width: 40,
                      height: 40,
                      minWidth: 40,
                      border: 'none',
                      borderRadius: '50%',
                      backgroundColor: value,
                      transition: 'transform 0.2s, border 0.2s',
                      '&.Mui-selected, &.Mui-selected:hover, &:hover': {
                        transform: 'scale(1.1)',
                        border: '2px solid #FFFFFF',
                        backgroundColor: value,
                      },
                    }}
                  />
                ))}
              </ToggleButtonGroup>
            </Box>
            <FormControlLabel
              control={
                <Switch
                  checked={visible}
                  onChange={(e) => setVisible(e.target.checked)}
                  color="primary"
                />
              }
              label={t('directoryPage.visible')}
            />
          </>
        )}
      </DialogContent>
      <DialogActions>
        <Button onClick={onClose} color="primary">
          {t('directoryPage.cancel')}
        </Button>
        <Button onClick={handleSave} variant="contained">
          {t('directoryPage.save')}
        </Button>
      </DialogActions>
    </Dialog>
  );
};

export default EditDirectoryDialog;
