
console.log(getComputedStyle(document.documentElement).getPropertyValue("--header-height"));

window.addEventListener("scroll", function() {
    const sidebar = document.querySelector(".sidebar");
    const headerHeight = getComputedStyle(document.documentElement).getPropertyValue("--header-height");
    const scrollPosition = window.scrollY;

    sidebar.style.top = `calc(${headerHeight} - ${scrollPosition}px)`;

});