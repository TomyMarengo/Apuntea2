// function defined in other file that uses ics-autocomplete.js
var extendedOnCompletion = null;

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
        loadFields(false);
    });
    // Initialize an empty array to store the option values
    autocomplete(document.getElementById("institutionAutocomplete"), document.getElementById("careerAutocomplete"),
        document.getElementById("institutionId"),
        () => institutions.map(i => ({value: i.institutionId, text: i.name}))
        , loadFields);
}


/* ------------ */
/* -- CAREERS - */
/* ------------ */
selectElement = document.getElementById('careerSelect');
if (selectElement && careerMap) {
    document.getElementById("eraseCareerButton").addEventListener("click", _ => {
        clearCareer();
        loadFields(false);
    });
    autocomplete(document.getElementById("careerAutocomplete"), document.getElementById("subjectAutocomplete"), document.getElementById("careerId"), _ => {
        const institutionId = document.getElementById('institutionId').value;
        return careerMap[institutionId].map(c => ({value: c.careerId, text: c.name}));}
        , loadFields);
}
/* ------------ */
/* - SUBJECTS - */
/* ------------ */
selectElement = document.getElementById('subjectSelect');
// Initialize an empty array to store the option values
if (selectElement && subjectMap) {
    document.getElementById("eraseSubjectButton").addEventListener("click", _ => {
        clearSubject();
        loadFields(false);
    });
    autocomplete(document.getElementById("subjectAutocomplete"), null, document.getElementById("subjectId"), _ => {
        const careerId = document.getElementById('careerId').value;
        return subjectMap[careerId].map(s => ({value: s.subjectId, text: s.name}));}
        , loadFields);
}

function loadFields(autocompleting = true) {
    const institutionHidden = document.getElementById('institutionId');
    const careerHidden = document.getElementById('careerId');
    const subjectHidden = document.getElementById('subjectId');

    // Los valores no están vacíos, ahora actualiza los elementos select correspondientes
    let institutionAutocomplete = document.getElementById('institutionAutocomplete');
    let careerAutocomplete = document.getElementById('careerAutocomplete');
    let subjectAutocomplete = document.getElementById('subjectAutocomplete');

    let ins = institutions.find(x => x.institutionId === institutionHidden?.value);
    let career = careerMap[ins?.institutionId]?.find(x => x.careerId === careerHidden?.value);
    let subject = subjectMap[career?.careerId]?.find(x => x.subjectId === subjectHidden?.value);
    // Establece el valor de los elementos select según los valores de id
    if (institutionAutocomplete) {
        institutionAutocomplete.value = ins ? ins.name : '';
        institutionHidden.value = ins ? ins.institutionId : '';
        if (careerAutocomplete) {
            careerAutocomplete.disabled = !institutionAutocomplete.value;
            careerAutocomplete.value = career ? career.name : '';
            careerHidden.value = career ? career.careerId : '';
            if (subjectAutocomplete) {
                subjectAutocomplete.disabled = !careerAutocomplete.value;
                subjectAutocomplete.value = subject ? subject.name : '';
                subjectHidden.value = subject ? subject.subjectId : '';
            }
        }
    }

    if (extendedOnCompletion != null) {
        extendedOnCompletion(autocompleting);
    }
}

document.addEventListener('DOMContentLoaded', () => loadFields(false));