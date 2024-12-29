// src/components/CreateNoteFab.tsx

import React, { useState, useRef } from 'react';
import {
  Box,
  Fab,
  Tooltip,
  Typography,
  TextField,
  Button,
  Select,
  MenuItem,
  FormControl,
  InputLabel,
  Switch,
  FormControlLabel,
  Paper,
  ClickAwayListener,
  SelectChangeEvent,
  CircularProgress,
} from '@mui/material';
import NoteAddIcon from '@mui/icons-material/NoteAdd';
import { useTranslation } from 'react-i18next';
import { toast } from 'react-toastify';
import { useCreateNoteMutation } from '../store/slices/notesApiSlice';
import { NoteCategory } from '../types';

interface CreateNoteFabProps {
  parentId: string;
}

const CreateNoteFab: React.FC<CreateNoteFabProps> = ({ parentId }) => {
  const { t } = useTranslation();

  const [createNote, { isLoading }] = useCreateNoteMutation();

  // Tracks if the form is expanded
  const [expanded, setExpanded] = useState(false);

  // Fields for creating a note
  const [noteName, setNoteName] = useState('');
  const [visible, setVisible] = useState(true);
  const [category, setCategory] = useState<NoteCategory>(NoteCategory.THEORY);
  const [file, setFile] = useState<File | null>(null);

  // Reference to the wrapper for ClickAwayListener
  const containerRef = useRef<HTMLDivElement | null>(null);

  // Toggles expansion upon FAB click
  const handleFabClick = () => {
    setExpanded((prev) => !prev);
  };

  // If user clicks away outside the box, close if expanded
  const handleClickAway = (_event: MouseEvent | TouchEvent) => {
    if (expanded) {
      // Only close if truly outside
      setExpanded(false);
    }
  };

  const handleCategoryChange = (e: SelectChangeEvent) => {
    setCategory(e.target.value as NoteCategory);
  };

  const handleCreate = async () => {
    if (!noteName || !file) {
      toast.error(t('createNoteFab.missingFields'));
      return;
    }

    try {
      const result = await createNote({
        name: noteName,
        parentId,
        visible,
        file,
        category,
      }).unwrap();

      if (result) {
        toast.success(t('createNoteFab.noteCreated'));
      } else {
        toast.error(t('createNoteFab.noteCreationFailed'));
      }
    } catch (error: any) {
      toast.error(
        error?.data?.[0]?.message || t('createNoteFab.noteCreationFailed'),
      );
      console.error('Failed to create note:', error);
    }

    // Reset form and close
    setNoteName('');
    setFile(null);
    setExpanded(false);
  };

  return (
    <ClickAwayListener onClickAway={handleClickAway}>
      <Box ref={containerRef}>
        {/* Floating Action Button (always visible) */}
        {!expanded && (
          <Tooltip title={t('createNoteFab.createNewNote')} placement="left">
            <Fab
              color="primary"
              onClick={handleFabClick}
              sx={{
                cursor: 'pointer',
              }}
            >
              <NoteAddIcon
                sx={{
                  color: 'background.paper',
                }}
              />
            </Fab>
          </Tooltip>
        )}

        {expanded && (
          <Paper
            sx={{
              p: 2,
              width: 300,
              borderRadius: 2,
              boxShadow: 4,
              display: 'flex',
              flexDirection: 'column',
              gap: 2,
            }}
            elevation={8}
          >
            <Typography variant="subtitle1" sx={{ mb: 1 }}>
              {t('createNoteFab.createNewNote')}
            </Typography>

            {/* Note Name */}
            <TextField
              size="small"
              label={t('createNoteFab.noteName')}
              value={noteName}
              onChange={(e) => setNoteName(e.target.value)}
              fullWidth
            />

            {/* Category */}
            <FormControl fullWidth size="small">
              <InputLabel>{t('createNoteFab.category')}</InputLabel>
              <Select
                label={t('createNoteFab.category')}
                value={category}
                onChange={handleCategoryChange}
                MenuProps={{
                  disablePortal: true,
                }}
              >
                {Object.values(NoteCategory).map((c) => (
                  <MenuItem key={c} value={c}>
                    {t(`createNoteFab.${c.toLowerCase()}`)}
                  </MenuItem>
                ))}
              </Select>
            </FormControl>

            {/* Visibility & File in one row */}
            <Box
              sx={{
                display: 'flex',
                flexDirection: 'row',
                gap: 1,
                alignItems: 'center',
                justifyContent: 'space-between',
              }}
            >
              <FormControlLabel
                control={
                  <Switch
                    checked={visible}
                    onChange={(e) => setVisible(e.target.checked)}
                  />
                }
                label={
                  visible
                    ? t('createNoteFab.visible')
                    : t('createNoteFab.hidden')
                }
                sx={{ mr: 1 }}
              />

              <Button
                variant="outlined"
                component="label"
                size="small"
                sx={{
                  textTransform: 'none',
                  flexShrink: 0,
                  maxWidth: '130px',
                  overflow: 'hidden',
                  textOverflow: 'ellipsis',
                  whiteSpace: 'nowrap',
                  display: 'block',
                  textAlign: 'left',
                }}
              >
                {file ? file.name : t('createNoteFab.chooseFile')}
                <input
                  type="file"
                  hidden
                  onChange={(e) => {
                    if (e.target.files && e.target.files[0]) {
                      setFile(e.target.files[0]);
                    }
                  }}
                />
              </Button>
            </Box>

            {/* Submit button */}
            <Box
              sx={{
                display: 'flex',
                justifyContent: 'flex-end',
                mt: 1,
              }}
            >
              {isLoading ? (
                <CircularProgress size={30} />
              ) : (
                <Button
                  variant="contained"
                  size="small"
                  onClick={handleCreate}
                  sx={{ textTransform: 'none' }}
                >
                  {t('createNoteFab.submit')}
                </Button>
              )}
            </Box>
          </Paper>
        )}
      </Box>
    </ClickAwayListener>
  );
};

export default CreateNoteFab;
