const slides = document.querySelectorAll(".slider-content");
const pageNav = document.querySelectorAll(".navigator.dot");

// handles page up for touch, scroll, mouse swipe
function pageUp(e) {
    if (e.target.nextElementSibling != null) {
        e.target.classList.add("prev");
        e.target.classList.remove("current");
        e.target.nextElementSibling.classList.add("current");
        e.target.nextElementSibling.classList.remove("next");
        let key = e.target.nextElementSibling.getAttribute("data-key");
        pageNav.forEach(
            item =>
                item.getAttribute("data-key") === key
                    ? item.classList.add("selected")
                    : item.classList.remove("selected")
        );
    }
}

// handles page down for touch, scroll, mouse swipe
function pageDown(e) {
    if (e.target.previousElementSibling != null) {
        e.target.classList.add("next");
        e.target.classList.remove("current");
        e.target.previousElementSibling.classList.add("current");
        e.target.previousElementSibling.classList.remove("prev");
        let key = e.target.previousElementSibling.getAttribute("data-key");
        pageNav.forEach(
            item =>
                item.getAttribute("data-key") === key
                    ? item.classList.add("selected")
                    : item.classList.remove("selected")
        );
    }
}

function onScroll(e) {
    // Find the closest slide to the event target
    let slide = e.target.closest('.slider-content');

    // If a slide is found and the wheel event is applicable (not over content)
    if (slide) {
        if (e.wheelDelta < 0) {
            pageUp({ target: slide });
        } else {
            pageDown({ target: slide });
        }
    }
}

// left-side page nav
function clickNav(e) {
    let key = e.target.getAttribute("data-key");

    slides.forEach(slide => {
        if (slide.getAttribute("data-key") === key) {
            e.target.classList.add("selected");
            slide.classList.add("current");
            slide.classList.remove("next");
            slide.classList.remove("prev");
            if (slide.nextElementSibling != null) {
                slide.nextElementSibling.classList.add("next");
                slide.nextElementSibling.classList.remove("current");
            }
            if (slide.previousElementSibling) {
                slide.previousElementSibling.classList.add("prev");
                slide.previousElementSibling.classList.remove("current");
            }
        } else {
            let slideKey = slide.getAttribute("data-key");

            slideKey > key
                ? (slide.classList.replace("current", "next"),
                    slide.classList.replace("prev", "next"))
                : (slide.classList.replace("current", "prev"),
                    slide.classList.replace("next", "prev"));

            pageNav.forEach(dot => {
                if (dot.getAttribute("data-key") === slideKey) {
                    dot.classList.remove("selected");
                }
            });
        }
    });
}

let xDown = null;
let yDown = null;

function getTouches(evt) {
    return evt.touches;
}

function handleTouchStart(e) {
    if (e.type === "mousedown") {
        xDown = e.clientX;
        yDown = e.clientY;
    } else {
        xDown = getTouches(e)[0].clientX;
        yDown = getTouches(e)[0].clientY;
    }
}

function handleTouchMove(e) {
    let xUp = null;
    let yUp = null;

    if (!xDown || !yDown) {
        return;
    }

    if (e.type === "mouseup") {
        xUp = e.clientX;
        yUp = e.clientY;
    } else {
        xUp = e.touches[0].clientX;
        yUp = e.touches[0].clientY;
    }

    let xDiff = xDown - xUp;
    let yDiff = yDown - yUp;

    if (Math.abs(xDiff) > Math.abs(yDiff)) {
        /*most significant*/
        if (xDiff > 0) {
            /* left swipe */
            console.log("left");
        } else {
            /* right swipe */
            console.log("right");
        }
    } else {
        if (yDiff > 0) {
            /* up swipe */
            pageUp(e);
        } else if (yDiff < 0) {
            /* down swipe */

            pageDown(e);
        }
    }
    /* reset values */
    xDown = null;
    yDown = null;
}


document.addEventListener("wheel", onScroll);
document.addEventListener("touchstart", handleTouchStart, false);
document.addEventListener("touchmove", handleTouchMove, false);
document.addEventListener("mousedown", handleTouchStart, false);
document.addEventListener("mouseup", handleTouchMove, false);
pageNav.forEach(item => item.addEventListener("click", clickNav));