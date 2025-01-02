import i18n from 'i18next';
import { initReactI18next } from 'react-i18next';

import enTranslations from './i18n/en/translations.json';
import esTranslations from './i18n/es/translations.json';
import namespaces from './namespaces.json';

const namespaceList: string[] = namespaces as string[];

i18n.use(initReactI18next).init({
  resources: {
    en: enTranslations,
    es: esTranslations,
  },
  fallbackLng: 'en',
  debug: true,
  ns: namespaceList,
  defaultNS: 'common',
  interpolation: {
    escapeValue: false,
  },
  react: {
    useSuspense: true,
  },
});

export default i18n;
