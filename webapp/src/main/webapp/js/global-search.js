const searchButton = document.getElementById("searchNavButton");
const searchInput = document.getElementById("searchNavInput");

function globalSearch(event) {
    event.preventDefault();
    const searchTerm = searchInput.value.trim();
    const params = searchButton.href.split("?")[1];
    if (searchTerm !== "") {
        window.location.href = `${baseUrl}/search?${params}&word=${encodeURIComponent(searchTerm)}`;
    } else {
        window.location.href = `${baseUrl}/search?${params}`;
    }
}

searchButton.addEventListener("click", function (event) {
    globalSearch(event);
});

searchInput.addEventListener("keydown", function (event) {
    if (event.key === "Enter") {
        globalSearch(event);
    }
})