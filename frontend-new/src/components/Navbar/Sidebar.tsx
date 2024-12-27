// src/components/Navbar/Sidebar.tsx

import { Drawer, List, ListItem, ListItemIcon, Tooltip } from '@mui/material';
import { Link } from 'react-router-dom';
import { useTranslation } from 'react-i18next';
import { useSelector } from 'react-redux';
import FolderSharedIcon from '@mui/icons-material/FolderShared';
import NotesIcon from '@mui/icons-material/Notes';
import SchoolIcon from '@mui/icons-material/School';
import FavoriteIcon from '@mui/icons-material/Favorite';
import PersonOffIcon from '@mui/icons-material/PersonOff';
import ClassIcon from '@mui/icons-material/Class';
import { RootState } from '../../store/store';

const Sidebar = () => {
  const { t } = useTranslation();

  const token = useSelector((state: RootState) => state.auth.token);

  return (
    <Drawer
      variant="permanent"
      sx={{
        width: 60,
        flexShrink: 0,
        [`& .MuiDrawer-paper`]: {
          width: 60,
          boxSizing: 'border-box',
          marginTop: '64px', // After the Navbar
          height: 'calc(100% - 64px)',
          overflow: 'hidden', // Prevent horizontal scrolling
          backgroundColor: 'background.paper',
          borderRight: 'none',
        },
      }}
    >
      <List sx={{ display: 'flex', flexDirection: 'column', gap: 1 }}>
        <Tooltip title={t('sidebar.mynotes')} placement="right" arrow>
          <ListItem component={Link} to="/notes" sx={{ textAlign: 'center' }}>
            <ListItemIcon>
              <FolderSharedIcon sx={{ color: 'primary.main' }} />
            </ListItemIcon>
          </ListItem>
        </Tooltip>

        <Tooltip title={t('sidebar.myreviews')} placement="right" arrow>
          <ListItem component={Link} to="/reviews" sx={{ textAlign: 'center' }}>
            <ListItemIcon>
              <NotesIcon sx={{ color: 'primary.main' }} />
            </ListItemIcon>
          </ListItem>
        </Tooltip>

        <Tooltip title={t('sidebar.mycareer')} placement="right" arrow>
          <ListItem component={Link} to="/career" sx={{ textAlign: 'center' }}>
            <ListItemIcon>
              <SchoolIcon sx={{ color: 'primary.main' }} />
            </ListItemIcon>
          </ListItem>
        </Tooltip>

        <Tooltip title={t('sidebar.myfavorites')} placement="right" arrow>
          <ListItem
            component={Link}
            to="/favorites"
            sx={{ textAlign: 'center' }}
          >
            <ListItemIcon>
              <FavoriteIcon sx={{ color: 'primary.main' }} />
            </ListItemIcon>
          </ListItem>
        </Tooltip>

        {token?.payload?.authorities.includes('ROLE_ADMIN') && (
          <>
            <Tooltip title={t('sidebar.adminuser')} placement="right" arrow>
              <ListItem
                component={Link}
                to="/admin/users"
                sx={{ textAlign: 'center' }}
              >
                <ListItemIcon>
                  <PersonOffIcon sx={{ color: 'primary.main' }} />
                </ListItemIcon>
              </ListItem>
            </Tooltip>

            <Tooltip title={t('sidebar.admincareers')} placement="right" arrow>
              <ListItem
                component={Link}
                to="/admin/careers"
                sx={{ textAlign: 'center' }}
              >
                <ListItemIcon>
                  <ClassIcon sx={{ color: 'primary.main' }} />
                </ListItemIcon>
              </ListItem>
            </Tooltip>
          </>
        )}
      </List>
    </Drawer>
  );
};

export default Sidebar;
