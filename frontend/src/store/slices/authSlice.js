import { createSlice } from '@reduxjs/toolkit';

const authSlice = createSlice({
  name: 'auth',
  initialState: { user: null, token: null },
  reducers: {
    setCredentials: (state, action) => {
      const { user, institution, career, token } = action.payload;
      const updatedUser = {
        institution: institution !== undefined ? institution : state.user.institution,
        career: career !== undefined ? career : state.user.career,
        ...user
      };
      state.user = updatedUser
      state.token = token;
    },
    logOut: (state) => {
      state.user = null;
      state.token = null;
    },
  }
});

export const { setCredentials, logOut } = authSlice.actions;

export default authSlice.reducer;

export const selectCurrentUser = (state) => state.auth.user;
export const selectCurrentUserId = (state) => state.auth.user.id;
export const selectCurrentToken = (state) => state.auth.token;
