import { createSlice } from '@reduxjs/toolkit';

const authSlice = createSlice({
  name: 'auth',
  initialState: { user: null, institution: null, token: null },
  reducers: {
    setCredentials: (state, action) => {
      const { user, institution, token } = action.payload;
      state.user = user;
      state.institution = institution;
      state.token = token;
    },
    logOut: (state) => {
      state.user = null;
      state.institution = null;
      state.token = null;
    },
  },
});

export const { setCredentials, logOut } = authSlice.actions;

export default authSlice.reducer;

export const selectCurrentUser = (state) => state.auth.user;
export const selectCurrentInstitution = (state) => state.auth.institution;
export const selectCurrentToken = (state) => state.auth.token;
