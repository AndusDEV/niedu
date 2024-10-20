const BACK_ICON_URL = "https://raw.githubusercontent.com/banocean/ifv/new-navbar/assets/icons/keyboard_backspace_24dp_E8EAED_FILL0_wght400_GRAD0_opsz24.svg";
const navIcons = {
    "tablica": "dashboard",
    "oceny": "counter_6",
    "frekwencja": "event_available",
    "planZajec": "calendar_clock"
};

async function runMobileNavbar() {
    const nav = document.createElement("nav");
    nav.classList.add("bottom-navigation-bar");

    const more = document.createElement("div");
    more.classList.add("more-popup", "list-modal");
    more.innerHTML = `<div><img src='${BACK_ICON_URL}'><h1>Więcej</h1></div><div></div>`;
    more.style.display = "none";

    more.querySelector("img").addEventListener("click", () => {
        more.style.display = "none";
        setHighlights();
    });

    await getFromAside(() => null);  // We need aside to just load
    await waitForRender(() => getPages().length > 1);

    const navPages = ["tablica", "oceny", "frekwencja", "planZajec"];
    const pages = getPages();
    
    // Populate the navigation bar and more menu
    for (const page of pages) {
        const itemClass = Array.from(page.element.classList).find((c) => !["MuiListItem-root", "MuiListItem-gutters", "selected"].includes(c));
        const item = document.createElement("div");

        if (!navPages.includes(itemClass)) {
            item.innerHTML = "<div class='icon'></div><span class='name'></span>";
            item.querySelector(".icon").style.content = page.icon;
            item.querySelector(".name").innerText = page.name;
            more.querySelector("div:last-of-type").appendChild(item);
        } else {
            item.innerHTML = `<div><img src="https://raw.githubusercontent.com/banocean/ifv/new-navbar/assets/icons/${navIcons[itemClass]}_24dp_E8EAED_FILL0_wght400_GRAD0_opsz24.svg"></div><div></div>`;
            item.querySelector("div:last-of-type").innerText = page.name;
            nav.appendChild(item);
        }
    }

    const moreButton = document.createElement("div");
    moreButton.innerHTML = `
        <div><img src="https://raw.githubusercontent.com/banocean/ifv/new-navbar/assets/icons/menu_24dp_E8EAED_FILL0_wght400_GRAD0_opsz24.svg"></div>
        <div>Więcej</div>
    `;
    moreButton.addEventListener("click", () => {
        more.style.display = "block";
        setHighlights();
    });

    nav.appendChild(moreButton);
    document.body.appendChild(nav);
    document.body.appendChild(more);
}

document.addEventListener("DOMContentLoaded", () => {
    if (window.location.hostname.match(/^(dziennik-)?(uczen).*/) && window.innerWidth < 1024) {
        runMobileNavbar();
    }
});
