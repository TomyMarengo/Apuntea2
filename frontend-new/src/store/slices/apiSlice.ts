// src/store/slices/apiSlice.ts

import {
  createApi,
  fetchBaseQuery,
  FetchBaseQueryError,
  BaseQueryFn,
  FetchArgs,
} from '@reduxjs/toolkit/query/react';
import { setCredentials, logOut, invalidateToken } from './authSlice';
import { decode } from '../../utils/helpers';

const baseUrl = import.meta.env.VITE_API_URL as string;

const baseQuery = fetchBaseQuery({
  baseUrl,
  credentials: 'include',
  prepareHeaders: (headers, { getState }) => {
    const state = getState() as any; // or RootState if typed
    if (!headers.has('Authorization')) {
      const token = state.auth?.token;
      const refreshToken = state.auth?.refreshToken;
      if (token || refreshToken) {
        headers.set('Authorization', 'Bearer ' + (token?.raw || refreshToken));
      }
    }
    return headers;
  },
});

const baseQueryWithReauth: BaseQueryFn<
  string | FetchArgs,
  unknown,
  FetchBaseQueryError
> = async (args, api, extraOptions) => {
  let result = await baseQuery(args, api, extraOptions);
  const { dispatch, getState } = api;
  const state = getState() as any;

  if (result?.error?.status === 401 && state.auth?.user) {
    if (
      args &&
      typeof args === 'object' &&
      'headers' in args &&
      args.headers &&
      args.headers['Content-Type'] ===
        'application/vnd.apuntea.user-update-v1.0+json'
    ) {
      throw new Error('InvalidPassword');
    }
    dispatch(invalidateToken());

    const data = await baseQuery(state.auth.user.selfUrl, api, extraOptions);
    let tokenStr = data.meta?.response?.headers
      .get('Access-Token')
      ?.split(' ')[1];
    let token = decode(tokenStr);
    const refreshToken = data.meta?.response?.headers
      .get('Refresh-Token')
      ?.split(' ')[1];
    if (token && refreshToken) {
      dispatch(setCredentials({ token, refreshToken }));
      result = await baseQuery(args, api, extraOptions);
    } else {
      dispatch(logOut());
    }
  }

  return result;
};

export const apiSlice = createApi({
  baseQuery: baseQueryWithReauth,
  endpoints: (_) => ({}),
  keepUnusedDataFor: 2,
  tagTypes: [
    'Institutions',
    'Careers',
    'Subjects',
    'Notes',
    'Directories',
    'Users',
    'ProfilePicture',
    'Reviews',
  ],
});
