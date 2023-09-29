const searchButton = document.getElementById("searchNavButton");
const searchInput = document.getElementById("searchNavInput");

function globalSearch() {
    const searchTerm = searchInput.value.trim();
    if (searchTerm !== "") {
        window.location.href = `${baseUrl}/search?word=${encodeURIComponent(searchTerm)}`;
    }
}

searchButton.addEventListener("click", globalSearch);
searchInput.addEventListener("keydown", function (event) {
    if (event.key === "Enter") {
        globalSearch();
    }
});



