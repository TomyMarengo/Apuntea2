// src/store/slices/authSlice.ts

import { createSlice, PayloadAction } from '@reduxjs/toolkit';
import { User, Token } from '../../types';

interface AuthState {
  user: User | null;
  token: Token | null;
  refreshToken?: string | null;
}

const initialState: AuthState = {
  user: null,
  token: null,
  refreshToken: null,
};

const authSlice = createSlice({
  name: 'auth',
  initialState,
  reducers: {
    setCredentials: (
      state,
      action: PayloadAction<{
        token?: Token;
        refreshToken?: string;
      }>,
    ) => {
      const { token, refreshToken } = action.payload;
      if (token) state.token = token;
      if (refreshToken) state.refreshToken = refreshToken;
    },
    setCurrentUser(state, action: PayloadAction<User>) {
      state.user = action.payload;
    },
    logOut: (state) => {
      state.user = null;
      state.token = null;
      state.refreshToken = null;
    },
    invalidateToken: (state) => {
      state.token = null;
    },
  },
});

export const { setCredentials, setCurrentUser, logOut, invalidateToken } = authSlice.actions;
export default authSlice.reducer;

export const selectCurrentUser = (state: any) => state.auth?.user as User;
export const selectCurrentUserId = (state: any) => state.auth?.user?.id;
export const selectCurrentToken = (state: any) => state.auth?.token;
