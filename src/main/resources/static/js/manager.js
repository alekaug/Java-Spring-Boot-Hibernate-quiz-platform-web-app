addEventListener('DOMContentLoaded', () => {
    const PURGE_URL = '/api/questions/manage/purge';
    const MANAGE_URL = '/api/questions/manage';

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

        show = () => {
            this.setVisible(true);
            document.body.style.overflow = 'hidden';
        }

        showContent = (title, content) => {
            this.modalTitle.innerText = title;
            // Content is made of message, and available buttons (?)
            this.modalContent.innerText = content.message;
            Modal.instance.show();
        }

        showHTML = (html) => {
            this.modalContent.innerHTML = html;
            Modal.instance.show();
        }

        setVisible = (bool) => {
            this.visible = bool;
            this.modalContainer.opacity = this.visible ? 1 : 0;
            this.modalContainer.hidden = !this.visible;
        }

        close = () => {
            Modal.instance.setVisible(false);
            document.body.style.overflow = 'auto';
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
            const response = await fetch(`${MANAGE_URL}?id=${questionId}`, {
                method: 'DELETE'
            });
            const result = await response.json();
            if (result === true) {
                modal.showContent('Success!', { message: 'Question removed successfully!' });
                const parentNode = e.target.parentNode;
                if (parentNode != null)
                    parentNode.remove();
            }
            else
                modal.showContent('Warning.', { message: 'There was an issue deleting a question. Try refreshing this page.' });
        });
    });

    // Modal window initialization
    const modal = new Modal(false);

    // Containers
    const closedQuestionContainer = document.getElementById('closed-questions-container');

    // Utility functions
    const hide = (element) => element.hidden = true;
    const unhide = (element) => element.hidden = false;

    // const checkFormValidity = () => {
    //     const state = questionContentTextbox.validity.valid;
    //     addQuestionButton.disabled = !state;
    //     questionContentError.hidden = state;
    // };

    const checkQuestionType = () => {
        switch (questionTypeContentDropdown.value) {
            case 'opened':
                hide(closedQuestionContainer);
                break;
            case 'closed-s':
            case 'closed-m':
            default:
                unhide(closedQuestionContainer);
                break;
        }
    };
    // checkFormValidity();
    checkQuestionType();

    // questionContentTextbox.addEventListener('keyup', checkFormValidity);
    questionTypeContentDropdown.addEventListener('change', checkQuestionType);

    const submitQuestion = (e) => {
        e.preventDefault();
        const content = questionContentTextbox.value;
        const selectVal = questionTypeContentDropdown.value;
        const type = selectVal === 'opened' ? 0 : selectVal === 'closed-s' ? 1 : 2;
        var answers = [];
        if (type === 1) {
            const answersArray = document.querySelectorAll('.new-answer-element');
            // Only one correct answer validation
            var correctAmount = 0;
            answersArray.forEach(a => {
                const content = a.querySelector('input[name="answer-content"]').value;
                const correct = a.querySelector('input[name="correct"]').checked;
                if (correct === true) correctAmount++;
                if (correctAmount > 1) return;
                answers.push({"content": content, "correct": correct});
            });
            if (correctAmount !== 1) return;
            /*answers = [
                {
                    "content": "Me, having only correct answer.",
                    "correct": true
                },
                {
                    "content": "This answers is not correct.",
                    "correct": false
                },
                {
                    'content': 'This answer is absolutely incorrect. Don\'t you dare touch that button.',
                    'correct': false
                }
            ];*/
        }
        else if (type === 2) {
            const answersArray = document.querySelectorAll('.new-answer-element');
            // Only one correct answer validation
            var correctAmount = 0;
            answersArray.forEach(a => {
                const content = a.querySelector('input[name="answer-content"]').value;
                const correct = a.querySelector('input[name="correct"]').checked;
                if (correct === true) correctAmount++;
                answers.push({"content": content, "correct": correct});
            });
            if (correctAmount < 2) return;

            /*answers = [
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
            ];*/
        }
        fetch(MANAGE_URL, {
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
    const removeAllQuestions = async () => {

        const response = await fetch(PURGE_URL, {
            method: 'DELETE'
        });
        location.reload();
        return; // TODO: Temporary solution, resolve the way it ends

        const result = await response.json();
        if (result > 0) {
            document.querySelectorAll('.question').forEach(el => el.remove());
            modal.showContent('Success!', { message: `${result} questions were deleted successfully.` });
        }
        else
            modal.showContent('Warning', { message: 'None of the questions were deleted.' });
    };
    removeQuestionsButton.addEventListener('click', removeAllQuestions);

    quizPreviewButton.addEventListener('click', () => location.href = '/'); // TODO: Set location to '/manager/preview'

    // Question creator
    const questionCreatorHTML = `
        <div id="question-creator">
                <div>
                    Question content:
                    <label>
                        <input name="question-content" type="text">
                    </label>
                </div>
                <div>
                    Question type:
                    <label>
                        <select id="question-type" name="question-type">
                            <option name="0">Open</option>
                            <option name="1">Closed (single)</option>
                            <option name="2">Closed (multi)</option>
                        </select>
                    </label>
                </div>
                <div>
                    Possible answers:
                    <label>
                        <input name="correct" type="checkbox">
                    </label>
                    <label>
                        <input name="answer-content" type="text">
                    </label>
                    <button>+</button>
                </div>
                <input type="submit" value="Create">
            </div>
    `;
    const openQuestionCreator = () => {
        modal.showHTML(questionCreatorHTML);
    };
    // addQuestionButton.addEventListener('click', openQuestionCreator);
    addQuestionButton.addEventListener('click', submitQuestion);

    /* New question creator */
    const MIN_ANSWERS_AMOUNT = 1;
    const MAX_ANSWERS_AMOUNT = 10;
    const answersList = document.getElementById('answers-list');
    const newAnswerButton = document.getElementById('new-answer-button');
    const deleteAnswerButton = document.getElementById('delete-answer-button');
    newAnswerButton.addEventListener('click', (e) => {
        e.preventDefault();

        const newElement = document.createElement('fieldset');
        newElement.classList.add('new-answer-element');
        newElement.setAttribute('name', 'answer[]');
        const answerHTML = '<label>Answer content<span class="required"></span><input name="answer-content" type="text"></label><label><input name="correct" type="checkbox">&nbsp;Is correct?<span class="required"></span></label>';
        newElement.innerHTML = answerHTML;

        if (answersList.childElementCount >= MAX_ANSWERS_AMOUNT - 1) {
            answersList.append(newElement);
            newAnswerButton.disabled = true;
            return;
        }
        answersList.append(newElement);
        deleteAnswerButton.disabled = false;
    });

    deleteAnswerButton.addEventListener('click', (e) => {
        e.preventDefault();

        if (answersList.childElementCount === MIN_ANSWERS_AMOUNT + 1) {
            answersList.lastChild.remove();
            deleteAnswerButton.disabled = true;
            return;
        }
        if (answersList.childElementCount <= MIN_ANSWERS_AMOUNT) return;
        answersList.lastChild.remove();
        if (answersList.childElementCount < MAX_ANSWERS_AMOUNT)
            newAnswerButton.disabled = false;

    });
});