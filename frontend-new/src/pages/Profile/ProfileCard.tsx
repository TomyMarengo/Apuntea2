// src/pages/Profile/ProfileCard.tsx

import React, { useState } from 'react';
import {
  Card,
  CardContent,
  Typography,
  Avatar,
  IconButton,
  Box,
  Button,
} from '@mui/material';
import { Edit as EditIcon } from '@mui/icons-material';
import { useTranslation } from 'react-i18next';
import EditProfileDialog from './EditProfileDialog.tsx';
import { User } from '../../types';

interface ProfileCardProps {
  user: User;
  onUpdateSuccess: () => void;
}

const ProfileCard: React.FC<ProfileCardProps> = ({ user, onUpdateSuccess }) => {
  const { t } = useTranslation();
  const [open, setOpen] = useState(false);

  const handleOpen = () => setOpen(true);
  const handleClose = () => setOpen(false);

  return (
    <>
      <Card sx={{ display: 'flex', alignItems: 'center', p: 3, boxShadow: 3 }}>
        <Avatar
          src={user.profilePictureUrl || ''}
          alt={user.username}
          sx={{ width: 120, height: 120, mr: 4 }}
        />
        <Box sx={{ flex: 1 }}>
          <Box sx={{ display: 'flex', alignItems: 'center' }}>
            <Typography variant="h5" gutterBottom>
              {user.firstName} {user.lastName}
            </Typography>
            <IconButton onClick={handleOpen} sx={{ ml: 1 }}>
              <EditIcon />
            </IconButton>
          </Box>
          <Typography variant="body1" color="textSecondary">
            {t('username')}: {user.username}
          </Typography>
          <Typography variant="body1" color="textSecondary">
            {t('email')}: {user.email}
          </Typography>
          <Typography variant="body1" color="textSecondary">
            {t('career')}: {user.career?.name || t('notSet')}
          </Typography>
          <Typography variant="body1" color="textSecondary">
            {t('institution')}: {user.institution?.name || t('notSet')}
          </Typography>
        </Box>
      </Card>

      <EditProfileDialog
        open={open}
        handleClose={handleClose}
        user={user}
        onUpdateSuccess={onUpdateSuccess}
      />
    </>
  );
};

export default ProfileCard;
