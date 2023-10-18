const pageNumberInput = document.getElementById('pageNumber');
const paginationLinks = document.querySelectorAll('.page-link');


for (let i = 0; i < paginationLinks.length; i++) {
    const link = paginationLinks[i];

    link.addEventListener('click', () => {
        pageNumberInput.value = link.getAttribute('data-page');
        document.getElementById('searchForm').submit();
    });
}