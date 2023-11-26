if (institutionId != null) {
    document.getElementById("institutionId").value = institutionId;
}
if (careerId != null) {
    document.getElementById("careerId").value = careerId;
}
let linkSubjectSelect = document.getElementById('linkSubjectSelect');
// Initialize an empty array to store the option values
if (linkSubjectSelect && unownedSubjects) {
    document.getElementById("eraseLinkSubjectButton").addEventListener("click", _ => {
        document.getElementById("linkSubjectAutocomplete").value = "";
        document.getElementById('linkSubjectId').value = "";
        setupLinkSubject();
    });
    autocomplete(document.getElementById("linkSubjectAutocomplete"), null, document.getElementById("linkSubjectId"),
            _ => unownedSubjects.map(s => ({value: s.subjectId , text: s.name})), setupLinkSubject);
}

function setupLinkSubject() {

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
        editSubjectForm.querySelector('#editSubjectName').value = name;
        editSubjectForm.querySelector('#editSubjectYear').value = year;
    });
    const unlinkButton = row.querySelector(`#unlink-${subjectId}`);
    unlinkButton.addEventListener('click', () => {
        const unlinkSubjectForm = document.getElementById('unlinkSubjectForm');
        unlinkSubjectForm.querySelector('#unlinkSubjectId').value = subjectId;
        unlinkSubjectForm.querySelector('#unlinkSubjectName').textContent = name;
    });
});
//variable declared in ics-autocomplete.js
extendedOnCompletion = (autocompleting) => {
    let careerId = document.getElementById('careerId').value;
    if (autocompleting && careerId != null && careerId !== "") {
        window.location.href = `${baseUrl}/manage/careers/${careerId}`;
    }
}