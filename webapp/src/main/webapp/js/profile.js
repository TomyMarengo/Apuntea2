//on click on another tab, set the current tab active
var rdYears = document.querySelectorAll('.rd-year');
var rdSubjectList = document.querySelectorAll('.rd-subject-list');

rdYears.forEach(function(tabLink, index) {
    tabLink.addEventListener('click', function(event) {
        event.preventDefault();

        rdSubjectList.forEach(function (tabContent) {
            tabContent.classList.remove('show', 'active');
        });

        rdSubjectList[index].classList.add('show', 'active');

        rdYears.forEach(function (link) {
            link.classList.remove('active');
        });

        rdYears[index].classList.add('active');
    });
});
