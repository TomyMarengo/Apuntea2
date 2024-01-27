import { createApi, fetchBaseQuery } from '@reduxjs/toolkit/query/react';
import { setCredentials, logOut, invalidateToken } from './authSlice';
import { decode } from '../../functions/utils';

const getTokenUrl = '/users?pageSize=4';
const baseUrl = 'http://localhost:8080/paw-2023b-12/api'; //TODO mover a .env

const baseQuery = fetchBaseQuery({
  baseUrl,
  credentials: 'include',
  prepareHeaders: (headers, { getState }) => {
    const token = getState().auth.token;
    const refreshToken = getState().auth.refreshToken;
    if (token || refreshToken) {
      headers.set('Authorization', 'Bearer ' + (token?.raw || refreshToken));
    }
    return headers;
  },
});

const baseQueryWithReauth = async (args, api, extraOptions) => {
  let result = await baseQuery(args, api, extraOptions);

  if (result?.error?.status === 401) {
    api.dispatch(invalidateToken());

    const data = await baseQuery(getTokenUrl, api, extraOptions);
    let token = data.meta.response.headers.get('Access-Token').split(' ')[1];
    token = decode(token);
    const refreshToken = data.meta.response.headers.get('Refresh-Token').split(' ')[1];
    if (token && refreshToken) {
      // store the new token
      api.dispatch(setCredentials({ token, refreshToken }));
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
  // eslint-disable-next-line no-unused-vars
  endpoints: (builder) => ({}),
  keepUnusedDataFor: 2,
  tagTypes: ['Institutions', 'Careers', 'Subjects', 'Notes', 'Directories', 'Users', 'ProfilePicture', 'Reviews'],
});
