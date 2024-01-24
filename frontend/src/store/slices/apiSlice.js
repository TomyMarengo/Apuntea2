import { createApi, fetchBaseQuery } from '@reduxjs/toolkit/query/react';
import { setCredentials, logOut } from './authSlice';

const baseQuery = fetchBaseQuery({
  baseUrl: 'http://localhost:8080/paw-2023b-12',
  credentials: 'include',
  prepareHeaders: (headers, { getState }) => {
    const token = getState().auth.token;
    if (token) {
      headers.set('Authorization', `Bearer ${token.raw}`);
    }
    return headers;
  },
});

const baseQueryWithReauth = async (args, api, extraOptions) => {
  let result = await baseQuery(args, api, extraOptions);

  if (result?.error?.status === 401) {
    console.log('sending refresh token');
    // send refresh token to get new access token
    api.dispatch(setCredentials({ refreshToken: api.getState().auth.token.refreshToken }));
    // retry the original query with new access token
    const token = await baseQuery('/users?pageSize=4', api, { headers: { 'Authorization': api.getState().auth.refreshToken } });
    if (token?.data) {
      const user = api.getState().auth.user;
      // store the new token
      api.dispatch(setCredentials({ ...token.data, user }));
      // retry the original query with new access token
      result = await baseQuery(args, api, extraOptions);
    } else {
      api.dispatch(logOut());
    }
  }

  return result;
};

export const apiSlice = createApi({
  baseQuery: baseQueryWithReauth,
  endpoints: (builder) => ({}),
  keepUnusedDataFor: 2,
  tagTypes: ['Institutions', 'Careers', 'Subjects', 'Notes', 'Directories', 'Users', 'ProfilePicture'],
});
