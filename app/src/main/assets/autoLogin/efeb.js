const loginField = document.getElementById('Login');
const hasloField = document.getElementById('Haslo');

if (localStorage.getItem('Login') !== null && localStorage.getItem('Haslo') !== null) {
    loginField.value = localStorage.getItem('Login');
    hasloField.value = localStorage.getItem('Haslo');
    document.getElementById('btLogOn').click();

    if (isErrorMessageVisible()) {
        localStorage.removeItem('Login');
        localStorage.removeItem('Haslo');
    }
} else {
    loginField.addEventListener('input', () => {
        localStorage.setItem('Login', loginField.value);
    });
    hasloField.addEventListener('input', () => {
        localStorage.setItem('Haslo', hasloField.value);
    });
}

function isErrorMessageVisible() {
    document.getElementsByClassName('error-message').forEach(element => {
        if (element.style.display !== 'none') {
            return true;
        }
    });

    return false;
}