const darkModeIcon = document.getElementById('darkModeIcon');
const darkModeToggle = document.getElementById('darkModeToggle');

const currentUrl = window.location.origin + '/'
const path = window.location.pathname.split('/')[1];
const baseUrl = currentUrl + path

// Obtener el valor del estado almacenado en el localStorage
const storedTheme = localStorage.getItem('theme');


// Verificar y mostrar la vista según el valor almacenado
if (storedTheme === 'light') {
    darkModeIcon.src = `${baseUrl}/svg/moon.svg`;
    document.documentElement.setAttribute('data-bs-theme', 'light');
}
else if (storedTheme === 'dark') {
    darkModeIcon.src = `${baseUrl}/svg/sun.svg`;
    document.documentElement.setAttribute('data-bs-theme', 'dark');
}


function toggleDarkMode() {
    if (localStorage.getItem('theme') === 'light') {
        darkModeIcon.src = `${baseUrl}/svg/sun.svg`;
        document.documentElement.setAttribute('data-bs-theme', 'dark');
        localStorage.setItem('theme', 'dark'); // Guarda el modo seleccionado en localStorage
    }
    else if (localStorage.getItem('theme') === 'dark') {
        darkModeIcon.src = `${baseUrl}/svg/moon.svg`;
        document.documentElement.setAttribute('data-bs-theme', 'light');
        localStorage.setItem('theme', 'light'); // Guarda el modo seleccionado en localStorage

    }
}

// Asociar la función de cambio de vista al botón
darkModeToggle.addEventListener('click', toggleDarkMode);