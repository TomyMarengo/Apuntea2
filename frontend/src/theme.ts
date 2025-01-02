// src/theme.ts

import { PaletteColorOptions } from '@mui/material';
import { createTheme } from '@mui/material/styles';

// Extender la interfaz de Palette para incluir 'tertiary'
declare module '@mui/material/styles' {
  interface Palette {
    tertiary: PaletteColorOptions;
  }
  interface PaletteOptions {
    tertiary?: PaletteColorOptions;
  }
}

export const lightTheme = createTheme({
  palette: {
    mode: 'light',
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
  },
  typography: {},
  custom: {
    '--gradient-top': '#F9F1E7',
    '--gradient-mid': '#F8EED8',
    '--gradient-bot': '#F7E4BA',
  },
});

export const darkTheme = createTheme({
  palette: {
    mode: 'dark',
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
  },
  typography: {},
  custom: {
    '--gradient-top': '#0A0B10',
    '--gradient-mid': '#0C0510',
    '--gradient-bot': '#101214',
  },
});

declare module '@mui/material/styles' {
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
