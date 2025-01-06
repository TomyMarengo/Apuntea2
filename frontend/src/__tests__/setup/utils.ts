import { configureStore } from '@reduxjs/toolkit';
import { expect } from 'vitest';

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
