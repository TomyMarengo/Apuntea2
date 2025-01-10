import { configureStore } from '@reduxjs/toolkit';
import { expect } from 'vitest';
import {HttpResponse} from "msw";

export function setupApiStore(api: any) {
    const store = configureStore({
        reducer: {
            [api.reducerPath]: api.reducer,
        },
        middleware: (getDefaultMiddleware) =>
            getDefaultMiddleware().concat(api.middleware),
    });

    return store;
}

export function apiUrl(path:string){
    return import.meta.env.VITE_API_URL + path;
}

export function expectToBePagedContent(collection, totalCount, totalPages) {
    expect(collection).toEqual(expect.any(Array));
    expect(totalCount).toEqual(expect.any(Number));
    expect(totalPages).toEqual(expect.any(Number));
}

export const UNAUTHORIZED_RESPONSE = () => new HttpResponse(null, {status: 401});
export const NOT_FOUND_RESPONSE = () => new HttpResponse(null, {status: 404});
export const NOT_ACCEPTABLE_RESPONSE = () => new HttpResponse(null, {status: 406});

export const UNSUPPORTED_MEDIA_TYPE_RESPONSE = () => new HttpResponse(null, {status: 415});
export const CREATED_RESPONSE = () => new HttpResponse(null, {status: 201});
export const NO_CONTENT_RESPONSE = () => new HttpResponse(null, {status: 204});

