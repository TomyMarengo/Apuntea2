// src/App.tsx

import { useState } from 'react';
import { useSelector } from 'react-redux';
import { selectCurrentUser } from './store/slices/authSlice';
import Navbar from './components/Navbar/Navbar';
import MiniSidebar from './components/Navbar/MiniSidebar';
import AppRouter from './routes/AppRouter';
import { CssBaseline, Box } from '@mui/material';
import { ThemeProvider } from '@mui/material/styles';
import { lightTheme, darkTheme } from './theme';
import { Locale } from './types';
import CreateNoteFab from './components/CreateNoteFab';
import { RootState } from './store/store';

function App() {
  const [isDarkMode, setIsDarkMode] = useState(false);
  const user = useSelector(selectCurrentUser);
  const token = useSelector((state: RootState) => state.auth.token);

  const handleToggleDarkMode = () => setIsDarkMode((prev) => !prev);

  const isLoggedIn = !!user;
  const isAdmin = token?.payload?.authorities.includes('ROLE_ADMIN');

  const locale = user?.locale || ('en' as Locale);

  return (
    <ThemeProvider theme={isDarkMode ? darkTheme : lightTheme}>
      <CssBaseline />
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
        }}
      >
        {/* Navbar */}
        <Box sx={{ gridArea: 'navbar' }}>
          <Navbar
            isDarkMode={isDarkMode}
            locale={locale}
            onToggleDarkMode={handleToggleDarkMode}
            isLoggedIn={isLoggedIn}
          />
        </Box>

        {/* MiniSidebar if logged in */}
        {isLoggedIn && (
          <Box sx={{ gridArea: 'sidebar' }}>
            <MiniSidebar isAdmin={isAdmin} />
          </Box>
        )}

        {/* Main Content */}
        <Box sx={{ gridArea: 'main', overflow: 'auto', position: 'relative' }}>
          <AppRouter />
          {/* Create Note Floating Button (appears on all pages) */}
          <CreateNoteFab />
        </Box>
      </Box>
    </ThemeProvider>
  );
}

export default App;
