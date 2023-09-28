/**************************/
/***** COPY BUTTONS *******/
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
        const id = button.getAttribute('id').split('.', 1)[0];
        const category = content.find(item => item.id === id).category;
        copyToClipboard(`${baseUrl}/${category === 'directory' ? 'directory' : 'notes'}/${id}`);
    });
});

/**************************/
/***** DOWNLOAD ALL *******/
/*************************/

const downloadSelectedButton = document.getElementById('downloadSelectedButton');

if (downloadSelectedButton)
    downloadSelectedButton.addEventListener('click', () => {
        selectedRowIds.forEach((itemId) => {
            const a = document.createElement('a')
            const {id, category, name } = content.find(item => item.id === itemId)
            a.href = `${baseUrl}/${category === 'directory' ? 'directory' : 'notes'}/${id}/download`
            a.download = name
            document.body.appendChild(a)
            a.click()
            document.body.removeChild(a)
        });
    });

/**************************/
/******* SELECT ALL *******/
/*************************/

const selectAllButton = document.getElementById('selectAllButton');
// Función para seleccionar todas las filas
function selectAll() {
    for (let i = 0; i < rows.length / 2; i++) {
        const checkbox = rows[i].querySelector('.select-checkbox');
        checkbox.checked = true;
        rows[i].classList.add('active-note-found');

        const checkbox2 = rows[i + rows.length / 2].querySelector('.select-checkbox');
        checkbox2.checked = true;
        rows[i + rows.length / 2].classList.add('active-note-found');

        selectedRowIds.add(content[i].id);
    }

    updateSelectedButtonsState();
}
selectAllButton.addEventListener('click', selectAll);

/**************************/
/****** DESELECT ALL ******/
/*************************/

const deselectAllButton = document.getElementById('deselectAllButton');
function deselectAll() {
    for (let i = 0; i < rows.length / 2; i++) {
        const checkbox = rows[i].querySelector('.select-checkbox');
        checkbox.checked = false;
        rows[i].classList.remove('active-note-found');

        const checkbox2 = rows[i + rows.length / 2].querySelector('.select-checkbox');
        checkbox2.checked = false;
        rows[i + rows.length / 2].classList.remove('active-note-found');

        selectedRowIds.delete(content[i].id);
    }

    updateSelectedButtonsState();
}
deselectAllButton.addEventListener('click', deselectAll);


/**************************/
/****** EDIT BUTTONS ******/
/*************************/
const editButtons = document.querySelectorAll('.edit-button');
const editNoteForm = document.getElementById('editNoteForm');
const editDirectoryForm = document.getElementById('editDirectoryForm');

function edit(id, error = false) {
    const { category, name } = content.find(item => item.id === id);

    if (category === 'directory') {
        editDirectoryForm.action = `${baseUrl}/directory/${id}/`;
        if(!error)
            editDirectoryForm.querySelectorAll('#name')[0].value = name;
    }
    else {
        editNoteForm.action = `${baseUrl}/notes/${id}/`;
        if(!error) {
            editNoteForm.querySelectorAll('#name')[0].value = name;
            editNoteForm.querySelectorAll('#categorySelect')[0].value = category;
        }
    }
}

editButtons.forEach(button => {
    button.addEventListener('click', () => {
        const id = button.getAttribute('id').split('.', 1)[0];
        edit(id);
    });
});