if (window.location.href.includes('logowanie')) {
	fillLoginForm();
} else {
	autoSelectAccountIfOnlyOnePresent();
}

function fillLoginForm() {
	const aliasField = document.getElementById('Alias');
	const passwordField = document.getElementById('Password');
	const loginButton = document.getElementById('btLogOn');

	if (localStorage.getItem('Alias') != null && localStorage.getItem('Password') != null && sessionStorage.getItem('triedLoggingIn') === null) {
		aliasField.value = localStorage.getItem('Alias');
		passwordField.value = localStorage.getItem('Password');
		loginButton.click();
		sessionStorage.setItem('triedLoggingIn', '1');
	} else {
		if (sessionStorage.getItem('triedLoggingIn') === '1') {
			document.querySelectorAll('.message-error').forEach(e => {
				e.innerHTML = "Wygląda na to, że niedu ma zapisane błędne dane logowania lub Vulcan wymaga uzupełnienia Captchy. Proszę zalogować się ręcznie.";
			});
		}
		aliasField.addEventListener('input', () => {
			localStorage.setItem('Alias', aliasField.value);
		});
		passwordField.addEventListener('input', () => {
			localStorage.setItem('Password', passwordField.value);
		});
	}
}

function autoSelectAccountIfOnlyOnePresent() {
	if (document.querySelectorAll('a.connected-account').length === 1) {
		document.querySelectorAll('a.connected-account')[0].click();
	}
}