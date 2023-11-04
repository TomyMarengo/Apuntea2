var profilePic = document.getElementById('preview-image');

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

let careerSelect = document.getElementById('careerSelect');
// Initialize an empty array to store the option values
if (careerSelect && careers) {
    document.getElementById("eraseCareerButton").addEventListener("click", _ => {
        document.getElementById("careerAutocomplete").value = "";
        document.getElementById('careerId').value = "";
        setupCareer();
    });
    autocomplete(document.getElementById("careerAutocomplete"), document.getElementById("careerId"),
        _ => careers.map(c => ({value: c.careerId , text: c.name})), setupCareer);
}
function setupCareer() {

}