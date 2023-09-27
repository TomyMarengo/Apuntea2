function autocomplete(inp, sel, arr) {
    /*the autocomplete function takes two arguments,
  the text field element and an array of possible autocompleted values:*/
    let currentFocus;

    /*execute a function when someone writes in the text field:*/
    function performAutocomplete() {
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

/* ------------ */
/* INSTITUTIONS */
/* ------------ */
let selectElement = document.getElementById('institutionSelect');
const institutions = [];
if (selectElement) {
    // Initialize an empty array to store the option values
    // Iterate through the option elements and add their text values to the array
    for (let i = 1; i < selectElement.options.length; i++) {
        const option = selectElement.options[i];
        institutions.push({value: option.value, text: option.text});
    }
    autocomplete(document.getElementById("institutionAutocomplete"), document.getElementById("institutionId"), institutions);
}
/* ------------ */
/* -- CAREERS - */
/* ------------ */
selectElement = document.getElementById('careerSelect');
const careers = [];
if (selectElement) {
    // Initialize an empty array to store the option values
    // Iterate through the option elements and add their text values to the array
    for (let i = 1; i < selectElement.options.length; i++) {
        const option = selectElement.options[i];
        careers.push({value: option.value, text: option.text});
    }
    autocomplete(document.getElementById("careerAutocomplete"), document.getElementById("careerId"), careers);
}
/* ------------ */
/* - SUBJECTS - */
/* ------------ */
selectElement = document.getElementById('subjectSelect');
// Initialize an empty array to store the option values
const subjects = [];
if (selectElement) {
    // Iterate through the option elements and add their text values to the array
    for (let i = 1; i < selectElement.options.length; i++) {
        const option = selectElement.options[i];
        subjects.push({value: option.value, text: option.text});
    }
    autocomplete(document.getElementById("subjectAutocomplete"), document.getElementById("subjectId"), subjects);
}


document.addEventListener('DOMContentLoaded', function () {
    const institutionValue = document.getElementById('institutionId')?.value;
    const careerValue = document.getElementById('careerId')?.value;
    const subjectValue = document.getElementById('subjectId')?.value;

    // Los valores no están vacíos, ahora actualiza los elementos select correspondientes
    let institutionAutocomplete = document.getElementById('institutionAutocomplete');
    let careerAutocomplete = document.getElementById('careerAutocomplete');
    let subjectAutocomplete = document.getElementById('subjectAutocomplete');

    let ins = institutions.find(x => x.value === institutionValue);
    let career = careers.find(x => x.value === careerValue);
    let subject = subjects.find(x => x.value === subjectValue);
    // Establece el valor de los elementos select según los valores de id
    if (institutionAutocomplete) institutionAutocomplete.value = ins ? ins.text : '';
    if (careerAutocomplete) careerAutocomplete.value = career ? career.text : '';
    if (subjectAutocomplete) subjectAutocomplete.value = subject ? subject.text : '';
});