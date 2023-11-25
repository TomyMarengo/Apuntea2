// This function requires the variable 'noteId' to be defined outside the script
const deleteReviewButtons = document.querySelectorAll('.deleteReviewModalButton');
deleteReviewButtons.forEach(button => {
    button.addEventListener('click', () => {
        const userId = button.value;
        document.getElementById('deleteReviewForm').action = `${baseUrl}/manage/users/${userId}/review/${noteId}/delete`;
    });
});


const showHideContentButtons = document.querySelectorAll('.show-hide-content-button');
const reviewComments = document.querySelectorAll('.reviews-comment');
const reviewCommentsArray = [];
reviewComments.forEach(reviewComment => {reviewCommentsArray.push({reviewComment: reviewComment, isOpened: false});});

showHideContentButtons.forEach((showHideContentButton, index) => {
    isOverflown(reviewComments[index]) ? showHideContentButton.style.display = 'block' : showHideContentButton.style.display = 'none';
    showHideContentButton.addEventListener('click', () => {
        const reviewComment = reviewCommentsArray[index];
        if (reviewComment.isOpened) {
            reviewComment.reviewComment.classList.add('overflow-hidden');
            reviewComment.reviewComment.style.textOverflow = 'ellipsis';
            reviewComment.reviewComment.style.whiteSpace = 'nowrap';
            showHideContentButton.querySelector('img').src = `${baseUrl}/svg/chevron-down.svg`;
            reviewComment.isOpened = false;
        } else {
            reviewComment.reviewComment.classList.remove('overflow-hidden');
            reviewComment.reviewComment.style.textOverflow = 'initial';
            reviewComment.reviewComment.style.whiteSpace = 'initial';
            showHideContentButton.querySelector('img').src = `${baseUrl}/svg/chevron-up.svg`;
            reviewComment.isOpened = true;
        }
    });
});

function isOverflown(element) {
    return element.scrollHeight > element.clientHeight || element.scrollWidth > element.clientWidth;
}

//disable new line when pressing enter in textarea
const textareas = document.querySelectorAll('textarea');
textareas.forEach(textarea => {
    textarea.addEventListener('keydown', function(e) {
        if (e.key === 'Enter') {
            e.preventDefault();
        }
    });
});