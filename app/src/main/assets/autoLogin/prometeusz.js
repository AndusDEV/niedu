const aliasField = document.getElementById('Alias');
const passwordField = document.getElementById('Password');

if (localStorage.getItem('Alias') !== null && localStorage.getItem('Password') !== null) {
    aliasField.value = localStorage.getItem('Alias');
    passwordField.value = localStorage.getItem('Password');
    document.getElementById('btLogOn').click();

    if (isErrorMessageVisible()) {
        localStorage.removeItem('Alias');
        localStorage.removeItem('Password');
    }
} else {
    aliasField.addEventListener('input', () => {
        localStorage.setItem('Alias', aliasField.value);
    });
    passwordField.addEventListener('input', () => {
        localStorage.setItem('Password', passwordField.value);
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