addEventListener('DOMContentLoaded', () => {
    const dialog = document.querySelector('dialog');
    dialog.show();
    document.body.style.overflow = 'hidden';
    const continueButton = dialog.querySelector('button');
    continueButton.addEventListener('click', () => {
        dialog.close('OK.');
        document.body.style.overflow = 'auto';
    });
});
