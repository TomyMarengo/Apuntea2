// TOOLTIP
const tooltipTriggerList = document.querySelectorAll('[data-bs-toggle="tooltip"]')
const tooltipList = [...tooltipTriggerList].map(tooltipTriggerEl => new bootstrap.Tooltip(tooltipTriggerEl))

const toastBootstrap = bootstrap.Toast.getOrCreateInstance(document.getElementById('liveToast'));
function displayToast(message){
    document.querySelector('#liveToast #text').textContent = message;
    toastBootstrap.show();
}

