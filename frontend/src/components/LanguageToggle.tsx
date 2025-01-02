// src/components/LanguageToggle.tsx

import LanguageIcon from '@mui/icons-material/Language';
import { IconButton, Menu, MenuItem } from '@mui/material';
import React from 'react';
import { useState } from 'react';
import { useDispatch } from 'react-redux';

import { setLocale } from '../store/slices/languageSlice';
import { Locale } from '../types';

export default function LanguageToggle() {
  const dispatch = useDispatch();
  const [anchorEl, setAnchorEl] = useState<null | HTMLElement>(null);

  const handleOpen = (event: React.MouseEvent<HTMLElement>) => {
    setAnchorEl(event.currentTarget);
  };

  const handleClose = (lang?: Locale) => {
    if (lang) {
      dispatch(setLocale(lang));
    }
    setAnchorEl(null);
  };

  return (
    <>
      <IconButton onClick={handleOpen}>
        <LanguageIcon sx={{ color: 'primary.main' }} />
      </IconButton>
      <Menu
        anchorEl={anchorEl}
        open={Boolean(anchorEl)}
        onClose={() => handleClose()}
      >
        <MenuItem onClick={() => handleClose('es' as Locale)}>Español</MenuItem>
        <MenuItem onClick={() => handleClose('en' as Locale)}>English</MenuItem>
      </Menu>
    </>
  );
}
