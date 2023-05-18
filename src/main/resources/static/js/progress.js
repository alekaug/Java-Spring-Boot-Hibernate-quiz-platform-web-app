'use strict';
/* Quiz Progress Bar Script */
addEventListener('DOMContentLoaded', () => {
    const progressBar = document.querySelector('progress');
    var height = document.documentElement.scrollHeight - document.documentElement.clientHeight;
    if (height === 0) progressBar.hidden = true;
    const updateProgress = () => {
        height = document.documentElement.scrollHeight - document.documentElement.clientHeight;
        if (height === 0) {
            progressBar.hidden = true;
            return;
        }
        progressBar.hidden = false;
        const scrollAmount = document.body.scrollTop || document.documentElement.scrollTop;
        const scrolled = (scrollAmount / height);
        progressBar.value = scrolled;
    };
    updateProgress();
    document.addEventListener('scroll', updateProgress, true);
});