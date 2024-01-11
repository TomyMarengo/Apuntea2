import { configureStore } from '@reduxjs/toolkit';
import { apiSlice } from './slices/apiSlice.js';
import authReducer from './slices/authSlice.js';

/* // Function to save state to localStorage
function saveToLocalStorage(state) {
  try {
    const serializedState = JSON.stringify(state);
    localStorage.setItem('state', serializedState);
  } catch (e) {
    console.warn(e);
  }
}

// Function to load state from localStorage
function loadFromLocalStorage() {
  try {
    const serializedState = localStorage.getItem('state');
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
  saveToLocalStorage(store.getState()); //TODO: remove token before saving to localStorage
  return result;
};

const preloadedState = loadFromLocalStorage(); */

const store = configureStore({
  reducer: {
    [apiSlice.reducerPath]: apiSlice.reducer,
    auth: authReducer,
  },
  /* middleware: (getDefaultMiddleware) => getDefaultMiddleware().concat(apiSlice.middleware).concat(localStorageMiddleware), */
  middleware: (getDefaultMiddleware) => getDefaultMiddleware().concat(apiSlice.middleware),
  devTools: true, //TODO: remove this in production
  /* preloadedState, */
});

export default store;
