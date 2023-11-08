const arrowImage = document.getElementById("arrowImage");
const ascDescButton = document.getElementById("ascDescButton");

ascDescButton.addEventListener("click", function () {
    if (arrowImage.getAttribute("title") === "ascending") {
        arrowImage.src = `${baseUrl}/svg/arrow-down.svg`;
        arrowImage.setAttribute("title", "descending");
    } else {
        arrowImage.src = `${baseUrl}/svg/arrow-up.svg`;
        arrowImage.setAttribute("title", "ascending");
    }
})

const sortBySelect = document.getElementById('sortBySelect');
const yearSelect = document.getElementById('yearSelect');

const subjectFounds = document.querySelectorAll('.note-found');
const subjectTable = document.getElementById('subjectTable');

function sortSubjects() {
    const sortBy = sortBySelect.value;
    const year = yearSelect.value;
    const sortedSubjects = Array.from(subjectFounds).sort((a, b) => {
        if (sortBy === 'name') {
            return a.dataset.name.localeCompare(b.dataset.name);
        } else if (sortBy === 'year') {
            return a.dataset.year - b.dataset.year;
        }
    });
    subjectTable.innerHTML = '';
    sortedSubjects.forEach(subject => {
        if (year === 'all' || subject.dataset.year === year) {
            subjectTable.appendChild(subject);
        }
    });
}

sortBySelect.addEventListener('change', sortSubjects);
yearSelect.addEventListener('change', sortSubjects);
