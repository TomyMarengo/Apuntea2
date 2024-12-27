import { useState } from 'react';
import { useSelector } from 'react-redux';
import { selectCurrentUser } from './store/slices/authSlice';
import Navbar from './components/Navbar/Navbar';
import Sidebar from './components/Navbar/Sidebar';
import AppRouter from './routes/AppRouter';
import { CssBaseline, Box } from '@mui/material';
import { ThemeProvider } from '@mui/material/styles';
import { lightTheme, darkTheme } from './theme';
import { Locale } from './types';

function App() {
  const [isDarkMode, setIsDarkMode] = useState(false);
  const user = useSelector(selectCurrentUser);

  const handleToggleDarkMode = () => setIsDarkMode((prev) => !prev);

  // If user != null, then isLoggedIn is true
  const isLoggedIn = !!user;

  const locale = user?.locale || ('en' as Locale);

  return (
    <ThemeProvider theme={isDarkMode ? darkTheme : lightTheme}>
      <CssBaseline />
      <Box
        sx={{
          display: 'grid',
          gridTemplateColumns: isLoggedIn ? '60px 1fr' : '1fr', // Sidebar width if logged in
          gridTemplateRows: '64px 1fr', // Navbar height and main content
          height: '100vh', // Full viewport height
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
        <Box sx={{ gridArea: 'navbar' }}>
          <Navbar
            isDarkMode={isDarkMode}
            locale={locale}
            onToggleDarkMode={handleToggleDarkMode}
            isLoggedIn={isLoggedIn}
          />
        </Box>
        {isLoggedIn && (
          <Box sx={{ gridArea: 'sidebar' }}>
            <Sidebar />
          </Box>
        )}
        <Box sx={{ gridArea: 'main', overflow: 'auto' }}>
          <AppRouter />
        </Box>
      </Box>
    </ThemeProvider>
  );
}

export default App;
