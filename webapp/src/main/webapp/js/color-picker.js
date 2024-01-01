const colorOptions = document.querySelectorAll('.color-option');
const colorRadios = document.querySelectorAll('.color-radio');

colorOptions.forEach((colorOption, index) => {
    colorOption.addEventListener('click', (event) => {
        colorRadios.forEach((colorRadio) => {
            colorRadio.checked = false;
        });
        colorRadios[index].checked = true;
    });
});