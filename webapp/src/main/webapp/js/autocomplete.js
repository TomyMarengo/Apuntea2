function autocomplete(inp, inpNext, sel, getArr, onCompletion) {
    /*the autocomplete function takes four arguments,
    the hidden input with the form data,
    the text field element for autocompletion,
    the function that returns the array of possible autocompleted values,
    the function that computes other fields when the autocomplete changes
    */
    let currentFocus;

    /*execute a function when someone writes in the text field:*/
    function performAutocomplete() {
        let arr = getArr();
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
            let position = textNormalize(arr[i].text.toUpperCase()).indexOf(textNormalize(val.toUpperCase()));
            if(position > -1) {
                /*create a DIV element for each matching element:*/
                b = document.createElement("DIV");
                let prefix = document.createElement("SPAN");
                let middle = document.createElement("STRONG");
                let sufix = document.createElement("SPAN");
                b.append(prefix, middle, sufix);
                /*make the matching letters bold:*/
                b.setAttribute("uuid", arr[i].value);
                b.setAttribute("str", arr[i].text);

                prefix.textContent = arr[i].text.substring(0, position);
                middle.textContent += arr[i].text.substring(position, position + val.length);
                sufix.textContent += arr[i].text.substring(position + val.length);

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
                    onCompletion();
                    if (inpNext) inpNext.focus();
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
    inp.addEventListener("focus", function () {
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
}

function textNormalize(text) {
    return text.normalize('NFD')
        .replace(/([^n\u0300-\u036f]|n(?!\u0303(?![\u0300-\u036f])))[\u0300-\u036f]+/gi,"$1")
        .normalize();
}