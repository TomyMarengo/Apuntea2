// This function requires the variable 'noteId' to be defined outside the script
const deleteReviewButtons = document.querySelectorAll('.deleteReviewModalButton');
deleteReviewButtons.forEach(button => {
    button.addEventListener('click', () => {
        const userId = button.value;
        document.getElementById('deleteReviewForm').action = `${baseUrl}/manage/users/${userId}/review/${noteId}/delete`;
    });
});
