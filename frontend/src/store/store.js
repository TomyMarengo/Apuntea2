import { configureStore } from '@reduxjs/toolkit';
import { apiSlice } from './slices/apiSlice.js';
import authReducer from './slices/authSlice.js';

// Function to save auth state to localStorage
function saveAuthStateToLocalStorage(state) {
  try {
    const serializedState = JSON.stringify(state);
    localStorage.setItem('authState', serializedState);
  } catch (e) {
    console.warn(e);
  }
}

// Function to load auth state from localStorage
function loadAuthStateFromLocalStorage() {
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
const localStorageMiddleware = (store) => (next) => (action) => {
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
    getDefaultMiddleware().concat(apiSlice.middleware).concat(localStorageMiddleware),
  devTools: true, //TODO: remove this in production
  preloadedState: {
    auth: preloadedAuthState, // Provide preloadedState only for auth slice
  },
});

export default store;