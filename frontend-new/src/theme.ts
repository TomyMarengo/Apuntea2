// src/theme.ts

import { createTheme } from '@mui/material/styles';
import { PaletteColorOptions } from '@mui/material';

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
  typography: {
    // Optionally override typography to ensure
    // consistent text color usage, fonts, etc.
    // e.g., fontFamily: 'Roboto, sans-serif'
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
    tertiary: {
      main: '#5A5044',
      dark: '#302A24',
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
  typography: {
    // same or different overrides for dark mode
  },
});
