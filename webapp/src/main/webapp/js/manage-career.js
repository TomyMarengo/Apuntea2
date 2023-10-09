if (institutionId != null) {
    document.getElementById("institutionId").value = institutionId;
}
if (careerId != null) {
    document.getElementById("careerId").value = careerId;
}
let addSubjectSelect = document.getElementById('addSubjectSelect');
// Initialize an empty array to store the option values
if (addSubjectSelect && unownedSubjects) {
    document.getElementById("eraseAddSubjectButton").addEventListener("click", _ => {
        document.getElementById("addSubjectAutocomplete").value = "";
        document.getElementById('addSubjectId').value = "";
        setupAddSubject();
    });
    autocomplete(document.getElementById("addSubjectAutocomplete"), document.getElementById("addSubjectId"),
            _ => unownedSubjects.map(s => ({value: s.subjectId , text: s.name})), setupAddSubject);
}

function setupAddSubject() {

}

const rows = document.querySelectorAll('.note-found');

rows.forEach((row) => {
    const subjectId = row.getAttribute('data-subject-id');
    const year = row.getAttribute('data-year');
    const name = row.getAttribute('data-name');
    const editButton = row.querySelector(`#edit-${subjectId}`);
    editButton.addEventListener('click', () => {
        const editSubjectForm = document.getElementById('editSubjectForm');
        editSubjectForm.querySelector('#editSubjectId').value = subjectId;
        editSubjectForm.querySelector('#editName').value = name;
        editSubjectForm.querySelector('#editYear').value = year;
    });
});
//variable declared in ics-autocomplete.js
extendedOnCompletion = (autocompleting) => {
    let careerId = document.getElementById('careerId').value;
    if (autocompleting && careerId != null && careerId !== "") {
        window.location.href = `${baseUrl}/careers/${careerId}`;
    }
}