const arrowImage = document.getElementById("arrowImage");
const ascCheckbox = document.getElementById("ascCheckbox");
arrowImage.addEventListener("click", function () {
  if (arrowImage.getAttribute("title") === "ascending") {
    arrowImage.src = '../svg/arrow-down.svg';
    arrowImage.setAttribute("title", "descending");
  } else {
    arrowImage.src = '../svg/arrow-up.svg';
    arrowImage.setAttribute("title", "ascending");
  }
  ascCheckbox.click();
})