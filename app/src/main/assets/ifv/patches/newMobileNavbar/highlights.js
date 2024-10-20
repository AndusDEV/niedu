const setHighlights = () => {
    let i;
    if (document.querySelector(".more-popup").style.display === "block") i = 4
    else if (window.location.pathname.endsWith("tablica")) i = 0
    else if (window.location.pathname.endsWith("oceny")) i = 1
    else if (window.location.pathname.endsWith("frekwencja")) i = 2
    else if (window.location.pathname.endsWith("planZajec")) i = 3

    const buttons = Array.from(document.querySelector(".bottom-navigation-bar").children)
    for (let j = 0; j < buttons.length; j++) {
        const button = buttons[j]
        const img = button.querySelector("div > img");
        if (i === j) img.classList.add("highlight")
        else img.classList.remove("highlight")
    }
}

document.addEventListener("DOMContentLoaded", (event) => {
    if (window.location.hostname.match(/^(dziennik-)?(uczen).*/) && window.innerWidth < 1024) {
        setHighlights()
    }
});