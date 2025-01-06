fillLoginForm();

function fillLoginForm() {
	const loginField = document.getElementById('Login');
	const hasloField = document.getElementById('Haslo');
	const loginButton = document.getElementById('btLogOn');
	const triedLoggingIn = sessionStorage.getItem('triedLoggingIn');

	browser.storage.local.get().then((result) => {
		const login = result?.Login;
		const haslo = result?.Haslo;

		if (login !== undefined && haslo !== undefined && triedLoggingIn === null) {
			loginField.value = login;
			hasloField.value = haslo;
			loginButton.click();
			sessionStorage.setItem("triedLoggingIn", "1");
		} else {
			if (triedLoggingIn === "1") {
				document.querySelectorAll('.message-error').forEach(e => {
					e.innerHTML = "Wygląda na to, że niedu ma zapisane błędne dane logowania lub Vulcan wymaga uzupełnienia Captchy. Proszę zalogować się ręcznie.";
				});
			}
			loginField.addEventListener('input', () => {
				browser.storage.local.set({ "Login": loginField.value });
			});
			hasloField.addEventListener('input', () => {
				browser.storage.local.set({ "Haslo": hasloField.value });
			});
		}
	});
}