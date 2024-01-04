import type { Config } from "tailwindcss";

const config: Config = {
  content: [
    "./pages/**/*.{js,ts,jsx,tsx,mdx}",
    "./components/**/*.{js,ts,jsx,tsx,mdx}",
    "./app/**/*.{js,ts,jsx,tsx,mdx}",
    "./stories/**/*.{js,ts,jsx,tsx}",
  ],
  theme: {
    extend: {
      backgroundImage: {
        "gradient-radial": "radial-gradient(var(--tw-gradient-stops))",
        "gradient-conic":
          "conic-gradient(from 180deg at 50% 50%, var(--tw-gradient-stops))",
      },
      colors: {
        primary: "#ef7765",
        secondary: "#fdc286",
        "dark-primary": "#cc6252",
        "dark-secondary": "#fcaa6f",
        "text": "#5a5044",
        "bg": "#fefefefe",
        "dark-text": "#302a24",
        "dark-bg": "#f9f1e7"
      },
    },
  },
  plugins: [],
};
export default config;
