if (institutionId != null) {
    document.getElementById("institutionId").value = institutionId;
}
if (careerId != null) {
    document.getElementById("careerId").value = careerId;
}
let newSubjectSelect = document.getElementById('newSubjectSelect');
// Initialize an empty array to store the option values
if (newSubjectSelect && unownedSubjects) {
    document.getElementById("eraseNewSubjectButton").addEventListener("click", _ => {
        document.getElementById("newSubjectAutocomplete").value = "";
        document.getElementById('newSubjectId').value = "";
        setupNewSubject();
    });
    autocomplete(document.getElementById("newSubjectAutocomplete"), document.getElementById("newSubjectId"),
            _ => unownedSubjects.map(s => ({value: s.subjectId , text: s.name})), setupNewSubject);
}

function setupNewSubject() {

}

const rows = document.querySelectorAll('.note-found');

rows.forEach((row, index) => {
    const subjectId = row.getAttribute('data-subject-id');
    const year = row.getAttribute('data-year');
    const editButton = row.querySelector(`#edit-${subjectId}`);
    editButton.addEventListener('click', () => {
        const editSubjectForm = document.getElementById('editSubjectForm');
        editSubjectForm.querySelector('#editSubjectId').value = subjectId;
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