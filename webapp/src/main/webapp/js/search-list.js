const rows = document.querySelectorAll('.note-found');

if (!baseUrl) {
  const currentUrl = window.location.origin + '/'
  const path = window.location.pathname.split('/')[1];
  const baseUrl = currentUrl + path
}

rows.forEach(row => {
  row.addEventListener('dblclick', () => {
    // Acción de doble clic aquí (por ejemplo, redirigir a /notes/{noteId})
    const noteId = row.getAttribute('id');
    console.log(noteId);

    window.location.href = `./notes/${noteId.slice(0, -1)}`;
  });
});

rows.forEach(row => {
  row.addEventListener('click', () => {
    // Desactivar todas las filas activas
    rows.forEach(otherRow => {
      if (otherRow !== row) {
        otherRow.classList.remove('active-note-found');
      }
    });

    // Alternar la clase active-row de la fila actual
    row.classList.toggle('active-note-found');
  });
});

/*************/
/* VIEW MODE */
/*************/

// Obtener las secciones por su ID
const horizontalListSection = document.getElementById('horizontalList');
const boxListSection = document.getElementById('boxList');
const searchViewIcon = document.getElementById('searchViewIcon');
const toggleViewBtn = document.getElementById('searchViewToggle');

// Obtener el valor del estado almacenado en el localStorage
const storedViewState = localStorage.getItem('viewState');
if (!storedViewState) {
  // Si no hay un valor almacenado, se establece el valor por defecto
  localStorage.setItem('viewState', 'box');
}

// Verificar y mostrar la vista según el valor almacenado
if (storedViewState === 'box') {
  boxListSection.style.display = 'block';
  searchViewIcon.src = `${baseUrl}/svg/horizontal-list.svg`;
} else {
  horizontalListSection.style.display = 'block';
  searchViewIcon.src = `${baseUrl}/svg/box-list.svg`;
}

// Función para cambiar la vista
function toggleView() {
  // Verificar si la vista actual es horizontal
  if (localStorage.getItem('viewState') === 'box') {
    // Cambiar a la vista de caja
    boxListSection.style.display = 'none';
    horizontalListSection.style.display = 'block';
    searchViewIcon.src = `${baseUrl}/svg/box-list.svg`;
    // Actualizar el valor almacenado en el localStorage
    localStorage.setItem('viewState', 'horizontal');
  } else {
    // Cambiar a la vista horizontal
    boxListSection.style.display = 'block';
    horizontalListSection.style.display = 'none';
    searchViewIcon.src = `${baseUrl}/svg/horizontal-list.svg`;
    // Actualizar el valor almacenado en el localStorage
    localStorage.setItem('viewState', 'box');
  }
}

// Asociar la función de cambio de vista al botón
toggleViewBtn.addEventListener('click', toggleView);



