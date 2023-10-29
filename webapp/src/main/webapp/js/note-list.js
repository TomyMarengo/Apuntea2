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

// Elements
const rows = document.querySelectorAll('.note-found');
// Content of the elements
const content = []

for (let i = 0; i < rows.length ; i++) {
    const id = rows[i].getAttribute('id').split('.', 1)[0];
    const category = rows[i].getAttribute('data-category');
    let noteName = rows[i].querySelector('.note-name').textContent.trim();
    noteName = noteName.replace(/_/g, '');
    const visible = rows[i].getAttribute('data-visible');
    let item = { id: id, category: category, name: noteName, visible: visible };

    if (category === 'directory') {
        item = {...item, iconColor: rows[i].querySelector('.folder-icon').getAttribute('data-color')};
    }

    content.push(item);
}

rows.forEach((row, index) => {
    row.addEventListener('dblclick', () => {
        const {id, category } = content[content.findIndex(item => item.id === row.getAttribute('id').split('.', 1)[0])];
        window.location.href = `${baseUrl}/${category === 'directory' ? 'directory' : 'notes'}/${id}`;
    });
});

let lastClickedRow = -1;
const selectedRowIds = new Set();
const selectedButtons = document.getElementById('selectedButtons');
const selectedCount = document.getElementById('selectedCount');

function updateSelectedState() {
    if (selectedRowIds.size > 0) {
        selectedButtons.style.display = 'flex'; // Mostrar el botón si hay filas seleccionadas
        downloadSelectedButton.style.display = 'flex';
        selectedCount.textContent = selectedRowIds.size.toString(); // Actualizar el número de filas seleccionadas
    } else {
        selectedButtons.style.display = 'none'; // Ocultar el botón si no hay filas seleccionadas
    }

    let entries = Array.from(selectedRowIds);
    for (let i = 0; i < rows.length; i++) {

        const index = entries.findIndex(itemId => itemId === content[i].id);

        if (index >= 0) {
            const noteHorizontal = document.getElementById(`${entries[index]}.1`);
            const noteBox = document.getElementById(`${entries[index]}.2`);

            const checkbox = noteHorizontal.querySelector('.select-checkbox');
            checkbox.checked = true;
            noteHorizontal.classList.add('active-note-found');

            const checkbox2 = noteBox.querySelector('.select-checkbox');
            checkbox2.checked = true;
            noteBox.classList.add('active-note-found');

            if (noteHorizontal.getAttribute('data-category') === 'directory') {
                downloadSelectedButton.style.display = 'none';
            }
        } else {
            const noteHorizontal = document.getElementById(`${content[i].id}.1`);
            const noteBox = document.getElementById(`${content[i].id}.2`);

            const checkbox = noteHorizontal.querySelector('.select-checkbox');
            checkbox.checked = false;
            noteHorizontal.classList.remove('active-note-found');

            const checkbox2 = noteBox.querySelector('.select-checkbox');
            checkbox2.checked = false;
            noteBox.classList.remove('active-note-found');
        }

    }
}

for (let i = 0; i < rows.length; i++) {
    rows[i].addEventListener('click', (event) => {

        const id = content[i].id;

        if (event.shiftKey && lastClickedRow >= 0) {
            let startIndex = lastClickedRow;
            let endIndex = i;

            const [minIndex, maxIndex] = [startIndex, endIndex].sort((a, b) => a - b);

            for (let j = minIndex; j <= maxIndex; j++) {
                selectedRowIds.add(content[j].id);
            }
        }
        else if (event.ctrlKey || (event.metaKey && navigator.appVersion.indexOf("Mac") !== -1) ) {
            if (selectedRowIds.has(id)) {
                selectedRowIds.delete(id);
            } else {
                selectedRowIds.add(id);
            }
        }
        else {
            let toggle = false;
            rows.forEach((otherRow, j) => {
                if (content[j].id !== id) {
                    selectedRowIds.delete(content[j].id);
                } else {
                    if (!toggle) {
                        toggle = true

                        if (!selectedRowIds.has(id)) {
                            selectedRowIds.add(id);
                            lastClickedRow = i;
                        } else {
                            selectedRowIds.delete(id);
                            lastClickedRow = -1;
                        }
                    }
                }
            });
        }
        updateSelectedState();
    });
}


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
const searchViewToggle = document.getElementById('searchViewToggle');


// Verificar y mostrar la vista según el valor almacenado
if (storedViewState === 'box') {
    boxListSection.style.display = 'block';
    searchViewIcon.src = `${baseUrl}/svg/horizontal-list.svg`;
    const attr = searchViewToggle.getAttribute('data-horizontal');
    searchViewToggle.setAttribute('data-bs-title', attr);
} else {
    horizontalListSection.style.display = 'block';
    searchViewIcon.src = `${baseUrl}/svg/box-list.svg`;
    const attr = searchViewToggle.getAttribute('data-box');
    searchViewToggle.setAttribute('data-bs-title', attr);
}

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
    if (localStorage.getItem('viewState') === 'box') {
        const attr = searchViewToggle.getAttribute('data-horizontal');
        searchViewToggle.setAttribute('data-bs-title', attr);
    } else {
        const attr = searchViewToggle.getAttribute('data-box');
        searchViewToggle.setAttribute('data-bs-title', attr);
    }
    // delete element with tooltip class
    const tooltip = document.querySelector('.tooltip');
    if (tooltip)
        tooltip.remove();
    new bootstrap.Tooltip(searchViewToggle);
}

// Asociar la función de cambio de vista al botón
searchViewToggle.addEventListener('click', toggleView);


/*************/
/* PAGE SIZE */
/*************/

const pageSizes = [12, 18, 24];
function changePageSize(pageSize) {
    /* Get searchForm, change pageSize to next element of the array and submit the form */
    const searchForm = document.getElementById('searchForm');
    const index = pageSizes.indexOf(pageSize);
    const newPageSize = pageSizes[(index + 1) % pageSizes.length];
    searchForm.querySelector('input[name="pageSize"]').value = newPageSize;
    searchForm.submit();
}