// Selecciona las pestañas y el contenido de las pestañas
var tabLinks = document.querySelectorAll('.nav-link');
var tabContents = document.querySelectorAll('.tab-pane');

// Agrega un evento de clic a cada pestaña
tabLinks.forEach(function(tabLink, index) {
    tabLink.addEventListener('click', function(event) {
        // Previene el comportamiento predeterminado del enlace
        event.preventDefault();

        // Oculta todos los contenidos de las pestañas
        tabContents.forEach(function(tabContent) {
            tabContent.classList.remove('show', 'active');
        });

        // Muestra el contenido de la pestaña correspondiente
        tabContents[index].classList.add('show', 'active');

        // Quita la clase activa de todas las pestañas
        tabLinks.forEach(function(link) {
            link.classList.remove('active');
        });

        // Agrega la clase activa a la pestaña clicada
        tabLink.classList.add('active');
    });
});

var profilePic = document.getElementById('preview-image');
var fileInput = document.getElementById('profilePicture');

function changeImage() {
    var reader = new FileReader();

    reader.addEventListener('load', function() {
        profilePic.setAttribute('src', reader.result);
    });

    reader.readAsDataURL(this.files[0]);
}



var editInfoButton = document.getElementById('edit-info-button');
var dynamicInfo = document.getElementsByClassName('dynamic-info');
var updateInfo = document.getElementById('update-info');
var cancelEditButton = document.getElementById('cancel-edit-button');
var hiddenPencil = document.getElementById('hidden-pencil');
var imageInput = document.getElementById('image-input');
var selectedImage = document.getElementById('selected-image');

editInfoButton.addEventListener('click', function() {
    for (var i = 0; i < dynamicInfo.length; i++) {
        dynamicInfo[i].disabled = false;
    }
    editInfoButton.classList.add('d-none');
    updateInfo.classList.remove('d-none');
    hiddenPencil.classList.remove('d-none');
    var input = document.createElement('input');
    input.setAttribute('type', 'file');
    input.setAttribute('name', 'profilePicture');
    input.setAttribute('style', 'display: none');
    input.setAttribute('id', 'profilePicture');
    input.setAttribute('accept', 'image/*');
    imageInput.appendChild(input);
    input.addEventListener('change', changeImage);
    selectedImage.classList.add('layout-image');
});

cancelEditButton.addEventListener('click', function() {
    for (var i = 0; i < dynamicInfo.length; i++) {
        dynamicInfo[i].disabled = true;
    }
    editInfoButton.classList.remove('d-none');
    updateInfo.classList.add('d-none');
    hiddenPencil.classList.add('d-none');
    imageInput.removeChild(imageInput.lastChild);
    selectedImage.classList.remove('layout-image');
});

