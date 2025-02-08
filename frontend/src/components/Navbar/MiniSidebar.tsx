// src/components/Navbar/MiniSidebar.tsx

import ClassIcon from '@mui/icons-material/Class';
import FavoriteIcon from '@mui/icons-material/Favorite';
import FolderSharedIcon from '@mui/icons-material/FolderShared';
import NotesIcon from '@mui/icons-material/Notes';
import PersonOffIcon from '@mui/icons-material/PersonOff';
import SchoolRoundedIcon from '@mui/icons-material/SchoolRounded';
import {
  Box,
  Divider,
  List,
  ListItem,
  ListItemIcon,
  Typography,
} from '@mui/material';
import React from 'react';
import { useTranslation } from 'react-i18next';
import { Link } from 'react-router-dom';

// Props for your mini sidebar (e.g. isAdmin).
interface MiniSidebarProps {
  isAdmin?: boolean;
}

const MiniSidebar: React.FC<MiniSidebarProps> = ({ isAdmin }) => {
  const { t } = useTranslation('miniSidebar');

  // Main menu items
  const mainMenuItems = [
    {
      label: t('mynotes'),
      icon: <FolderSharedIcon sx={{ color: 'primary.main' }} />,
      to: '/notes',
    },
    {
      label: t('mycareer'),
      icon: <SchoolRoundedIcon sx={{ color: 'primary.main' }} />,
      to: '/mycareer',
    },
    {
      label: t('myfavorites'),
      icon: <FavoriteIcon sx={{ color: 'primary.main' }} />,
      to: '/favorites',
    },
    {
      label: t('myreviews'),
      icon: <NotesIcon sx={{ color: 'primary.main' }} />,
      to: '/reviews',
    },
  ];

  // Admin menu items
  const adminMenuItems = [
    {
      label: t('adminuser'),
      icon: <PersonOffIcon sx={{ color: 'secondary.main' }} />,
      to: '/admin/users',
    },
    {
      label: t('admincareers'),
      icon: <ClassIcon sx={{ color: 'secondary.main' }} />,
      to: '/admin/careers',
    },
  ];

  return (
    <Box
      sx={{
        position: 'relative',
        mt: 6,
        width: 48,
        height: 'auto',
        backgroundColor: 'background.paper',
        borderTopRightRadius: 8,
        borderBottomRightRadius: 8,
        zIndex: 1100,
        '&:hover': {
          width: 180,
        },
        transition: 'width 0.3s',
        boxShadow: 3,
        overflow: 'hidden',
        '&:hover .admin-label': { opacity: 1 },
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
        {/* Render main menu items */}
        {mainMenuItems.map((item, index) => (
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
            {/* When hovered (width > 48), show text. We rely on container expansion. */}
            <Typography
              sx={{
                whiteSpace: 'nowrap',
                ml: 0.5,
              }}
            >
              {item.label}
            </Typography>
          </ListItem>
        ))}

        {/* If isAdmin, render Divider and Admin subtitle */}
        {isAdmin && (
          <>
            <Divider sx={{ my: 1 }} />
            <Typography
              className="admin-label"
              variant="subtitle1"
              sx={{
                px: 1,
                opacity: 0,
                transition: 'opacity 0.3s',
              }}
            >
              {t('admin')}
            </Typography>
            {adminMenuItems.map((item, index) => (
              <ListItem
                key={`admin-${index}`}
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
                <Typography
                  sx={{
                    whiteSpace: 'nowrap',
                    ml: 0.5,
                  }}
                >
                  {item.label}
                </Typography>
              </ListItem>
            ))}
          </>
        )}
      </List>
    </Box>
  );
};

export default MiniSidebar;
