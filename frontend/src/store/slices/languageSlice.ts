// src/store/slices/languageSlice.ts

import { createSlice, PayloadAction } from '@reduxjs/toolkit';

import { Locale } from '../../types';

interface LanguageState {
  locale: Locale;
}

const loadLanguageFromLocalStorage = (): Locale | undefined => {
  try {
    // const serializedState = localStorage.getItem('language');
    // if (serializedState === null)
    // return JSON.parse(serializedState);
    return navigator.language.split('-')[0] as Locale;
  } catch (e) {
    console.warn('Failed to load language from localStorage:', e);
    return undefined;
  }
};

const initialState: LanguageState = {
  locale: loadLanguageFromLocalStorage() || ('en' as Locale),
};

const languageSlice = createSlice({
  name: 'language',
  initialState,
  reducers: {
    setLocale(state, action: PayloadAction<Locale>) {
      state.locale = action.payload;
    },
  },
});

export const { setLocale } = languageSlice.actions;
export default languageSlice.reducer;
