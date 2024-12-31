// src/components/Navbar/Navbar.tsx

import {
  AppBar,
  Toolbar,
  Typography,
  Box,
  Button,
  IconButton,
} from '@mui/material';
import { useTranslation } from 'react-i18next';
import DarkModeToggle from '../DarkModeToggle';
import LanguageToggle from '../LanguageToggle';
import ProfileButton from '../ProfileButton';
import { Link } from 'react-router-dom';
import SearchIcon from '@mui/icons-material/Search';

interface NavbarProps {
  isLoggedIn: boolean;
}

export default function Navbar({ isLoggedIn }: NavbarProps) {
  const { t } = useTranslation('navbar');
  return (
    <AppBar
      position="static"
      elevation={0}
      sx={{
        backgroundColor: 'background.paper',
        color: 'primary.main',
        boxShadow: 0,
        backgroundImage: 'none',
      }}
    >
      <Toolbar sx={{ display: 'flex', justifyContent: 'space-between' }}>
        {/* Left Side: App Name */}
        <Typography
          variant="h4"
          component={Link}
          to="/"
          sx={{ fontWeight: 'bold', textDecoration: 'none', color: 'inherit' }}
        >
          {t('appName') /* "Apuntea" */}
        </Typography>

        {/* Right Side */}
        <Box sx={{ display: 'flex', alignItems: 'center', gap: 2 }}>
          <IconButton component={Link} to="/search">
            <SearchIcon sx={{ color: 'primary.main' }} />
          </IconButton>
          <LanguageToggle />
          <DarkModeToggle />

          {isLoggedIn ? (
            <ProfileButton />
          ) : (
            <>
              <Button component={Link} to="/login" color="inherit">
                {t('login')}
              </Button>
              <Button component={Link} to="/register" color="inherit">
                {t('signup')}
              </Button>
            </>
          )}
        </Box>
      </Toolbar>
    </AppBar>
  );
}
