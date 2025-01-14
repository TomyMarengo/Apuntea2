import { configureStore } from '@reduxjs/toolkit';
import { HttpResponse } from 'msw';
import { expect } from 'vitest';

export function setupApiStore(apis: any) {
  const reducer = {};
  for (const api of apis) {
    reducer[api.reducerPath] = api.reducer;
  }
  const store = configureStore({
    reducer: reducer,
    middleware: (getDefaultMiddleware) => {
        return getDefaultMiddleware().concat(
            apis.map((api) => api.middleware),
        );
    }
  });

  return store;
}

export function apiUrl(path: string) {
  return import.meta.env.VITE_API_URL + path;
}

export function expectToBePagedContent(
  collection: Array<any>,
  totalCount: number,
  totalPages: number,
) {
  expect(collection).toEqual(expect.any(Array));
  expect(totalCount).toEqual(expect.any(Number));
  expect(totalPages).toEqual(expect.any(Number));
}

export const UNAUTHORIZED_RESPONSE = () =>
  new HttpResponse(JSON.stringify({ message: 'unauthorized' }), {
    status: 401,
  });
export const NOT_FOUND_RESPONSE = () =>
  new HttpResponse(JSON.stringify({ message: 'not found' }), { status: 404 });
export const NOT_ACCEPTABLE_RESPONSE = () =>
  new HttpResponse(JSON.stringify({ message: 'not acceptable media type' }), {
    status: 406,
  });

export const UNSUPPORTED_MEDIA_TYPE_RESPONSE = () =>
  new HttpResponse(JSON.stringify({ message: 'unsupported media type' }), {
    status: 415,
  });
export const CREATED_RESPONSE = () => new HttpResponse(null, { status: 201 });
export const NO_CONTENT_RESPONSE = () =>
  new HttpResponse(null, { status: 204 });
