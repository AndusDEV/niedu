const loginField = document.getElementById('Login');
const hasloField = document.getElementById('Haslo');
const loginButton = document.getElementById('btLogOn');

if (localStorage.getItem('Login') != null && localStorage.getItem('Haslo') != null && sessionStorage.getItem('triedLoggingIn') === null) {
	loginField.value = localStorage.getItem('Login');
	hasloField.value = localStorage.getItem('Haslo');
	loginButton.click();
	sessionStorage.setItem('triedLoggingIn', '1');
} else {
	if (sessionStorage.getItem('triedLoggingIn') === '1') {
		document.querySelectorAll('.message-error').forEach(e => {
			e.innerHTML = "Wygląda na to, że niedu ma zapisane błędne dane logowania lub Vulcan wymaga uzupełnienia Captchy. Proszę zalogować się ręcznie.";
		});
	}
	loginField.addEventListener('input', () => {
		localStorage.setItem('Login', loginField.value);
	});
	hasloField.addEventListener('input', () => {
		localStorage.setItem('Haslo', hasloField.value);
	});
}