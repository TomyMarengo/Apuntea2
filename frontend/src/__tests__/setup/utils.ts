import { configureStore } from '@reduxjs/toolkit';

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

