import { useEffect, useState } from 'react';
import { SunIcon, MoonIcon } from './Icons';

const DarkMode = () => {
  const [theme, setTheme] = useState(() => {
    const localTheme = window.localStorage.getItem('theme');
    return localTheme ? localTheme : 'light';
  });

  useEffect(() => {
    if (theme === 'light') {
      window.localStorage.setItem('theme', 'light');
      document.documentElement.classList.remove('dark');
    } else {
      window.localStorage.setItem('theme', 'dark');
      document.documentElement.classList.add('dark');
    }
  }, [theme]);

  const toggleTheme = () => {
    setTheme((prevTheme) => (prevTheme === 'light' ? 'dark' : 'light'));
  };

  return (
    <button className="icon-button" onClick={toggleTheme}>
      {theme === 'light' ? <MoonIcon className="icon-s fill-dark-pri" /> : <SunIcon className="icon-s fill-dark-pri" />}
    </button>
  );
};

export default DarkMode;
