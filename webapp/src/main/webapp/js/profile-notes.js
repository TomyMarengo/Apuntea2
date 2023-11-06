// Selecciona las pestañas y el contenido de las pestañas
const tabLinks = document.querySelectorAll('.nav-link');
const tabContents = document.querySelectorAll('.tab-pane');

// Agrega un evento de clic a cada pestaña
tabLinks.forEach(function(tabLink, index) {
    tabLink.addEventListener('click', function(event) {
        // Previene el comportamiento predeterminado del enlace
        event.preventDefault();

        // Oculta todos los contenidos de las pestañas
        tabContents.forEach(function(tabContent) {
            tabContent.classList.remove('show', 'active');
        });

        // Muestra el contenido de la pestaña correspondiente
        tabContents[index].classList.add('show', 'active');

        // Quita la clase activa de todas las pestañas
        tabLinks.forEach(function(link) {
            link.classList.remove('active');
        });

        // Agrega la clase activa a la pestaña clicada
        tabLink.classList.add('active');
    });
});

const rootDirs = document.querySelectorAll('.root-dir');
const rootDirLists = document.querySelectorAll('.root-dir-list');

rootDirs.forEach(function(rootDir, index) {
    rootDir.addEventListener('click', function(event) {
        event.preventDefault();

        rootDirLists.forEach(function (tabContent) {
            tabContent.classList.remove('show', 'active');
        });

        rootDirLists[index].classList.add('show', 'active');

        rootDirs.forEach(function (link) {
            link.classList.remove('active');
        });

        rootDirs[index].classList.add('active');
    });
});

// TODO: Change active between favorites and my notes
const favoriteDirs = document.querySelectorAll('.favorite-dir');
const favoriteDirLists = document.querySelectorAll('.favorite-dir-list');