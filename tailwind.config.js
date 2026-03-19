/** @type {import('tailwindcss').Config} */
module.exports = {
  // NOTE: Update this to include the paths to all files that contain Nativewind classes.
  content: ["./app/**/*.{js,jsx,ts,tsx}", "./components/**/*.{js,jsx,ts,tsx}"],
  presets: [require("nativewind/preset")],
  theme: {
    extend: {
      colors:{
        "primary": "#39C6A6",
        "secondary": "#399FC6",
        "accent": "#C63959",
        light:{
          text: "#ffffff",
        },
        dark:{
          text: "#000000",
        }
      }
    },
  },
  plugins: [],
}