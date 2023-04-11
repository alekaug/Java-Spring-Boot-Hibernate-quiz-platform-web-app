// TODO: If modal visible, overflow hidden; else auto
// TODO: Tracking modal state (open/closed) with global variable


addEventListener('DOMContentLoaded', () => {
    class Modal {
        modalContainer = document.getElementById('modal-container');
        modalClose = this.modalContainer.querySelector('#close-button');
        modalTitle = this.modalContainer.querySelector('#modal-title');
        modalContent = this.modalContainer.querySelector('#modal-content');

        constructor(isVisible) { // title: string, content: object
            if (Modal.exists)
                return Modal.instance;
            // initialize fields
            Modal.exists = true;
            Modal.instance = this;
            Modal.instance.setVisible(isVisible);

            // Set close button click callback
            this.modalClose.addEventListener('click', this.close);

            return this;
        }

        show = (title, content) => {
            this.setVisible(true);
            this.modalTitle.innerText = title;
            // Content is made of message, and available buttons (?)
            this.modalContent.innerText = content.message;
        }

        setVisible = (bool) => {
            if (bool === this.visible)
                return;
            this.visible = bool;
            this.modalContainer.opacity = this.visible ? 1 : 0;
            this.modalContainer.hidden = !this.visible;
        }

        close = () => {
            Modal.instance.setVisible(false);
        }
    }

    const questionContentTextbox = document.getElementById('question-content');
    const questionTypeContentDropdown = document.getElementById('question-type');
    const addQuestionButton = document.getElementById('add-question-button');
    const removeQuestionsButton = document.getElementById('remove-questions-button');
    const quizPreviewButton = document.getElementById('quiz-preview-button');
    const questionContentError = document.getElementById('content-error');

    const questions = document.querySelectorAll('.question');
    questions.forEach(e => {
        // Event set for deleting question button
        e.querySelector('#questions-remove-button').addEventListener('click', async e => {
            const parentNode = e.target.parentNode;
            if (parentNode == null)
                return;
            const questionId = parentNode.getAttribute('data-id');
            const response = await fetch(`/api/questions/manage?id=${questionId}`, {
                method: 'DELETE'
            });
            const result = await response.json();
            if (result === true) {
                modal.show('Success!', { message: 'Question removed successfully!' });
                const parentNode = e.target.parentNode;
                if (parentNode != null)
                    parentNode.remove();
            }
            else
                modal.show('Warning.', { message: 'There was an issue deleting a question' });
        });

        // TODO: Implement
        e.querySelector('#questions-edit-button').addEventListener('click', () => {

        });
    });

    // Modal window initialization
    const modal = new Modal(false);

    // Containers
    const closedQuestionContainer = document.getElementById('closed-questions-container');
    const wordsJumbleQuestionContainer = document.getElementById('words-jumble-questions-container');

    // Utility functions
    const hide = (element) => element.hidden = true;
    const unhide = (element) => element.hidden = false;

    const checkFormValidity = () => {
        const state = questionContentTextbox.validity.valid;
        addQuestionButton.disabled = !state;
        questionContentError.hidden = state;
    };

    const checkQuestionType = () => {
        switch (questionTypeContentDropdown.value) {
            case 'closed':
                hide(wordsJumbleQuestionContainer);
                unhide(closedQuestionContainer);
                break;
            case 'words-jumble':
                unhide(wordsJumbleQuestionContainer);
                hide(closedQuestionContainer);
                break;
            case 'opened':
            default:
                hide(wordsJumbleQuestionContainer);
                hide(closedQuestionContainer);
                break;
        }
    };
    checkFormValidity();
    checkQuestionType();

    questionContentTextbox.addEventListener('keyup', checkFormValidity);
    questionTypeContentDropdown.addEventListener('change', checkQuestionType);

    const submitQuestion = () => {
        const content = questionContentTextbox.value;
        const type = questionTypeContentDropdown.value !== 'closed';
        const answers = [
            {
                "content": "Me, having correct answer.",
                "correct": true
            },
            {
                "content": "This answers is also correct.",
                "correct": true
            },
            {
                'content': 'This answer is absolutely incorrect. Don\'t you dare touch that button.',
                'correct': false
            }
        ];
        fetch('/api/questions/manage', {
            method: 'PUT',
            headers: {
                'Accept': 'application/json',
                'Content-Type': 'application/json'
            },
            body: JSON.stringify({
                'content': content,
                'type': type,
                'answers': answers
            })
        })
            .then(response => response.json())
            .then(() => location.reload());

    };
    addQuestionButton.addEventListener('click', submitQuestion);

    const removeAllQuestions = async () => {
        const response = await fetch('/api/questions/manage/purge', {
            method: 'DELETE'
        });
        const result = await response.json();
        if (result > 0) {
            document.querySelectorAll('.question').forEach(el => el.remove());
            modal.show('Success!', { message: `${result} questions were deleted successfully.` });
        }
        else
            modal.show('Warning', { message: 'None of the questions were deleted.' });
    };
    removeQuestionsButton.addEventListener('click', removeAllQuestions);

    quizPreviewButton.addEventListener('click', () => location.href = '/quiz'); // TODO: Set location to '/manager/preview'
});