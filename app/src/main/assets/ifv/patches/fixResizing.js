const startingWidth = window.innerWidth

const startListening = () => {
    addEventListener("resize", () => {
        if (startingWidth < 1024 !== window.innerWidth < 1024) window.location.reload()
    });
}

document.addEventListener("DOMContentLoaded", (event) => {
    if (window.location.hostname !== "eduvulcan.pl") {
        startListening()
    }
});
