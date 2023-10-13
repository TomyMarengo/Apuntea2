// This function requires the variable 'noteId' to be defined outside the script
const deleteReviewButtons = document.querySelectorAll('.deleteReviewModalButton');
deleteReviewButtons.forEach(button => {
    button.addEventListener('click', () => {
        const userId = button.value;
        const url = `./${noteId}/review/${userId}/delete`;
        console.log(url);
        document.getElementById('deleteReviewForm').action = url;
    });
});
