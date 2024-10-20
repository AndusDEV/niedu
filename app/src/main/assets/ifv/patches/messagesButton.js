async function move() {
    const inner = await getFromAside(
        async () => {
            await waitForRender(() => document.querySelector(".messages"))
            return document.querySelector(".messages")?.innerHTML
        }
    )

    const messages = document.createElement("div");
    messages.innerHTML = inner;
    messages.style.float = "right";
    messages.style.padding = "20px";
    messages.style.marginLeft = "auto";
    messages.querySelector(
        ".MuiBadge-anchorOriginTopRightRectangle",
    ).style.transitionDuration = "0ms";

    document
        .querySelector(".header_logo_tools-container")
        .appendChild(messages);
}

document.addEventListener("DOMContentLoaded", (event) => {
    if (window.location.hostname.match(/^(dziennik-)?(uczen).*/) && window.innerWidth < 1024) {
        move()
    }
});
