// eslint.config.js (Flat Config)

import js from '@eslint/js';
import globals from 'globals';
import reactHooks from 'eslint-plugin-react-hooks';
import reactRefresh from 'eslint-plugin-react-refresh';

// TypeScript
import ts from '@typescript-eslint/eslint-plugin';
import tsParser from '@typescript-eslint/parser';

// Prettier
import prettierPlugin from 'eslint-plugin-prettier';
import { configs as prettierConfigs } from 'eslint-config-prettier';

export default [
  {
    // We want to lint TS, TSX, and optionally JS
    files: ['**/*.{ts,tsx,js}'],
    ignores: ['dist', 'node_modules'],
    languageOptions: {
      parser: tsParser,
      parserOptions: {
        // Link to whichever tsconfig you want for linting
        project: ['./tsconfig.app.json'],
        ecmaVersion: 'latest',
        sourceType: 'module',
      },
      globals: {
        ...globals.browser,
      },
    },
    plugins: {
      '@typescript-eslint': ts,
      'react-hooks': reactHooks,
      'react-refresh': reactRefresh,
      prettier: prettierPlugin,
    },
    rules: {
      // Base JS recommended
      ...js.configs.recommended.rules,
      // TS recommended
      ...ts.configs.recommended.rules,
      // React Hooks
      ...reactHooks.configs.recommended.rules,
      // React Refresh
      'react-refresh/only-export-components': [
        'warn',
        { allowConstantExport: true },
      ],

      // Prettier as an ESLint rule:
      'prettier/prettier': 'warn',
    },
  },

  // Integrate Prettier's recommended config
  // which disables ESLint rules that conflict with Prettier
  prettierConfigs.recommended,
];
