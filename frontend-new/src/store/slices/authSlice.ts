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
        user?: User;
        token?: Token;
        refreshToken?: string;
      }>,
    ) => {
      const { user, token, refreshToken } = action.payload;
      if (user) state.user = user;
      if (token) state.token = token;
      if (refreshToken) state.refreshToken = refreshToken;
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

export const { setCredentials, logOut, invalidateToken } = authSlice.actions;
export default authSlice.reducer;

export const selectCurrentUser = (state: any) => state.auth?.user as User;
export const selectCurrentUserId = (state: any) => state.auth?.user?.id;
export const selectCurrentToken = (state: any) => state.auth?.token;
