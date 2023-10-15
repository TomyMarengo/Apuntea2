// TOOLTIP
const tooltipTriggerList = document.querySelectorAll('[data-bs-toggle="tooltip"]')
const tooltipList = [...tooltipTriggerList].map(tooltipTriggerEl => new bootstrap.Tooltip(tooltipTriggerEl))

const toastBootstrap = bootstrap.Toast.getOrCreateInstance(document.getElementById('liveToast'));
function displayToast(message){
    document.querySelector('#liveToast #text').textContent = message;
    toastBootstrap.show();
}

document.querySelectorAll('.close-modal').forEach((element) => {
    element.addEventListener('click', () => {
        const form = element.closest('form');
        if (form.querySelector('.text-danger:not(.d-none)')) {
            location.reload();
        }
    });
});