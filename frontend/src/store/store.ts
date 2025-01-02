// src/store/store.ts

import { configureStore } from '@reduxjs/toolkit';

import { apiSlice } from './slices/apiSlice';
import authReducer from './slices/authSlice';
import languageReducer from './slices/languageSlice';
import themeReducer from './slices/themeSlice';

function saveStateToLocalStorage(key: string, state: any) {
  try {
    const serializedState = JSON.stringify(state);
    localStorage.setItem(key, serializedState);
  } catch (e) {
    console.warn(`Failed to save ${key} to localStorage:`, e);
  }
}

function loadStateFromLocalStorage(key: string): any | undefined {
  try {
    const serializedState = localStorage.getItem(key);
    if (serializedState === null) return undefined;
    return JSON.parse(serializedState);
  } catch (e) {
    console.warn(`Failed to load ${key} from localStorage:`, e);
    return undefined;
  }
}

const createLocalStorageMiddleware =
  (key: string) => (store: any) => (next: any) => (action: any) => {
    const result = next(action);
    const state = store.getState()[key];
    saveStateToLocalStorage(key, state);
    return result;
  };

const preloadedLanguageState = loadStateFromLocalStorage('language');
const preloadedThemeState = loadStateFromLocalStorage('theme');
const preloadedAuthState = loadStateFromLocalStorage('auth');

const store = configureStore({
  reducer: {
    [apiSlice.reducerPath]: apiSlice.reducer,
    auth: authReducer,
    language: languageReducer,
    theme: themeReducer,
  },
  middleware: (getDefaultMiddleware) =>
    getDefaultMiddleware({
      serializableCheck: false,
    })
      .concat(apiSlice.middleware)
      .concat(createLocalStorageMiddleware('auth'))
      .concat(createLocalStorageMiddleware('language'))
      .concat(createLocalStorageMiddleware('theme')),
  devTools: true,
  preloadedState: {
    auth: preloadedAuthState,
    language: preloadedLanguageState,
    theme: preloadedThemeState,
  },
});

export default store;

export type RootState = ReturnType<typeof store.getState>;
export type AppDispatch = typeof store.dispatch;
