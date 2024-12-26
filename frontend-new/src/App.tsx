// src/App.tsx
import { useState } from 'react';
import { useSelector } from 'react-redux';
import { selectCurrentUser } from './store/slices/authSlice';
import Navbar from './components/Navbar/Navbar';
import AppRouter from './routes/AppRouter';
import { CssBaseline } from '@mui/material';
import { ThemeProvider } from '@mui/material/styles';
import { lightTheme, darkTheme } from './theme';

function App() {
  const [isDarkMode, setIsDarkMode] = useState(false);
  const user = useSelector(selectCurrentUser);

  const handleToggleDarkMode = () => setIsDarkMode((prev) => !prev);

  // If user != null, then isLoggedIn is true
  const isLoggedIn = !!user;

  return (
    <ThemeProvider theme={isDarkMode ? darkTheme : lightTheme}>
      <CssBaseline />
      <Navbar
        isDarkMode={isDarkMode}
        onToggleDarkMode={handleToggleDarkMode}
        isLoggedIn={isLoggedIn}
      />
      <AppRouter />
    </ThemeProvider>
  );
}

export default App;
