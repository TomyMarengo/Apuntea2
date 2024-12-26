// src/store/store.ts

import { configureStore } from '@reduxjs/toolkit';
import { apiSlice } from './slices/apiSlice';
import authReducer from './slices/authSlice';

// Function to save auth state to localStorage
function saveAuthStateToLocalStorage(state: any) {
  try {
    const serializedState = JSON.stringify(state);
    localStorage.setItem('authState', serializedState);
  } catch (e) {
    console.warn(e);
  }
}

// Function to load auth state from localStorage
function loadAuthStateFromLocalStorage(): any | undefined {
  try {
    const serializedState = localStorage.getItem('authState');
    if (serializedState === null) return undefined;
    return JSON.parse(serializedState);
  } catch (e) {
    console.warn(e);
    return undefined;
  }
}

// Middleware function
const localStorageMiddleware = (store: any) => (next: any) => (action: any) => {
  const result = next(action);
  saveAuthStateToLocalStorage(store.getState().auth); // Only save auth state to localStorage
  return result;
};

const preloadedAuthState = loadAuthStateFromLocalStorage(); // Load only auth state from localStorage

const store = configureStore({
  reducer: {
    [apiSlice.reducerPath]: apiSlice.reducer,
    auth: authReducer,
  },
  middleware: (getDefaultMiddleware) =>
    getDefaultMiddleware()
      .concat(apiSlice.middleware)
      .concat(localStorageMiddleware),
  devTools: true,
  preloadedState: {
    auth: preloadedAuthState, // Provide preloadedState only for auth slice
  },
});

export default store;

export type RootState = ReturnType<typeof store.getState>;
export type AppDispatch = typeof store.dispatch;
