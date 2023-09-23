const pageNumberInput = document.getElementById('pageNumber');
const previousPageLink = document.getElementById('previousPage');
const nextPageLink = document.getElementById('nextPage');
const paginationLinks = document.querySelectorAll('.page-link');


// Add event listener click to all pagination links except previous and next
for (let i = 1; i < paginationLinks.length - 1; i++) {
    const link = paginationLinks[i];

    link.addEventListener('click', () => {
        pageNumberInput.value = link.getAttribute('data-page');
        document.getElementById('searchForm').submit();
    });
}

previousPageLink.addEventListener('click', (e) => {
    e.preventDefault();
    const currentPage = parseInt(pageNumberInput.value);
    if (currentPage > 1) {
        pageNumberInput.value = currentPage - 1;
        document.getElementById('searchForm').submit();
    }
});

nextPageLink.addEventListener('click', (e) => {
    e.preventDefault();
    const currentPage = parseInt(pageNumberInput.value);
    if (currentPage < paginationLinks[paginationLinks.length-2].getAttribute('data-page')) {
        pageNumberInput.value = currentPage + 1;
        document.getElementById('searchForm').submit();
    }
});