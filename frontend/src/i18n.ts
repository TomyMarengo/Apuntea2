// src/i18n.ts

import i18n from 'i18next';
import { initReactI18next } from 'react-i18next';
import HttpBackend from 'i18next-http-backend';
import LanguageDetector from 'i18next-browser-languagedetector';
import namespaces from './namespaces.json';

const namespaceList: string[] = namespaces as string[];

i18n
  .use(HttpBackend)
  .use(LanguageDetector)
  .use(initReactI18next)
  .init({
    fallbackLng: ['en'],
    debug: true,

    ns: namespaceList,
    defaultNS: 'common',

    interpolation: {
      escapeValue: false,
    },

    backend: {
      loadPath: 'locales/{{lng}}/{{ns}}.json',
    },

    react: {
      useSuspense: true,
    },
  });

export default i18n;
