let profilePic = document.getElementById('preview-image');

function changeImage() {
    let reader = new FileReader();

    reader.addEventListener('load', function() {
        profilePic.setAttribute('src', reader.result);
    });

    reader.readAsDataURL(this.files[0]);
}


let editInfoButton = document.getElementById('edit-info-button');
let cancelEditButton = document.getElementById('cancel-edit-button');
let dynamicInfo = document.getElementsByClassName('dynamic-info');
let staticInfo = document.getElementsByClassName('static-info');
let updateInfo = document.getElementById('update-info');
let hiddenPencil = document.getElementById('hidden-pencil');
let imageInput = document.getElementById('image-input');
let selectedImage = document.getElementById('selected-image');

editInfoButton.addEventListener('click', function() {
    for (let i = 0; i < dynamicInfo.length; i++) {
        dynamicInfo[i].classList.remove('d-none');
    }
    for (let i = 0; i < staticInfo.length; i++) {
        staticInfo[i].classList.add('d-none');
    }
    editInfoButton.classList.add('d-none');
    updateInfo.classList.remove('d-none');
    hiddenPencil.classList.remove('d-none');
    let input = document.createElement('input');
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
    for (let i = 0; i < dynamicInfo.length; i++) {
        dynamicInfo[i].classList.add('d-none');
    }
    for (let i = 0; i < staticInfo.length; i++) {
        staticInfo[i].classList.remove('d-none');
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