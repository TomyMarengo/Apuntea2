/** @type {import('tailwindcss').Config} */
// Manually define the colors object with opacity variations
const colors = {
  "light-pri": "var(--light-pri)",
  "pri": "var(--pri)",
  "sec": "var(--sec)",
  "dark-pri": "var(--dark-pri)",
  "dark-sec": "var(--dark-sec)",
  "text": "var(--text)",
  "dark-text": "var(--dark-text)",
  "bg": "var(--bg)",
  "dark-bg": "var(--dark-bg)",
};

const opacityVariations = [10, 20, 50];

const coloredVariations = {};

Object.keys(colors).forEach(colorKey => {
  opacityVariations.forEach(opacity => {
    coloredVariations[`${colorKey}/${opacity}`] = `rgba(var(--${colorKey}-rgb), 0.${opacity})`;
  });
});

export default {
  content: [
    "./index.html",
    "./src/**/*.{js,ts,jsx,tsx}",
  ],
  darkMode: "class",
  theme: {
    extend: {
      colors: {
        ...colors,
        ...coloredVariations,
      },
    },
  },
  plugins: [],
}
