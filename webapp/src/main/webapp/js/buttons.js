/**************************/
/* COPY DOWNLOAD BUTTONS */
/*************************/


const copyButtons = document.querySelectorAll('.copy-button');

// Función para copiar texto al portapapeles
function copyToClipboard(text) {
    const el = document.createElement('textarea');
    el.value = text;
    document.body.appendChild(el);
    el.select();
    document.execCommand('copy');
    document.body.removeChild(el);
}

// Agregar eventos de clic a los botones de copia
copyButtons.forEach(button => {
    button.addEventListener('click', () => {
        const noteId = button.getAttribute('id');
        const [id, type] = noteId.slice(0, -1).split('.');
        copyToClipboard(`${baseUrl}/${type === 'directory' ? 'directory' : 'notes'}/${id}`);
    });
});

const downloadSelectedButton = document.getElementById('downloadSelectedButton');

if (downloadSelectedButton)
    downloadSelectedButton.addEventListener('click', () => {
        selectedRowIds.forEach((itemId) => {
            const a = document.createElement('a')
            const [id, type] = itemId.slice(0, -1).split('.');
            a.href = `${baseUrl}/${type === 'directory' ? 'directory' : 'notes'}/${id}/download`
            a.download = names[ids.indexOf(itemId)]
            document.body.appendChild(a)
            a.click()
            document.body.removeChild(a)
        });
    });


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

        selectedRowIds.add(ids[i]);
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

        selectedRowIds.delete(ids[i]);
    }

    updateSelectedButtonsState();
}

selectAllButton.addEventListener('click', selectAll);
deselectAllButton.addEventListener('click', deselectAll);
