// src/themes/valentines.ts
import { createTheme, alpha } from '@mui/material/styles';

const valentinesBaseTheme = createTheme({
  palette: {
    mode: 'light',
    primary: {
      main: '#FF9CBD',
      dark: '#FF63A7',
      contrastText: '#ffffff',
    },
    secondary: {
      main: '#FFC1CC',
      dark: '#FFA8B8',
      contrastText: '#5A5044',
    },
    background: {
      default: '#FFEFF7',
      paper: '#FFF5FA',
    },
    text: {
      primary: '#5A5044',
      secondary: '#302A24',
    },
  },
  typography: {},
  custom: {
    '--gradient-top': '#FFF5FA',
    '--gradient-mid': '#FFF5FA',
    '--gradient-bot': '#FFC9DD',
  },
});

const extendThemeWithOverrides = (baseTheme: typeof valentinesBaseTheme) => {
  return createTheme(baseTheme, {
    components: {
      MuiCssBaseline: {
        styleOverrides: `
          body {
            background-image: linear-gradient(
              180deg,
              var(--gradient-top) 0%,
              var(--gradient-mid) 42.19%,
              var(--gradient-bot) 99.99%
            );
            background-repeat: no-repeat;
            background-attachment: scroll;
            overflow-x: hidden;
          }
        `,
      },
      MuiButton: {
        styleOverrides: {
          root: {
            position: 'relative',
            overflow: 'visible',
            transition: 'transform 0.3s',
            '&:hover': {
              transform: 'scale(0.96)',
            },
          },
          contained: {
            '&:hover': {
              backgroundColor: baseTheme.palette.primary.dark,
              color: baseTheme.palette.primary.contrastText,
            },
          },
          outlined: {
            '&:hover': {
              backgroundColor: baseTheme.palette.primary.main,
              color: baseTheme.palette.background.paper,
            },
          },
        },
      },
      MuiIconButton: {
        styleOverrides: {
          root: {
            position: 'relative',
            overflow: 'visible',
            transition: 'transform 0.3s',
            '&:hover': {
              transform: 'scale(0.96)',
              backgroundColor: alpha(baseTheme.palette.text.primary, 0.1),
            },
          },
        },
      },
      MuiSwitch: {
        styleOverrides: {
          root: {
            position: 'relative',
            overflow: 'visible',
            '&:hover': {
              transform: 'scale(0.96)',
            },
          },
        },
      },
    },
  });
};

export const valentinesTheme = extendThemeWithOverrides(valentinesBaseTheme);
