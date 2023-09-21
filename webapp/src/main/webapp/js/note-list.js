/*
 This file contains the JavaScript code for the note list page.
    It is responsible for handling the user interaction with the page.
    Like clicking on a note, selecting a note, etc.
 */

/*************/
/* SELECTED  */
/*************/


if (!baseUrl) {
  const currentUrl = window.location.origin + '/'
  const path = window.location.pathname.split('/')[1];
  const baseUrl = currentUrl + path
}

const rows = document.querySelectorAll('.note-found');
console.log(rows)

rows.forEach(row => {
  row.addEventListener('dblclick', () => {
    // Acción de doble clic aquí (por ejemplo, redirigir a /notes/{noteId})
    const noteId = row.getAttribute('id');

    window.location.href = `./notes/${noteId.slice(0, -1)}`;
  });
});

let lastClickedRow = -1;
const selectedRowIds = new Set();
const selectedButtons = document.getElementById('selectedButtons');
const selectedCount = document.getElementById('selectedCount');

function updateSelectedButtonsState() {
  if (selectedRowIds.size > 0) {
    selectedButtons.style.display = 'flex'; // Mostrar el botón si hay filas seleccionadas
    selectedCount.textContent = selectedRowIds.size.toString(); // Actualizar el número de filas seleccionadas
  } else {
    selectedButtons.style.display = 'none'; // Ocultar el botón si no hay filas seleccionadas
  }
}

for (let i = 0; i < rows.length; i++) {
  rows[i].addEventListener('click', (event) => {
    if (i >= rows.length / 2)
      i -= rows.length / 2;

    if (event.shiftKey && lastClickedRow >= 0) {
      let startIndex = lastClickedRow;
      let endIndex = i;

      if (startIndex >= rows.length / 2)
        startIndex -= rows.length / 2;

      const [minIndex, maxIndex] = [startIndex, endIndex].sort((a, b) => a - b);

      for (let j = minIndex; j <= maxIndex; j++) {
        const checkbox = rows[j].querySelector('.select-checkbox');
        checkbox.checked = true;
        rows[j].classList.add('active-note-found');

        const checkbox2 = rows[j*2].querySelector('.select-checkbox');
        checkbox2.checked = true;
        rows[j + rows.length / 2].classList.add('active-note-found');

        selectedRowIds.add(rows[j].getAttribute('id').slice(0, -1));
      }
    }
    else if (event.ctrlKey) {
        // Si se mantiene presionada la tecla Ctrl, alternar la selección de la fila

        rows[i].classList.toggle('active-note-found');
        const checkbox = rows[i].querySelector('.select-checkbox');
        checkbox.checked = !checkbox.checked;

        rows[i + rows.length / 2].classList.toggle('active-note-found');
        const checkbox2 = rows[i + rows.length / 2].querySelector('.select-checkbox');
        checkbox2.checked = !checkbox2.checked;

        if (checkbox.checked) {
            selectedRowIds.add(rows[i].getAttribute('id').slice(0, -1));
        }
        else {
            selectedRowIds.delete(rows[i].getAttribute('id').slice(0, -1));
        }
    }
    else {
      let toggle = false;
      rows.forEach(otherRow => {
        if (otherRow !== rows[i] && otherRow !== rows[i + rows.length / 2]) {
          const checkbox = otherRow.querySelector('.select-checkbox');
          checkbox.checked = false;
          otherRow.classList.remove('active-note-found');
          selectedRowIds.delete(otherRow.getAttribute('id').slice(0, -1));
        } else {
          if (!toggle) {
            toggle = true
            rows[i].classList.toggle('active-note-found');
            const checkbox = rows[i].querySelector('.select-checkbox');
            checkbox.checked = !checkbox.checked;

            rows[i + rows.length / 2].classList.toggle('active-note-found');
            const checkbox2 = rows[i + rows.length / 2].querySelector('.select-checkbox');
            checkbox2.checked = !checkbox2.checked;

            if (checkbox.checked) {
              selectedRowIds.add(rows[i].getAttribute('id').slice(0, -1));
              lastClickedRow = i;
            } else {
              selectedRowIds.delete(rows[i].getAttribute('id').slice(0, -1));
              lastClickedRow = -1;
            }
          }
        }
      });
    }
    updateSelectedButtonsState();
  });
}


const selectAllButton = document.getElementById('selectAllButton');
const deselectAllButton = document.getElementById('deselectAllButton');

// Función para seleccionar todas las filas
function selectAll() {
    for (let i = 0; i < rows.length / 2; i++) {
      const checkbox = rows[i].querySelector('.select-checkbox');
      checkbox.checked = true;
      rows[i].classList.add('active-note-found');

      const checkbox2 = rows[i + rows.length / 2].querySelector('.select-checkbox');
      checkbox2.checked = true;
      rows[i + rows.length / 2].classList.add('active-note-found');

      selectedRowIds.add(rows[i].getAttribute('id').slice(0, -1));
    }

  updateSelectedButtonsState();
}

function deselectAll() {
    for (let i = 0; i < rows.length / 2; i++) {
      const checkbox = rows[i].querySelector('.select-checkbox');
      checkbox.checked = false;
      rows[i].classList.remove('active-note-found');

      const checkbox2 = rows[i + rows.length / 2].querySelector('.select-checkbox');
      checkbox2.checked = false;
      rows[i + rows.length / 2].classList.remove('active-note-found');

      selectedRowIds.delete(rows[i].getAttribute('id').slice(0, -1));
    }

  updateSelectedButtonsState();
}

selectAllButton.addEventListener('click', selectAll);
deselectAllButton.addEventListener('click', deselectAll);


/*************/
/* VIEW MODE */
/*************/

// Obtener el valor del estado almacenado en el localStorage
const storedViewState = localStorage.getItem('viewState');
if (!storedViewState) {
  // Si no hay un valor almacenado, se establece el valor por defecto
  localStorage.setItem('viewState', 'box');
}

// Obtener las secciones por su ID
const horizontalListSection = document.getElementById('horizontalList');
const boxListSection = document.getElementById('boxList');
const searchViewIcon = document.getElementById('searchViewIcon');
const toggleViewBtn = document.getElementById('searchViewToggle');


// Verificar y mostrar la vista según el valor almacenado
if (storedViewState === 'box') {
  boxListSection.style.display = 'block';
  searchViewIcon.src = `${baseUrl}/svg/horizontal-list.svg`;
} else {
  horizontalListSection.style.display = 'block';
  searchViewIcon.src = `${baseUrl}/svg/box-list.svg`;
}


const dataTitleBox = toggleViewBtn.getAttribute('data-title-box');
const dataTitleList = toggleViewBtn.getAttribute('data-title-list');

// Función para cambiar la vista
changeTooltipText()
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
    changeTooltipText();
}

function changeTooltipText() {
  let ttInner = document.getElementsByClassName("tooltip-inner")[0];
  if(localStorage.getItem('viewState') === 'box'){
    toggleViewBtn.setAttribute('data-bs-title', dataTitleList);
    if(ttInner) {
      ttInner.innerHTML = dataTitleList;
    }
  }
  else{
    toggleViewBtn.setAttribute('data-bs-title', dataTitleBox);
    if(ttInner) {
      ttInner.innerHTML = dataTitleBox;
    }
  }
}

// Asociar la función de cambio de vista al botón
toggleViewBtn.addEventListener('click', toggleView);


