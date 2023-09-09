const darkModeToggle = document.getElementById('darkModeToggle');
const modeIcon = document.getElementById('darkModeIcon');

const savedTheme = localStorage.getItem('theme');
if (savedTheme) {
  document.documentElement.setAttribute('data-bs-theme', savedTheme);
  if (savedTheme === 'dark') {
    modeIcon.src = '../svg/moon.svg';
  } else {
    modeIcon.src = '../svg/sun.svg';
  }
}

darkModeToggle.addEventListener('click', () => {
  if (document.documentElement.getAttribute('data-bs-theme') === 'dark') {
    modeIcon.src = '../svg/moon.svg';
    document.documentElement.setAttribute('data-bs-theme', 'light');
    localStorage.setItem('theme', 'light'); // Guarda el modo seleccionado en localStorage
  } else {
    modeIcon.src = '../svg/sun.svg';
    document.documentElement.setAttribute('data-bs-theme', 'dark');
    localStorage.setItem('theme', 'dark'); // Guarda el modo seleccionado en localStorage
  }
});