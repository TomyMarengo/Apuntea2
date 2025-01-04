// src/themes/theme.ts

import { PaletteColorOptions, createTheme, alpha } from '@mui/material/styles';

import { valentinesTheme } from './valentines';

declare module '@mui/material/styles' {
  interface Palette {
    tertiary: PaletteColorOptions;
  }
  interface PaletteOptions {
    tertiary?: PaletteColorOptions;
  }

  interface Theme {
    custom: {
      [key: string]: string;
    };
  }
  interface ThemeOptions {
    custom?: {
      [key: string]: string;
    };
  }
}

const createBaseTheme = (mode: 'light' | 'dark') => {
  return createTheme({
    palette: {
      mode,
      ...(mode === 'light'
        ? {
            primary: {
              main: '#EF7765',
              dark: '#CC6252',
            },
            secondary: {
              main: '#FDC286',
              dark: '#FCAA6F',
            },
            text: {
              primary: '#5A5044',
              secondary: '#302A24',
            },
            background: {
              default: '#FEFEFE',
              paper: '#F9F1E7',
            },
          }
        : {
            primary: {
              main: '#B3E8C3',
              dark: '#7FB58F',
            },
            secondary: {
              main: '#50C4EC',
              dark: '#2F9CBB',
            },
            text: {
              primary: '#FEFEFE',
              secondary: '#F9F1E7',
            },
            background: {
              default: '#454D55',
              paper: '#0A0B10',
            },
          }),
    },
    typography: {},
    custom: {
      ...(mode === 'light'
        ? {
            '--gradient-top': '#F9F1E7',
            '--gradient-mid': '#F8EED8',
            '--gradient-bot': '#F7E4BA',
          }
        : {
            '--gradient-top': '#0A0B10',
            '--gradient-mid': '#0C0510',
            '--gradient-bot': '#101214',
          }),
    },
  });
};

const lightBaseTheme = createBaseTheme('light');
const darkBaseTheme = createBaseTheme('dark');

const extendThemeWithOverrides = (
  baseTheme: ReturnType<typeof createBaseTheme>,
) => {
  return createTheme(baseTheme, {
    components: {
      MuiCssBaseline: {
        styleOverrides: {
          body: {
            backgroundImage: `linear-gradient(
              180deg,
              var(--gradient-top) 0%,
              var(--gradient-mid) 42.19%,
              var(--gradient-bot) 99.99%
            )`,
            backgroundRepeat: 'no-repeat',
            backgroundAttachment: 'scroll',
          },
        },
      },
      MuiButton: {
        styleOverrides: {
          root: {
            transition: 'background-color 0.3s, color 0.3s',
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
            transition: 'background-color 0.3s, color 0.3s',
            '&:hover': {
              backgroundColor: alpha(baseTheme.palette.text.primary, 0.1),
            },
          },
        },
      },
    },
  });
};

export const lightTheme = extendThemeWithOverrides(lightBaseTheme);
export const darkTheme = extendThemeWithOverrides(darkBaseTheme);
export { valentinesTheme };
