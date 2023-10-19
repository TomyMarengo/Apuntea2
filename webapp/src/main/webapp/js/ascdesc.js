// const arrowSpan = document.getElementById("arrow-span");
const arrowImage = document.getElementById("arrowImage");
const ascCheckbox = document.getElementById("ascCheckbox");
const ascDescButton = document.getElementById("ascDescButton");

if (!baseUrl) {
    const currentUrl = window.location.origin + '/'
    const path = window.location.pathname.split('/')[1];
    const baseUrl = currentUrl + path
}

ascDescButton.addEventListener("click", function () {
    if (arrowImage.getAttribute("title") === "ascending") {
        arrowImage.src = `${baseUrl}/svg/arrow-down.svg`;
        arrowImage.setAttribute("title", "descending");
    } else {
        arrowImage.src = `${baseUrl}/svg/arrow-up.svg`;
        arrowImage.setAttribute("title", "ascending");
    }
    ascCheckbox.click();
})