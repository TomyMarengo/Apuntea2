import { createSlice } from '@reduxjs/toolkit';

const authSlice = createSlice({
  name: 'auth',
  initialState: { user: null, token: null },
  reducers: {
    setCredentials: (state, action) => {
      const { user, token, refreshToken } = action.payload;
      if (user) state.user = user
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
    }
  }
});

export const { setCredentials, logOut, invalidateToken } = authSlice.actions;

export default authSlice.reducer;

export const selectCurrentUser = (state) => state.auth?.user;
export const selectCurrentUserId = (state) => state.auth?.user?.id;
export const selectCurrentToken = (state) => state.auth?.token;
