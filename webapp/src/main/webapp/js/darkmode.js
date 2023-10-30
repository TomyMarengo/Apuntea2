const darkModeIcon = document.getElementById('darkModeIcon');
const darkModeToggle = document.getElementById('darkModeToggle');

const currentUrl = window.location.origin + '/'
const path = window.location.pathname.split('/')[1];
const baseUrl = currentUrl + path

// Obtener el valor del estado almacenado en el localStorage
const storedTheme = localStorage.getItem('theme');
if (!storedTheme) {
    // Si no hay un valor almacenado, se establece el valor por defecto
    localStorage.setItem('theme', 'light');
}

// Verificar y mostrar la vista según el valor almacenado
if (storedTheme === 'light') {
    darkModeIcon.src = `${baseUrl}/svg/moon.svg`;
    document.documentElement.setAttribute('data-bs-theme', 'light');
    dontMoveGhost();
}
else if (storedTheme === 'dark') {
    darkModeIcon.src = `${baseUrl}/svg/pumpkin.svg`;
    document.documentElement.setAttribute('data-bs-theme', 'dark');
    dontMoveGhost();
}
else if (storedTheme === 'halloween') {
    darkModeIcon.src = `${baseUrl}/svg/sun.svg`;
    document.documentElement.setAttribute('data-bs-theme', 'halloween');
    moveGhost();
}

function toggleDarkMode() {
    if (localStorage.getItem('theme') === 'light') {
        darkModeIcon.src = `${baseUrl}/svg/pumpkin.svg`;
        document.documentElement.setAttribute('data-bs-theme', 'dark');
        localStorage.setItem('theme', 'dark'); // Guarda el modo seleccionado en localStorage
        dontMoveGhost();
    }
    else if (localStorage.getItem('theme') === 'dark') {
        darkModeIcon.src = `${baseUrl}/svg/sun.svg`;
        document.documentElement.setAttribute('data-bs-theme', 'halloween');
        localStorage.setItem('theme', 'halloween'); // Guarda el modo seleccionado en localStorage
        moveGhost();

    }
    else if (localStorage.getItem('theme') === 'halloween') {
        darkModeIcon.src = `${baseUrl}/svg/moon.svg`;
        document.documentElement.setAttribute('data-bs-theme', 'light');
        localStorage.setItem('theme', 'light'); // Guarda el modo seleccionado en localStorage
        dontMoveGhost();
    }
}

// Asociar la función de cambio de vista al botón
darkModeToggle.addEventListener('click', toggleDarkMode);


/*************/
/* HALLOWEEN */
/*************/

/* Create an event listener that the halloween div follows the user's mouse */
const ghost = document.querySelector('.ghost');
let targetX = 170, targetY = 30;
let currentX = 170, currentY = 30;
const easingFactor = 0.1;

function animate() {
    currentX += (targetX - currentX) * easingFactor;
    currentY += (targetY - currentY) * easingFactor;

    ghost.style.left = currentX + 'px';
    ghost.style.top = currentY + 'px';

    requestAnimationFrame(animate);
}

function moveGhost() {
    document.addEventListener('mousemove', (e) => {
        targetX = e.clientX;
        targetY = e.clientY;
        ghost.style.position = 'absolute'; // or 'fixed'
        ghost.style.transform = 'none';
    });

    requestAnimationFrame(animate);
}

function dontMoveGhost() {
    document.removeEventListener('mousemove', (e) => {
        targetX = e.clientX;
        targetY = e.clientY;
        ghost.style.position = 'absolute'; // or 'fixed'
        ghost.style.transform = 'none';
    });
}



