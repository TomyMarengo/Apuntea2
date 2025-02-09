// src/pages/Notes/NotesPage.tsx

import {
  Box,
  CircularProgress,
  FormControlLabel,
  Switch,
  Typography,
} from '@mui/material';
import React, { useEffect, useState } from 'react';
import { Helmet } from 'react-helmet-async';
import { useTranslation } from 'react-i18next';
import { useSelector } from 'react-redux';
import { useLocation, useNavigate } from 'react-router-dom';

import { selectCurrentUser } from '../../store/slices/authSlice';
import UserNotes from '../Users/UserNotes';

export default function NotesPage() {
  const { t } = useTranslation('notesPage');

  // Current logged user
  const user = useSelector(selectCurrentUser);
  const userId = user?.id;
  const [isChecked, setIsChecked] = useState(true);

  const location = useLocation();
  const navigate = useNavigate();

  useEffect(() => {
    const params = new URLSearchParams(location.search);
    const switchState = params.get('onlyMyNotes') === 'true';
    setIsChecked(switchState);
  }, [location.search]);

  const handleSwitchChange = (event: React.ChangeEvent<HTMLInputElement>) => {
    const newCheckedState = event.target.checked;
    setIsChecked(newCheckedState);

    const params = new URLSearchParams(location.search);
    if (newCheckedState) {
      params.set('onlyMyNotes', 'true');
    } else {
      params.delete('onlyMyNotes');
    }

    navigate({
      pathname: location.pathname,
      search: params.toString(),
    });
  };
  const notesUserId = isChecked ? userId : undefined;

  return (
    <>
      <Helmet>
        <title>{t('titlePage')}</title>
      </Helmet>
      {!userId || !user?.career ? (
        <Box display="flex" justifyContent="center" alignItems="center">
          <CircularProgress />
        </Box>
      ) : (
        <Box sx={{ p: 3 }}>
          <Typography variant="h5" sx={{ mb: 3 }}>
            {t('myNotes')}
          </Typography>
          <FormControlLabel
            control={
              <Switch
                defaultChecked
                checked={isChecked}
                onChange={handleSwitchChange}
              />
            }
            label={t('onlyMyNotes')}
          />
          {/* Title */}
          <UserNotes userId={notesUserId} career={user?.career} />
        </Box>
      )}
    </>
  );
}
