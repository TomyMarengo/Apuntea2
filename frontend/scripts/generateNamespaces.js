// scripts/generateNamespaces.js

import fs from 'fs';
import path from 'path';
import { fileURLToPath } from 'url';

const __filename = fileURLToPath(import.meta.url);
const __dirname = path.dirname(__filename);

const localesPath = path.join(__dirname, '../public/locales/en');

const files = fs.readdirSync(localesPath);

const namespaces = files
  .filter(file => file.endsWith('.json'))
  .map(file => path.basename(file, '.json'));

const namespacesArray = JSON.stringify(namespaces, null, 2);

const outputPath = path.join(__dirname, '../src/namespaces.json');

fs.writeFileSync(outputPath, namespacesArray);

console.log('Namespaces list generated successfully.');
