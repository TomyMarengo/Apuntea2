// src/main.tsx

import { Suspense } from 'react';
import { StrictMode } from 'react';
import { createRoot } from 'react-dom/client';
import { Provider } from 'react-redux';
import { BrowserRouter } from 'react-router-dom';
import store from './store/store';
import './index.css';
import App from './App.tsx';
import './i18n'; // Inicializa i18n aquí
import { ToastContainer } from 'react-toastify';
import 'react-toastify/dist/ReactToastify.css';
import { HelmetProvider } from 'react-helmet-async';
import TranslationFallback from './components/TranslationFallback';

createRoot(document.getElementById('root')!).render(
  <StrictMode>
    <HelmetProvider>
      <Provider store={store}>
        <BrowserRouter basename="/paw-2023b-12">
          <Suspense fallback={<TranslationFallback />}>
            <App />
          </Suspense>
          <ToastContainer />
        </BrowserRouter>
      </Provider>
    </HelmetProvider>
  </StrictMode>,
);
