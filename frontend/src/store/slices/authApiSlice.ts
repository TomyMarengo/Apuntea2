// src/store/slices/authApiSlice.ts

import { QueryReturnValue } from '@reduxjs/toolkit/query';

import { apiSlice } from './apiSlice';
import {
  USER_COLLECTION_CONTENT_TYPE,
  USER_CONTENT_TYPE,
} from '../../contentTypes';

// Minimal credential interfaces
interface Credentials {
  email: string;
  password: string;
}

interface RegisterArgs {
  credentials: Credentials;
  userId?: string;
  url?: string;
}

interface LoginResponse {
  token?: any;
  refreshToken?: any;
  user?: any;
}

export const authApiSlice = apiSlice.injectEndpoints({
  endpoints: (builder) => ({
    login: builder.query<LoginResponse, Credentials>({
      query: (credentials) => ({
        url: `/users?pageSize=1`,
        headers: {
          Authorization: `Basic ${btoa(`${credentials.email}:${credentials.password}`)}`,
          Accept: USER_COLLECTION_CONTENT_TYPE,
        },
      }),
      transformResponse: async (
        response: any,
        meta: QueryReturnValue<any>,
      ): Promise<LoginResponse> => {
        const user = await response;
        console.log(user);
        const token = (meta as any).response?.headers
          ?.get('Access-Token')
          ?.split(' ')[1];
        const refreshToken = (meta as any).response?.headers
          ?.get('Refresh-Token')
          ?.split(' ')[1];
        return { token, refreshToken };
      },
    }),
    register: builder.query<LoginResponse, RegisterArgs>({
      query: ({ credentials, userId, url }) => ({
        url: url || `/users/${userId}`,
        headers: {
          Authorization: `Basic ${btoa(`${credentials.email}:${credentials.password}`)}`,
          Accept: USER_CONTENT_TYPE,
        },
      }),
      transformResponse: async (
        response: any,
        meta: QueryReturnValue<any>,
      ): Promise<LoginResponse> => {
        const user = response;
        const token = (meta as any).response?.headers
          ?.get('Access-Token')
          ?.split(' ')[1];
        const refreshToken = (meta as any).response?.headers
          ?.get('Refresh-Token')
          ?.split(' ')[1];
        return { user, token, refreshToken };
      },
    }),
  }),
});

export const { useLazyLoginQuery, useLazyRegisterQuery } = authApiSlice;
