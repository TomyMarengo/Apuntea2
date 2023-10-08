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

for (let i = 0; i < rows.length / 2; i++) {
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
        // Acción de doble clic aquí (por ejemplo, redirigir a /notes/{noteId})
        if (index >= rows.length / 2)
            index -= rows.length / 2;

        const id = content[index].id;
        const category = content[index].category;
        // Open the URL in a new tab
        window.location.href = `${baseUrl}/${category === 'directory' ? 'directory' : 'notes'}/${id}`;
    });
});

let lastClickedRow = -1;
const selectedRowIds = new Set();
const selectedButtons = document.getElementById('selectedButtons');
const selectedCount = document.getElementById('selectedCount');

function updateSelectedButtonsState() {
    if (selectedRowIds.size > 0) {
        selectedButtons.style.display = 'flex'; // Mostrar el botón si hay filas seleccionadas
        downloadSelectedButton.style.display = 'flex';
        selectedCount.textContent = selectedRowIds.size.toString(); // Actualizar el número de filas seleccionadas
    } else {
        selectedButtons.style.display = 'none'; // Ocultar el botón si no hay filas seleccionadas
    }
    let entries = Array.from(selectedRowIds);
    for (let i = 0; i < selectedRowIds.size; i++) {
        if (content[content.findIndex(item => item.id === entries[i])].category === 'directory') {
            downloadSelectedButton.style.display = 'none';
            break;
        }
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

                const checkbox2 = rows[j * 2].querySelector('.select-checkbox');
                checkbox2.checked = true;
                rows[j + rows.length / 2].classList.add('active-note-found');

                selectedRowIds.add(content[j].id);
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
                selectedRowIds.add(content[i].id);
            } else {
                selectedRowIds.delete(content[i].id);
            }
        }
        else {
            let toggle = false;
            rows.forEach((otherRow, index) => {
                if (otherRow !== rows[i] && otherRow !== rows[i + rows.length / 2]) {
                    const checkbox = otherRow.querySelector('.select-checkbox');
                    checkbox.checked = false;
                    otherRow.classList.remove('active-note-found');

                    selectedRowIds.delete(content[index % (rows.length / 2)].id);
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
                            selectedRowIds.add(content[i].id);
                            lastClickedRow = i;
                        } else {
                            selectedRowIds.delete(content[i].id);
                            lastClickedRow = -1;
                        }
                    }
                }
            });
        }
        updateSelectedButtonsState();
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
    pageSize = pageSizes[(index + 1) % pageSizes.length];
    searchForm.querySelector('input[name="pageSize"]').value = pageSize;
    searchForm.submit();
}