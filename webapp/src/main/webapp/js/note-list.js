/*
 This file contains the JavaScript code for the note list page.
    It is responsible for handling the user interaction with the page.
    Like clicking on a note, selecting a note, etc.
 */


const rows = document.querySelectorAll('.note-found');
const selectedButtons = document.getElementById('selectedButtons');

if (!baseUrl) {
  const currentUrl = window.location.origin + '/'
  const path = window.location.pathname.split('/')[1];
  const baseUrl = currentUrl + path
}

rows.forEach(row => {
  row.addEventListener('dblclick', () => {
    // Acción de doble clic aquí (por ejemplo, redirigir a /notes/{noteId})
    const noteId = row.getAttribute('id');

    window.location.href = `./notes/${noteId.slice(0, -1)}`;
  });
});

let lastClickedRow = null;
const selectedRowIds = new Set();

function updateSelectedButtonsState() {
  console.log('selectedRowIds', selectedRowIds)
  if (selectedRowIds.size > 0) {
    selectedButtons.style.display = 'flex'; // Mostrar el botón si hay filas seleccionadas
  } else {
    selectedButtons.style.display = 'none'; // Ocultar el botón si no hay filas seleccionadas
  }
}

rows.forEach(row => {
  row.addEventListener('click', (event) => {
    if (event.shiftKey && lastClickedRow) {
      const startIndex = Array.from(rows).indexOf(lastClickedRow);
      const endIndex = Array.from(rows).indexOf(row);
      const [minIndex, maxIndex] = [startIndex, endIndex].sort((a, b) => a - b);

      for (let i = minIndex; i <= maxIndex; i++) {
        const checkbox = rows[i].querySelector('.select-checkbox');
        checkbox.checked = true;
        rows[i].classList.add('active-note-found');
        selectedRowIds.add(rows[i].getAttribute('id'));
      }
    }
    else if (event.ctrlKey) {
        // Si se mantiene presionada la tecla Ctrl, alternar la selección de la fila
        row.classList.toggle('active-note-found');
        const checkbox = row.querySelector('.select-checkbox');
        checkbox.checked = !checkbox.checked;
        if (checkbox.checked) {
            selectedRowIds.add(row.getAttribute('id'));
        }
        else {
            selectedRowIds.delete(row.getAttribute('id'));
        }
    }
    else {
      rows.forEach(otherRow => {
        if (otherRow !== row) {
          const checkbox = otherRow.querySelector('.select-checkbox');
          checkbox.checked = false;
          otherRow.classList.remove('active-note-found');

          selectedRowIds.delete(otherRow.getAttribute('id'));
        }
        else {
          row.classList.toggle('active-note-found');
          const checkbox = row.querySelector('.select-checkbox');
          checkbox.checked = !checkbox.checked;
          if (checkbox.checked) {
            selectedRowIds.add(row.getAttribute('id'));
            lastClickedRow = row;
          }
          else {
            selectedRowIds.delete(row.getAttribute('id'));
            lastClickedRow = null;
          }
        }
      });
    }
    updateSelectedButtonsState();
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



