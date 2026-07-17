/** @type {import('tailwindcss').Config} */
import {colours} from "./constants/colours";

module.exports = {
  // NOTE: Update this to include the paths to all files that contain Nativewind classes.
  content: ["./app/**/*.{js,jsx,ts,tsx}", "./components/**/*.{js,jsx,ts,tsx}"],
  presets: [require("nativewind/preset")],
  theme: {
    extend: {
      colors:{
        "primary": colours.primary,
        "secondary": colours.secondary,
        light:{
          text: colours.secondary,
        },
        dark:{
          text: colours.primary,
        }
      }
    },
  },
  plugins: [],
}