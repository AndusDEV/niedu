if (window.location.href.includes('logowanie')) {
	fillLoginForm();
} else {
	autoSelectAccountIfOnlyOnePresent();
}

function fillLoginForm() {
	const aliasField = document.getElementById('Alias');
	const passwordField = document.getElementById('Password');
	const loginButton = document.getElementById('btLogOn');
	const triedLoggingIn = sessionStorage.getItem('triedLoggingIn');

	browser.storage.local.get().then((result) => {
		const alias = result?.Alias;
		const password = result?.Password;

		if (alias !== undefined && password !== undefined && triedLoggingIn === null) {
			aliasField.value = alias;
			passwordField.value = password;
			loginButton.click();
			sessionStorage.setItem("triedLoggingIn", "1");
		} else {
			if (triedLoggingIn === "1") {
				document.querySelectorAll('.message-error').forEach(e => {
					e.innerHTML = "Wygląda na to, że niedu ma zapisane błędne dane logowania lub Vulcan wymaga uzupełnienia Captchy. Proszę zalogować się ręcznie.";
				});
			}
			aliasField.addEventListener('input', () => {
				browser.storage.local.set({ "Alias": aliasField.value });
			});
			passwordField.addEventListener('input', () => {
				browser.storage.local.set({ "Password": passwordField.value });
			});
		}
	});
}

function autoSelectAccountIfOnlyOnePresent() {
	if (document.querySelectorAll('a.connected-account').length === 1) {
		document.querySelectorAll('a.connected-account')[0].click();
	}
}