/**************************/
/***** UN|BAN BUTTONS *****/
/*************************/
const banButtons = document.querySelectorAll('.ban-button');
const banUserForm = document.getElementById('banUserForm');
banButtons.forEach(button => {
    button.addEventListener('click', () => {
        const id = button.getAttribute('id').split('.', 1)[0];
        banUserForm.querySelector('#banUserId').value = id;
    });
});

const unbanButtons = document.querySelectorAll('.unban-button');
const unbanUserForm = document.getElementById('unbanUserForm');
unbanButtons.forEach(button => {
    button.addEventListener('click', () => {
        const id = button.getAttribute('id').split('.', 1)[0];
        unbanUserForm.querySelector('#unbanUserId').value = id;
    });
});