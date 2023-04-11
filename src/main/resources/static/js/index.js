addEventListener('DOMContentLoaded', () => {
    // Current placeholder name element
    const currentName = document.getElementById('name');
    const nextButton = document.getElementById('next-button');
    nextButton.disabled = true;

    // Names' array
    const names = ['Piotr',
        'Ania',
        'Paweł',
        'Marcin',
        'Grzesiek',
        'Katarzyna',
        'Przemek',
        'Krzysztof',
        'Kajetan',
        'Magdalena',
        'Kuba',
        'Michał',
        'Jan',
        'Franek',
        'Andrzej'];

    // Random name function for the placeholder element
    const randomName = (current) => {
        next = names[Math.floor(Math.random() * names.length)];
        while (current === next) {
            next = names[Math.floor(Math.random() * names.length)];
        }
        currentName.placeholder = next;
        setTimeout(randomName, 0b1011101110, next);
    }

    // Random name placeholder value initialization
    randomName(names[0]);

    // Test question to be added
    const addQuestion = function (content, type, answers) {
        const jsonObject = {
            'content': content,
            'type': type,
            'answers': answers
        };
        console.log('Initial value: ' + jsonObject);
        return jsonObject;
    }

    currentName.addEventListener('keyup', () =>{
        nextButton.disabled = !currentName.validity.valid;
    });

    nextButton.addEventListener('click', () => {
        // TODO: Add new user's session
        location.href = '/quiz';
    });

    // Adding click event listener with a question to be sent
    /*
    nextButton.addEventListener('click', () => {
        fetch('/api/questions/manage', {
            method: 'PUT',
            headers: {
                'Accept': 'application/json',
                'Content-Type': 'application/json'
            },
            body: JSON.stringify(addQuestion('Whatever?', true, []))
        })
            .then(response => response.json())
            .then(response => console.log('Received response: ' + JSON.stringify(response)));

        const currentNameVal = currentName.value;
        location.href = `?name=${currentNameVal === '' ? 'whoever' : currentNameVal}`;
    });
    */
});

