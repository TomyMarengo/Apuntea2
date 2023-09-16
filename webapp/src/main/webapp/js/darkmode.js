const darkModeIcon = document.getElementById('darkModeIcon');
const darkModeToggle = document.getElementById('darkModeToggle');

// Obtener el valor del estado almacenado en el localStorage
const storedTheme = localStorage.getItem('theme');
if (!storedTheme) {
  // Si no hay un valor almacenado, se establece el valor por defecto
  localStorage.setItem('theme', 'light');
}

// Verificar y mostrar la vista según el valor almacenado
if (storedTheme === 'dark') {
  darkModeIcon.src = '/svg/sun.svg';
  document.documentElement.setAttribute('data-bs-theme', 'dark');
} else {
  darkModeIcon.src = '/svg/moon.svg';
  document.documentElement.setAttribute('data-bs-theme', 'light');
}

function toggleDarkMode() {
  if (localStorage.getItem('theme') === 'dark') {
    darkModeIcon.src = '/svg/sun.svg';
    document.documentElement.setAttribute('data-bs-theme', 'light');
    localStorage.setItem('theme', 'light'); // Guarda el modo seleccionado en localStorage
  } else {
    darkModeIcon.src = '/svg/sun.svg';
    document.documentElement.setAttribute('data-bs-theme', 'dark');
    localStorage.setItem('theme', 'dark'); // Guarda el modo seleccionado en localStorage
  }
}

// Asociar la función de cambio de vista al botón
darkModeToggle.addEventListener('click', toggleDarkMode);