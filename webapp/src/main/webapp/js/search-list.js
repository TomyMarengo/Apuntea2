const rows = document.querySelectorAll('.note-found');

rows.forEach(row => {
  row.addEventListener('dblclick', () => {
    // Acción de doble clic aquí (por ejemplo, redirigir a /notes/{noteId})
    const noteId = row.getAttribute('id');
    console.log(noteId);

    window.location.href = `/notes/${noteId.slice(0, -1)}`;
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
const horizontalListSection = document.getElementById('horizontal-list');
const boxListSection = document.getElementById('box-list');
const searchViewIcon = document.getElementById('search-view-icon');
const toggleViewBtn = document.getElementById('search-view-toggle');

// Obtener el valor del estado almacenado en el localStorage
const storedViewState = localStorage.getItem('viewState');
if (!storedViewState) {
  // Si no hay un valor almacenado, se establece el valor por defecto
  localStorage.setItem('viewState', 'box');
}

// Verificar y mostrar la vista según el valor almacenado
if (storedViewState === 'box') {
  boxListSection.style.display = 'block';
  searchViewIcon.src = '/svg/horizontal-list.svg';
} else {
  horizontalListSection.style.display = 'block';
  searchViewIcon.src = '/svg/box-list.svg';
}

// Función para cambiar la vista
function toggleView() {
  // Verificar si la vista actual es horizontal
  if (localStorage.getItem('viewState') === 'box') {
    // Cambiar a la vista de caja
    boxListSection.style.display = 'none';
    horizontalListSection.style.display = 'block';
    searchViewIcon.src = '/svg/box-list.svg';
    // Actualizar el valor almacenado en el localStorage
    localStorage.setItem('viewState', 'horizontal');
  } else {
    // Cambiar a la vista horizontal
    boxListSection.style.display = 'block';
    horizontalListSection.style.display = 'none';
    searchViewIcon.src = '/svg/horizontal-list.svg';
    // Actualizar el valor almacenado en el localStorage
    localStorage.setItem('viewState', 'box');
  }
}

// Asociar la función de cambio de vista al botón
toggleViewBtn.addEventListener('click', toggleView);

/**************************/
/* COPY DOWNLOAD BUTTONS */
/*************************/

const downloadButtons = document.querySelectorAll('.download-button');
const copyButtons = document.querySelectorAll('.copy-button');

// Función para descargar una nota
function downloadNote(noteId) {
  window.location.href = `../notes/${noteId}/download`;
}

// Función para copiar texto al portapapeles
function copyToClipboard(text) {
  const el = document.createElement('textarea');
  el.value = text;
  document.body.appendChild(el);
  el.select();
  document.execCommand('copy');
  document.body.removeChild(el);
}

// Agregar eventos de clic a los botones de descarga
downloadButtons.forEach(button => {
  button.addEventListener('click', () => {
    const noteId = button.getAttribute('id');
    downloadNote(noteId.slice(0, -2));
  });
});

// Agregar eventos de clic a los botones de copia
copyButtons.forEach(button => {
  button.addEventListener('click', () => {
    const noteId = button.getAttribute('id');
    const currentUrl = window.location.origin
    const path = window.location.pathname.split('/')[1];
    if(path === 'search'){
      copyToClipboard(`${currentUrl}/notes/${noteId.slice(0, -2)}`);
    }
    else {
      copyToClipboard(`${currentUrl}/${path}/notes/${noteId.slice(0, -2)}`);
    }
  });
});



