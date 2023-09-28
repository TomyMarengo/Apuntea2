document.addEventListener("DOMContentLoaded", function () {
    const searchButton = document.getElementById("searchNavButton");
    const searchInput = document.getElementById("searchNavInput");

    // Función para realizar la búsqueda
    function realizarBusqueda() {
        const searchTerm = searchInput.value.trim(); // Obtener el valor del input

        if (searchTerm !== "") {
            // Redirigir a la URL de búsqueda con el término
            window.location.href = `${baseUrl}/search?word=${encodeURIComponent(searchTerm)}`;
        }
    }

    // Escuchar clic en el botón de búsqueda
    searchButton.addEventListener("click", realizarBusqueda);

    // Escuchar el evento "keydown" en el input para detectar la tecla Enter
    searchInput.addEventListener("keydown", function (event) {
        if (event.key === "Enter") {
            realizarBusqueda();
        }
    });
});



