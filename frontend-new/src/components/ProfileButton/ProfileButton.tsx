import React from 'react';
import { useDispatch, useSelector } from 'react-redux';
import { useNavigate } from 'react-router-dom';
import { logOut, selectCurrentUser } from '../../store/slices/authSlice';
import {
  Button,
  Menu,
  MenuItem,
  Typography,
  Divider,
  Box,
  useTheme,
} from '@mui/material';
import AccountCircleIcon from '@mui/icons-material/AccountCircle';
import ExpandMoreIcon from '@mui/icons-material/ExpandMore';
import ExpandLessIcon from '@mui/icons-material/ExpandLess';
import { useTranslation } from 'react-i18next';

const ProfileButton: React.FC = () => {
  const dispatch = useDispatch();
  const navigate = useNavigate();
  const user = useSelector(selectCurrentUser);
  const { t } = useTranslation();
  const theme = useTheme();

  const [anchorEl, setAnchorEl] = React.useState<null | HTMLElement>(null);
  const open = Boolean(anchorEl);

  const handleMenuOpen = (event: React.MouseEvent<HTMLElement>) => {
    setAnchorEl(event.currentTarget);
  };

  const handleMenuClose = () => {
    setAnchorEl(null);
  };

  const handleLogOut = () => {
    dispatch(logOut());
    navigate('/');
    handleMenuClose();
  };

  return (
    <>
      <Button
        onClick={handleMenuOpen}
        color="primary"
        variant="contained"
        endIcon={
          open ? (
            <ExpandLessIcon
              sx={{
                color: 'background.paper',
              }}
            />
          ) : (
            <ExpandMoreIcon
              sx={{
                color: 'background.paper',
              }}
            />
          )
        }
        sx={{
          display: 'flex',
          alignItems: 'center',
          textTransform: 'none',
          padding: '6px 12px',
          borderRadius: '24px',
          backgroundColor: 'primary.main',
          '&:hover': {
            backgroundColor: 'primary.dark',
          },
        }}
        aria-controls={open ? 'profile-menu' : undefined}
        aria-haspopup="true"
        aria-expanded={open ? 'true' : undefined}
      >
        <AccountCircleIcon
          sx={{
            color: 'background.paper',
            borderRadius: '50%',
          }}
        />
      </Button>

      <Menu
        id="profile-menu"
        anchorEl={anchorEl}
        open={open}
        onClose={handleMenuClose}
        anchorOrigin={{
          vertical: 'bottom',
          horizontal: 'right',
        }}
        transformOrigin={{
          vertical: 'top',
          horizontal: 'right',
        }}
        PaperProps={{
          sx: {
            mt: 1.5,
            minWidth: 200,
            boxShadow: theme.shadows[5],
            borderRadius: 2,
          },
        }}
      >
        <Box sx={{ px: 2, py: 1 }}>
          <Typography variant="caption" color="text.secondary">
            {t('profileButton.loggedInAs')}
          </Typography>
          <Typography variant="subtitle2">
            {user?.username || t('profileButton.user')}
          </Typography>
        </Box>
        <Divider />
        <MenuItem
          onClick={() => {
            navigate('/profile');
            handleMenuClose();
          }}
        >
          {t('profileButton.profile.title')}
        </MenuItem>
        <MenuItem
          onClick={() => {
            navigate('/settings');
            handleMenuClose();
          }}
        >
          {t('profileButton.settings.title')}
        </MenuItem>
        <Divider />
        <MenuItem onClick={handleLogOut}>{t('profileButton.logout')}</MenuItem>
      </Menu>
    </>
  );
};

export default ProfileButton;
