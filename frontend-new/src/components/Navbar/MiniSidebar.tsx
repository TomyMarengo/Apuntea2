// src/components/Navbar/MiniSidebar.tsx

import React from 'react';
import { Box, List, ListItem, ListItemIcon, Typography } from '@mui/material';
import { useTranslation } from 'react-i18next';
import FolderSharedIcon from '@mui/icons-material/FolderShared';
import NotesIcon from '@mui/icons-material/Notes';
import SchoolIcon from '@mui/icons-material/School';
import FavoriteIcon from '@mui/icons-material/Favorite';
import PersonOffIcon from '@mui/icons-material/PersonOff';
import ClassIcon from '@mui/icons-material/Class';
import { Link } from 'react-router-dom';

// Props for your mini sidebar (e.g. isAdmin).
interface MiniSidebarProps {
  isAdmin?: boolean;
}

const MiniSidebar: React.FC<MiniSidebarProps> = ({ isAdmin }) => {
  const { t } = useTranslation();

  const menuItems = [
    {
      label: t('miniSidebar.mynotes'),
      icon: <FolderSharedIcon sx={{ color: 'primary.main' }} />,
      to: '/notes',
    },
    {
      label: t('miniSidebar.myreviews'),
      icon: <NotesIcon sx={{ color: 'primary.main' }} />,
      to: '/reviews',
    },
    {
      label: t('miniSidebar.mycareer'),
      icon: <SchoolIcon sx={{ color: 'primary.main' }} />,
      to: '/career',
    },
    {
      label: t('miniSidebar.myfavorites'),
      icon: <FavoriteIcon sx={{ color: 'primary.main' }} />,
      to: '/favorites',
    },
  ];

  if (isAdmin) {
    menuItems.push(
      {
        label: t('miniSidebar.adminuser'),
        icon: <PersonOffIcon sx={{ color: 'primary.main' }} />,
        to: '/admin/users',
      },
      {
        label: t('miniSidebar.admincareers'),
        icon: <ClassIcon sx={{ color: 'primary.main' }} />,
        to: '/admin/careers',
      },
    );
  }

  return (
    <Box
      sx={{
        position: 'absolute',
        top: '80px',
        right: 0,
        width: 48,
        height: 'auto',
        backgroundColor: 'background.paper',
        borderTopLeftRadius: 8,
        borderBottomLeftRadius: 8,
        zIndex: 1100,
        '&:hover': {
          width: 180,
        },
        transition: 'width 0.3s',
        boxShadow: 3,
        overflow: 'hidden',
      }}
    >
      <List
        sx={{
          display: 'flex',
          flexDirection: 'column',
          gap: 1,
          py: 2,
          px: 0.5,
        }}
      >
        {menuItems.map((item, index) => (
          <ListItem
            key={index}
            component={Link}
            to={item.to}
            sx={{
              display: 'flex',
              flexDirection: 'row',
              alignItems: 'center',
              gap: 1,
              py: 1,
              px: 1,
              borderRadius: 1,
              '&:hover': {
                backgroundColor: 'action.hover',
              },
            }}
          >
            <ListItemIcon
              sx={{
                minWidth: 'auto',
                color: 'inherit',
              }}
            >
              {item.icon}
            </ListItemIcon>
            {/* When hovered (width > 12), show text. We rely on container expansion. */}
            <Typography
              sx={{
                whiteSpace: 'nowrap',
                overflow: 'hidden',
              }}
            >
              {item.label}
            </Typography>
          </ListItem>
        ))}
      </List>
    </Box>
  );
};

export default MiniSidebar;
