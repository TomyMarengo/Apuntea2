/**************************/
/******* DELETE ALL *******/
/*************************/

document.getElementById('openDeleteSelectedModalButton').addEventListener('click', function () {
    const deleteForm = document.getElementById('deleteForm');

    const existingInputs = deleteForm.querySelectorAll('input[name^="directoryIds"], input[name^="noteIds"]');
    existingInputs.forEach(input => {
        deleteForm.removeChild(input);
    });

    selectedRowIds.forEach(itemId => {
        const {id, category} = content.find(item => item.id === itemId);
        const input = document.createElement('input');
        input.type = 'hidden';
        input.name = category === 'directory' ? 'directoryIds' : 'noteIds';
        input.value = id;
        deleteForm.appendChild(input);
    });
});

document.getElementById('deleteSelectedButton').addEventListener('click', function () {
    // Submit the form when the "Delete" button in the modal is clicked
    document.getElementById('deleteForm').submit();
});


/**************************/
/****** EDIT BUTTONS ******/
/*************************/
const editButtons = document.querySelectorAll('.edit-button');
const editNoteForm = document.getElementById('editNoteForm');
const editDirectoryForm = document.getElementById('editDirectoryForm');

function edit(id, error = false) {
    const { category, name, visible, iconColor } = content.find(item => item.id === id);

    if (category === 'directory') {
        editDirectoryForm.action = `${baseUrl}/directory/${id}/`;
        if(!error) {
            editDirectoryForm.querySelectorAll('#name')[0].value = name;
            editDirectoryForm.querySelectorAll(`#color${iconColor}`)[0].checked = true;
            editDirectoryForm.querySelectorAll('#visible')[0].value = visible;
        }
    }
    else {
        editNoteForm.action = `${baseUrl}/notes/${id}/`;
        if(!error) {
            editNoteForm.querySelectorAll('#name')[0].value = name;
            editNoteForm.querySelectorAll('#categorySelect')[0].value = category;
            editNoteForm.querySelectorAll('#visible')[0].value = visible;
        }
    }
}

editButtons.forEach(button => {
    button.addEventListener('click', () => {
        const id = button.getAttribute('id').split('.', 1)[0];
        edit(id);
    });
});

/**************************/
/***** DELETE BUTTONS *****/
/*************************/
const deleteButtons = document.querySelectorAll('.delete-button');
deleteButtons.forEach(button => {
    button.addEventListener('click', () => {
        const deleteForm = document.getElementById('deleteForm');

        const existingInputs = deleteForm.querySelectorAll('input[name^="directoryIds"], input[name^="noteIds"]');
        existingInputs.forEach(input => {
            deleteForm.removeChild(input);
        });

        const id = button.getAttribute('id').split('.', 1)[0];
        const category = content.find(item => item.id === id).category;
        const input = document.createElement('input');
        input.type = 'hidden';
        input.name = category === 'directory' ? 'directoryIds' : 'noteIds';
        input.value = id;
        deleteForm.appendChild(input);
    });
});