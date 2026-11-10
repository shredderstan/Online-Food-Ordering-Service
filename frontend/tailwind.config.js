/** @type {import('tailwindcss').Config} */
export default {
  content: [
    "./index.html",
    "./src/**/*.{js,ts,jsx,tsx}",
  ],
  theme: {
    extend: {
      fontFamily: {
        serif: ['"Playfair Display"', 'Georgia', 'serif'],
        sans: ['"Plus Jakarta Sans"', 'system-ui', 'sans-serif'],
      },
      colors: {
        ivory: {
          50: '#FDFBF9',
          100: '#FAF8F5',
          200: '#F3EFEA',
          300: '#E8E1D7',
          400: '#DDD3C5',
        },
        champagne: {
          100: '#F7F3EB',
          200: '#EFE7D8',
          300: '#DECDB5',
          400: '#CBB291',
          500: '#B69771',
          600: '#9B7A54',
          700: '#7E603E',
        },
        espresso: {
          700: '#38332E',
          800: '#23201D',
          900: '#151312',
          950: '#0C0B0A',
        },
        terracotta: {
          500: '#C26543',
          600: '#A95131',
        }
      },
      boxShadow: {
        'chic': '0 10px 40px -10px rgba(21, 19, 18, 0.08)',
        'chic-hover': '0 20px 50px -15px rgba(21, 19, 18, 0.14)',
        'chic-dark': '0 15px 40px -10px rgba(0, 0, 0, 0.4)',
      }
    },
  },
  plugins: [],
}
