// src/components/TranslationFallback.tsx

import React from 'react';
import { useTranslation } from 'react-i18next';

const TranslationFallback: React.FC = () => {
  const { t } = useTranslation('common');

  return <div>{t('loadingTranslations')}</div>;
};

export default TranslationFallback;
