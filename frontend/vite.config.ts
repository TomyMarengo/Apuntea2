// @ts-nocheck
import react from '@vitejs/plugin-react-swc';
import { defineConfig } from 'vite';

// https://vite.dev/config/
export default defineConfig({
  plugins: [react()],
  base: '/paw-2023b-12',
  test: {
    environment: "jsdom",
    setupFiles: ["./src/__tests__/setup/setup.js"]
  }
});
