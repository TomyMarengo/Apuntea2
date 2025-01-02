// scripts/generateNamespaces.js

import fs from 'fs';
import path from 'path';
import { fileURLToPath } from 'url';

// Obtain __dirname and __filename in ES modules
const __filename = fileURLToPath(import.meta.url);
const __dirname = path.dirname(__filename);

/**
 * Extracts namespaces from a translations.json file.
 *
 * @param {string} translationsPath - The path to the translations.json file.
 * @returns {string[]} An array of namespace names.
 */
const extractNamespaces = (translationsPath) => {
  try {
    const data = fs.readFileSync(translationsPath, 'utf-8');
    const translations = JSON.parse(data);
    const namespaces = Object.keys(translations);
    return namespaces;
  } catch (error) {
    console.error(`Error reading or parsing ${translationsPath}:`, error);
    return [];
  }
};

// Define the path to translations.json for the English locale
// Change 'en' to 'es' or other locales as needed
const enTranslationsPath = path.join(__dirname, '../i18n/en/translations.json');

// Extract namespaces from the English translations.json
const namespaces = extractNamespaces(enTranslationsPath);

// Check if any namespaces were found
if (namespaces.length === 0) {
  console.error('No namespaces found in translations.json.');
  process.exit(1);
}

// Convert the namespaces array to a JSON-formatted string
const namespacesArray = JSON.stringify(namespaces, null, 2);

// Define the output path for namespaces.json
const outputPath = path.join(__dirname, '../namespaces.json');

// Write the namespaces array to namespaces.json
try {
  fs.writeFileSync(outputPath, namespacesArray, 'utf-8');
  console.log('Namespaces list successfully generated in namespaces.json.');
} catch (error) {
  console.error(`Error writing to ${outputPath}:`, error);
  process.exit(1);
}