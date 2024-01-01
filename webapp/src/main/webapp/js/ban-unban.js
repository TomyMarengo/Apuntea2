/**************************/
/***** UN|BAN BUTTONS *****/
/*************************/
const banButtons = document.querySelectorAll('.ban-button');
const banUserForm = document.getElementById('banUserForm');
const unbanUserName = document.getElementById('unbanUserName');

banButtons.forEach(button => {
    button.addEventListener('click', () => {
        const id = button.getAttribute('id').split('.', 1)[0];
        banUserForm.querySelector('#banUserId').value = id;
        banUserName.innerHTML = button.getAttribute('data-email');
    });
});

const unbanButtons = document.querySelectorAll('.unban-button');
const unbanUserForm = document.getElementById('unbanUserForm');
const banUserName = document.getElementById('banUserName');

unbanButtons.forEach(button => {
    button.addEventListener('click', () => {
        const id = button.getAttribute('id').split('.', 1)[0];
        unbanUserForm.querySelector('#unbanUserId').value = id;
        /* get data-username attribute value and set it to banUserName */
        unbanUserName.innerHTML = button.getAttribute('data-email');
    });
});