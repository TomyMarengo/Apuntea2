const arrowImage = document.getElementById("arrowImage");
const ascDescButton = document.getElementById("ascDescButton");

if (ascDescButton) {
    ascDescButton.addEventListener("click", function () {
        if (arrowImage.getAttribute("title") === "ascending") {
            arrowImage.src = `${baseUrl}/svg/arrow-down.svg`;
            arrowImage.setAttribute("title", "descending");
            sortSubjects(true);
        } else {
            arrowImage.src = `${baseUrl}/svg/arrow-up.svg`;
            arrowImage.setAttribute("title", "ascending");
            sortSubjects(false);
        }
    })
}


const sortBySelect = document.getElementById('sortBySelect');
const yearSelect = document.getElementById('yearSelect');

const subjectFounds = document.querySelectorAll('.note-found');
const subjectTable = document.getElementById('subjectTable');

function sortSubjects(sortAscending) {
    const sortBy = sortBySelect.value;
    const year = yearSelect.value;
    const sortedSubjects = Array.from(subjectFounds).sort((a, b) => {
        if (sortAscending) {
            if (sortBy === 'name') {
                return a.dataset.name.localeCompare(b.dataset.name);
            } else if (sortBy === 'year') {
                if (a.dataset.year === b.dataset.year)
                    return a.dataset.name.localeCompare(b.dataset.name);
                return a.dataset.year - b.dataset.year;
            }
        }
        if (sortBy === 'name') {
            return b.dataset.name.localeCompare(a.dataset.name);
        } else if (sortBy === 'year') {
            if (a.dataset.year === b.dataset.year)
                return a.dataset.name.localeCompare(b.dataset.name);
            return b.dataset.year - a.dataset.year;
        }
    });
    subjectTable.innerHTML = '';
    sortedSubjects.forEach(subject => {
        if (year === 'all' || subject.dataset.year === year) {
            subjectTable.appendChild(subject);
        }
    });
}

if (sortBySelect && yearSelect) {
    sortSubjects(true);

    sortBySelect.addEventListener('change', sortSubjects);
    yearSelect.addEventListener('change', (sortAscending) => {
        if (yearSelect.value !== 'all') {
            // disable sort by select
            sortBySelect.disabled = true;
            sortBySelect.value = 'name';
        } else {
            sortBySelect.disabled = false;
        }
        sortSubjects(sortAscending);
    });
}

