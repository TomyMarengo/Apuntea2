// src/store/slices/themeSlice.ts

import { createSlice, PayloadAction } from '@reduxjs/toolkit';

interface ThemeState {
  isDarkMode: boolean;
}

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
  },
});

export const { toggleDarkMode, setDarkMode } = themeSlice.actions;
export default themeSlice.reducer;
