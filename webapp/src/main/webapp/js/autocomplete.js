function autocomplete(inp, sel, getArr) {
    /*the autocomplete function takes two arguments,
  the text field element and an array of possible autocompleted values:*/
    let currentFocus;

    /*execute a function when someone writes in the text field:*/
    function performAutocomplete() {
        var arr = getArr();
        let a, b, i, val = this.value;
        /*close any already open lists of autocompleted values*/
        closeAllLists();
        currentFocus = -1;
        /*create a DIV element that will contain the items (values):*/
        a = document.createElement("DIV");
        a.setAttribute("id", this.id + "-autocomplete-list");
        a.setAttribute("class", "autocomplete-items");
        /*append the DIV element as a child of the autocomplete container:*/
        this.parentNode.appendChild(a);
        /*for each item in the array...*/
        for (i = 0; i < arr.length; i++) {
            /*check if the item starts with the same letters as the text field value:*/
            if (arr[i].text.substring(0, val.length).toUpperCase() === val.toUpperCase()) {
                /*create a DIV element for each matching element:*/
                b = document.createElement("DIV");
                /*make the matching letters bold:*/
                b.setAttribute("uuid", arr[i].value);
                b.setAttribute("str", arr[i].text);
                b.innerHTML = "<strong>" + arr[i].text.substring(0, val.length) + "</strong>";
                b.innerHTML += arr[i].text.substring(val.length);
                if (arr[i].text.length === val.length) {
                    sel.value = arr[i].value;
                }
                /*execute a function when someone clicks on the item value (DIV element):*/
                b.addEventListener("click", function () {
                    /*insert the value for the autocomplete text field:*/
                    inp.value = this.getAttribute("str");
                    sel.value = this.getAttribute("uuid");
                    /*close the list of autocompleted values,
                    (or any other open lists of autocompleted values:*/
                    closeAllLists();
                    loadFields();
                });
                a.appendChild(b);
            }
        }
        if (this.value.trim() === "") {
            inp.value = '';
            sel.value = '';
        }
    }

    document.addEventListener("click", function (e) {
        closeAllLists(e.target);
    }, true);

    inp.addEventListener("input", function () {
        performAutocomplete.call(this);
    });
    inp.addEventListener("click", function () {
        performAutocomplete.call(this);
    });
    /*execute a function presses a key on the keyboard:*/
    inp.addEventListener("keydown", function (e) {
        let x = document.getElementById(this.id + "-autocomplete-list");
        if (x) x = x.getElementsByTagName("div");
        if (e.keyCode === 40) {
            /*If the arrow DOWN key is pressed,
      increase the currentFocus variable:*/
            currentFocus++;
            /*and make the current item more visible:*/
            addActive(x);
        } else if (e.keyCode === 38) { //up
            /*If the arrow UP key is pressed,
      decrease the currentFocus variable:*/
            currentFocus--;
            /*and make the current item more visible:*/
            addActive(x);
        } else if (e.keyCode === 13) {
            /*If the ENTER key is pressed, prevent the form from being submitted,*/
            e.preventDefault();
            if (currentFocus > -1) {
                /*and simulate a click on the "active" item:*/
                if (x) x[currentFocus].click();
            }
        }
    });

    function addActive(x) {
        /*a function to classify an item as "active":*/
        if (!x) return false;
        /*start by removing the "active" class on all items:*/
        removeActive(x);
        if (currentFocus >= x.length) currentFocus = 0;
        if (currentFocus < 0) currentFocus = (x.length - 1);
        /*add class "autocomplete-active":*/
        x[currentFocus].classList.add("autocomplete-active");
    }

    function removeActive(x) {
        /*a function to remove the "active" class from all autocomplete items:*/
        for (let i = 0; i < x.length; i++) {
            x[i].classList.remove("autocomplete-active");
        }
    }

    function closeAllLists(elmnt) {
        /*close all autocomplete lists in the document,
    except the one passed as an argument:*/
        let x = document.getElementsByClassName("autocomplete-items");
        for (let i = 0; i < x.length; i++) {
            if (elmnt !== x[i] && elmnt !== inp) {
                x[i].parentNode.removeChild(x[i]);
            }
        }
    }

    /*execute a function when someone clicks in the document:*/
}


function clearInstitution() {
    document.getElementById("institutionAutocomplete").value = "";
    document.getElementById('institutionId').value = "";
    clearCareer();
}

function clearCareer() {
    document.getElementById("careerAutocomplete").value = "";
    document.getElementById('careerId').value = "";
    clearSubject();
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
        () => institutions.map(i => ({value: i.institutionId, text: i.name})));
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
        return careerMap[institutionId].map(c => ({value: c.careerId, text: c.name}));
    });
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
        return subjectMap[careerId].map(s => ({value: s.subjectId, text: s.name}));
    });
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