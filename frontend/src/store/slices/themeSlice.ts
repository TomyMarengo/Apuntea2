// src/store/slices/themeSlice.ts

import { createSlice, PayloadAction } from '@reduxjs/toolkit';

interface ThemeState {
  isDarkMode: boolean;
  isSeasonalMode: boolean;
}

const loadSeasonalFromLocalStorage = (): boolean | undefined => {
  try {
    const serializedState = localStorage.getItem('seasonalMode');
    if (serializedState === null) return undefined;
    return JSON.parse(serializedState);
  } catch (e) {
    console.warn('Failed to load seasonal mode from localStorage:', e);
    return undefined;
  }
};

const loadThemeFromLocalStorage = (): boolean | undefined => {
  try {
    const serializedState = localStorage.getItem('theme');
    if (serializedState === null) return undefined;
    return JSON.parse(serializedState);
  } catch (e) {
    console.warn('Failed to load theme from localStorage:', e);
    return undefined;
  }
};

const initialState: ThemeState = {
  isDarkMode: loadThemeFromLocalStorage() || false,
  isSeasonalMode: loadSeasonalFromLocalStorage() || false,
};

const themeSlice = createSlice({
  name: 'theme',
  initialState,
  reducers: {
    toggleDarkMode(state) {
      state.isDarkMode = !state.isDarkMode;
    },
    setDarkMode(state, action: PayloadAction<boolean>) {
      state.isDarkMode = action.payload;
    },
    toggleSeasonalMode(state) {
      state.isSeasonalMode = !state.isSeasonalMode;
    },
    setSeasonalMode(state, action: PayloadAction<boolean>) {
      state.isSeasonalMode = action.payload;
    },
  },
});

export const {
  toggleDarkMode,
  setDarkMode,
  toggleSeasonalMode,
  setSeasonalMode,
} = themeSlice.actions;

export default themeSlice.reducer;
