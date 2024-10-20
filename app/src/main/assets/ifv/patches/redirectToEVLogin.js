function redirectToLoginPage() {
    window.location.pathname = "/logowanie";
}

document.addEventListener("DOMContentLoaded", (event) => {
    if (window.location.hostname === "eduvulcan.pl" && window.location.pathname === "/" && !!document.querySelector("#panelLoginButton")) {
        redirectToLoginPage()
    }
});
