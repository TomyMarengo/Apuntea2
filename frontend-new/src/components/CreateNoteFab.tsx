// src/components/CreateNoteFab.tsx

import React, { useState, useEffect, useRef } from 'react';
import {
  Box,
  Fab,
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
} from '@mui/material';
import AddIcon from '@mui/icons-material/Add';
import { useTranslation } from 'react-i18next';
import { toast } from 'react-toastify';
import { useSelector } from 'react-redux';
import { selectCurrentUser } from '../store/slices/authSlice';
import { useNavigate } from 'react-router-dom';
import { useGetSubjectsByCareerQuery } from '../store/slices/institutionsApiSlice';
import { useCreateNoteMutation } from '../store/slices/notesApiSlice';
import { Category, Subject } from '../types';

const CreateNoteFab: React.FC = () => {
  const { t } = useTranslation();
  const navigate = useNavigate();
  const user = useSelector(selectCurrentUser);
  const isLoggedIn = !!user;

  const [createNote] = useCreateNoteMutation();

  const careerId = user?.career?.id;

  // Tracks if the form is expanded
  const [expanded, setExpanded] = useState(false);

  const { data: subjectsData } = useGetSubjectsByCareerQuery(
    { careerId: careerId || '' },
    { skip: !careerId || !expanded },
  );

  // Fields for creating a note
  const [noteName, setNoteName] = useState('');
  const [visible, setVisible] = useState(true);
  const [category, setCategory] = useState<Category>(Category.NOTE);
  const [selectedSubject, setSelectedSubject] = useState<string>('');
  const [file, setFile] = useState<File | null>(null);

  // Reference to the wrapper for ClickAwayListener
  const containerRef = useRef<HTMLDivElement | null>(null);

  // Toggles expansion upon FAB click
  const handleFabClick = () => {
    setExpanded((prev) => !prev);
  };

  // If user clicks away outside the box, close if expanded
  const handleClickAway = (event: MouseEvent | TouchEvent) => {
    if (expanded) {
      // Only close if truly outside
      setExpanded(false);
    }
  };

  // Changing the subject or category from the <Select> without closing form
  const handleSubjectChange = (e: SelectChangeEvent) => {
    setSelectedSubject(e.target.value as string);
  };

  const handleCategoryChange = (e: SelectChangeEvent) => {
    setCategory(e.target.value as Category);
  };

  const handleCreate = async () => {
    if (!isLoggedIn) {
      toast.info(t('createNoteFab.loginRequired'));
      navigate('/login');
      return;
    }
    if (!noteName || !file || !selectedSubject) {
      toast.error(t('createNoteFab.missingFields'));
      return;
    }

    // Find the subject
    const subjectObj = subjectsData?.find((s) => s.id === selectedSubject);
    if (!subjectObj || !subjectObj.rootDirectoryUrl) {
      toast.error(t('createNoteFab.noDirectory'));
      return;
    }
    const splitted = subjectObj.rootDirectoryUrl.split('/');
    const parentId = splitted[splitted.length - 1];

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
    setSelectedSubject('');
    setExpanded(false);
  };

  return (
    <ClickAwayListener onClickAway={handleClickAway}>
      <Box
        ref={containerRef}
        sx={{
          position: 'fixed',
          bottom: 16,
          right: 16,
          zIndex: 1300,
        }}
      >
        {/* Floating Action Button (always visible) */}
        {!expanded && (
          <Fab
            color="primary"
            onClick={handleFabClick}
            sx={{
              cursor: 'pointer',
            }}
          >
            <AddIcon />
          </Fab>
        )}

        {expanded && (
          <Paper
            sx={{
              p: 2,
              mt: 2,
              width: 300,
              borderRadius: 2,
              boxShadow: 4,
              display: 'flex',
              flexDirection: 'column',
              gap: 1,
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

            {/* Subject */}
            <FormControl fullWidth size="small">
              <InputLabel>{t('createNoteFab.selectSubject')}</InputLabel>
              <Select
                label={t('createNoteFab.selectSubject')}
                value={selectedSubject}
                onChange={handleSubjectChange}
                // Disable portal so the menu is inside this container
                MenuProps={{
                  disablePortal: true,
                }}
              >
                {subjectsData?.map((sub: Subject) => (
                  <MenuItem key={sub.id} value={sub.id}>
                    {sub.name}
                  </MenuItem>
                ))}
              </Select>
            </FormControl>

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
                {Object.values(Category).map((c) => (
                  <MenuItem key={c} value={c}>
                    {c}
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
                sx={{ textTransform: 'none', flexShrink: 0 }}
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
              <Button
                variant="contained"
                size="small"
                onClick={handleCreate}
                sx={{ textTransform: 'none' }}
              >
                {t('createNoteFab.submit')}
              </Button>
            </Box>
          </Paper>
        )}
      </Box>
    </ClickAwayListener>
  );
};

export default CreateNoteFab;
