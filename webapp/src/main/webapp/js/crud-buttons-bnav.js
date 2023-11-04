/**************************/
/****** EDIT BUTTONS ******/
/*************************/
function editBNavDirectory(id, name, visible, iconColor, parentId, error = false) {
    editDirectoryForm.action = `${baseUrl}/directory/${id}/`;
    editDirectoryForm.querySelectorAll('#parentId')[0].value = parentId;
    if(!error) {
        editDirectoryForm.querySelectorAll('#directoryId')[0].value = id;
        editDirectoryForm.querySelectorAll('#name')[0].value = name;
        editDirectoryForm.querySelectorAll(`#color${iconColor}`)[0].checked = true;
        editDirectoryForm.querySelectorAll('#visible')[0].value = visible;
    }
}
