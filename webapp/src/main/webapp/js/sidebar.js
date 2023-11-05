const sidebars = document.querySelectorAll(".sidebar");
const headerHeight = getComputedStyle(document.documentElement).getPropertyValue("--nav-height");

window.addEventListener("scroll", function() {
    const scrollPosition = window.scrollY;

    sidebars.forEach(sidebar => {
        sidebar.style.top = `calc(${headerHeight} - ${scrollPosition}px)`;
        sidebar.style.height = `calc(100vh - ${headerHeight} + ${scrollPosition}px)`;
    })
});