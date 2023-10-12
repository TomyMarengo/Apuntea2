//on click on another tab, set the current tab active
var tabLinks = document.querySelectorAll('.nav-link');
var tabContents = document.querySelectorAll('.tab-pane');

tabLinks.forEach(function(tabLink, index) {
    tabLink.addEventListener('click', function(event) {
        event.preventDefault();

        tabContents.forEach(function (tabContent) {
            tabContent.classList.remove('show', 'active');
        });

        tabContents[index].classList.add('show', 'active');

        tabLinks.forEach(function (link) {
            link.classList.remove('active');
        });

        tabLinks[index].classList.add('active');
    });
});
