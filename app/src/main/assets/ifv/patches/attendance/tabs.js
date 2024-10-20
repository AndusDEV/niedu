const selector = document.createElement("div")
selector.innerHTML =
    '<button class="MuiButtonBase-root MuiButton-root MuiButton-contained default-button primary-button" disabled><span class="MuiButton-label">Frekwencja</span></button><button class="MuiButtonBase-root MuiButton-root MuiButton-contained default-button primary-button"><span class="MuiButton-label">Statystyki</span></button>';
selector.classList.add("attendance-tabs")

const createSelector = () => {
    document.querySelector("h1").replaceWith(selector)
    changeStatsVisibility(false)
}

const changeStatsVisibility = (isStatsVisible) => {
    const mainStats = document.querySelector(".content-container:has(.statistics)")
    mainStats.style.display = isStatsVisible ? "block" : "none"
    const element = document.querySelector(".tabsview")
    element.classList.add("attendance-init")
    if (isStatsVisible) element.classList.add("stats")
    else element.classList.remove("stats")
}

selector.querySelector("button:first-of-type").addEventListener("click", () => {
    changeStatsVisibility(false)

    selector.querySelector("button:first-of-type").disabled = true
    selector.querySelector("button:last-of-type").disabled = false
})

selector.querySelector("button:last-of-type").addEventListener("click", () => {
    changeStatsVisibility(true)

    selector.querySelector("button:first-of-type").disabled = false
    selector.querySelector("button:last-of-type").disabled = true
})

const isAttendancePage = () => window.location.pathname.endsWith("frekwencja")

const isRendered = () =>
    !!document.querySelector(".content-container:has(.statistics)")
        && !!document.querySelector(".tabsview")
        && !!document.querySelector("h1")

document.addEventListener("DOMContentLoaded", (event) => {
    if (isAttendancePage) {
        createSelector()
    }
});
