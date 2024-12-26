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
    // text colors for "light mode"
    text: {
      primary: '#5A5044', // default text color
      secondary: '#302A24',
    },
    // custom "tertiary" color
    tertiary: {
      main: '#FEFEFE',
      dark: '#EBE3D8',
    },
    // You could also define background colors, etc.
    background: {
      default: '#FEFEFE', // e.g. white-ish background
      paper: '#FEFEFE',
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
      main: '#DFE44A',
      dark: '#B1B63B',
    },
    // text colors for "dark mode"
    text: {
      primary: '#FEFEFE',
      secondary: '#EBE3D8',
    },
    // custom "tertiary" color
    tertiary: {
      main: '#5A5044',
      dark: '#302A24',
    },
    background: {
      default: '#302A24', // or some darker background
      paper: '#302A24',
    },
  },
  typography: {
    // same or different overrides for dark mode
  },
});
