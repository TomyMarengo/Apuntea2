function clearInstitution() {
    document.getElementById("institutionAutocomplete").value = "";
    document.getElementById('institutionId').value = "";

    clearCareer();
}

function clearCareer() {
    document.getElementById("careerAutocomplete").value = "";
    document.getElementById('careerId').value = "";
    if (document.getElementById('subjectId')) clearSubject();
}

function clearSubject() {
    document.getElementById("subjectAutocomplete").value = "";
    document.getElementById('subjectId').value = "";
}

/* ------------ */
/* INSTITUTIONS */
/* ------------ */
let selectElement = document.getElementById('institutionSelect');
if (selectElement && institutions) {
    document.getElementById("eraseInstitutionButton").addEventListener("click", _ => {
        clearInstitution();
        loadFields();
    });
    // Initialize an empty array to store the option values
    autocomplete(document.getElementById("institutionAutocomplete"), document.getElementById("institutionId"),
        () => institutions.map(i => ({value: i.institutionId, text: i.name}))
        , loadFields);
}


/* ------------ */
/* -- CAREERS - */
/* ------------ */
selectElement = document.getElementById('careerSelect');
if (selectElement && careers) {
    document.getElementById("eraseCareerButton").addEventListener("click", _ => {
        clearCareer();
        loadFields();
    });
    autocomplete(document.getElementById("careerAutocomplete"), document.getElementById("careerId"), _ => {
        const institutionId = document.getElementById('institutionId').value;
        return careerMap[institutionId].map(c => ({value: c.careerId, text: c.name}));}
        , loadFields);
}
/* ------------ */
/* - SUBJECTS - */
/* ------------ */
selectElement = document.getElementById('subjectSelect');
// Initialize an empty array to store the option values
if (selectElement && subjects) {
    document.getElementById("eraseSubjectButton").addEventListener("click", _ => {
        clearSubject();
        loadFields();
    });
    autocomplete(document.getElementById("subjectAutocomplete"), document.getElementById("subjectId"), _ => {
        const careerId = document.getElementById('careerId').value;
        return subjectMap[careerId].map(s => ({value: s.subjectId, text: s.name}));}
        , loadFields);
}

function loadFields() {
    const institutionValue = document.getElementById('institutionId')?.value;
    const careerValue = document.getElementById('careerId')?.value;
    const subjectValue = document.getElementById('subjectId')?.value;

    // Los valores no están vacíos, ahora actualiza los elementos select correspondientes
    let institutionAutocomplete = document.getElementById('institutionAutocomplete');
    let careerAutocomplete = document.getElementById('careerAutocomplete');
    let subjectAutocomplete = document.getElementById('subjectAutocomplete');

    let ins = institutions.find(x => x.institutionId === institutionValue);
    let career = careers.find(x => x.careerId === careerValue);
    let subject = subjects.find(x => x.subjectId === subjectValue);
    // Establece el valor de los elementos select según los valores de id
    if (institutionAutocomplete) {
        institutionAutocomplete.value = ins ? ins.name : '';
        if (careerAutocomplete) {
            careerAutocomplete.disabled = !institutionAutocomplete.value;
            careerAutocomplete.value = career ? career.name : '';
            if (subjectAutocomplete) {
                subjectAutocomplete.disabled = !careerAutocomplete.value;
                subjectAutocomplete.value = subject ? subject.name : '';
            }
        }
    }
}

document.addEventListener('DOMContentLoaded', loadFields);