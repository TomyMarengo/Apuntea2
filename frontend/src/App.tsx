// src/App.tsx

import { useEffect, useMemo } from 'react';
import { useSelector } from 'react-redux';
import { selectCurrentUser } from './store/slices/authSlice';
import Navbar from './components/Navbar/Navbar';
import MiniSidebar from './components/Navbar/MiniSidebar';
import AppRouter from './routes/AppRouter';
import { CssBaseline, Box, GlobalStyles } from '@mui/material';
import { ThemeProvider } from '@mui/material/styles';
import { lightTheme, darkTheme } from './theme';
import CreateNoteFab from './components/CreateNoteFab';
import { Locale } from './types';
import { useTranslation } from 'react-i18next';
import { RootState } from './store/store';

function App() {
  const { i18n } = useTranslation();
  const user = useSelector(selectCurrentUser);
  const token = useSelector((state: RootState) => state.auth.token);
  const isDarkMode = useSelector((state: RootState) => state.theme.isDarkMode);
  const currentLocale = useSelector(
    (state: RootState) => state.language.locale,
  );

  // Determine the locale based on priority
  const locale = useMemo(() => {
    return (
      currentLocale ||
      user?.locale ||
      (navigator.language.split('-')[0] as Locale) ||
      'en'
    );
  }, [currentLocale, user]);

  // Synchronize the Redux locale with the user locale if available
  useEffect(() => {
    i18n.changeLanguage(locale);
  }, [locale]);

  const isLoggedIn = !!user;
  const isAdmin = token?.payload?.authorities.includes('ROLE_ADMIN');
  const theme = isDarkMode ? darkTheme : lightTheme;

  return (
    <ThemeProvider theme={theme}>
      <CssBaseline />
      <GlobalStyles
        styles={{
          ':root': {
            ...theme.custom,
          },
        }}
      />
      <Box
        sx={{
          display: 'grid',
          gridTemplateColumns: isLoggedIn ? '48px 1fr' : '1fr',
          gridTemplateRows: '64px 1fr',
          height: '100vh',
          gridTemplateAreas: isLoggedIn
            ? `
                "navbar navbar"
                "sidebar main"
              `
            : `
                "navbar"
                "main"
              `,
          background: `linear-gradient(
                180deg,
                var(--gradient-top) 0%,
                var(--gradient-mid) 42.19%,
                var(--gradient-bot) 99.99%
              ) no-repeat fixed`,
          backgroundBlendMode: 'lighten',
        }}
      >
        {/* Navbar */}
        <Box sx={{ gridArea: 'navbar' }}>
          <Navbar isLoggedIn={isLoggedIn} />
        </Box>

        {/* MiniSidebar if logged in */}
        {isLoggedIn && (
          <Box sx={{ gridArea: 'sidebar' }}>
            <MiniSidebar isAdmin={isAdmin} />
          </Box>
        )}

        {/* Main Content */}
        <Box
          sx={{
            gridArea: 'main',
            overflow: 'auto',
            position: 'relative',
          }}
        >
          <AppRouter />
          {/* Create Note Floating Button (appears on all pages) */}
          <CreateNoteFab />
        </Box>
      </Box>
    </ThemeProvider>
  );
}

export default App;
