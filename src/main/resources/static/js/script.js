function updateTableVisuals() {
    // 1. Get State
    const activeBtn = document.querySelector('.hour-btn.active');
    const hour = parseInt(activeBtn ? activeBtn.innerText : '12');
    const nextHour = hour + 1;

    // 2. Get Filter Preferences
    const prefs = {
        window: document.getElementById('pref-window').checked,
        corner: document.getElementById('pref-corner').checked,
        divan:  document.getElementById('pref-divan').checked
    };

    // 3. Apply logic to all tables
    document.querySelectorAll('.table-div').forEach(table => {
        const bookedSlots = table.getAttribute('data-slots');
        const isBusy = bookedSlots.includes(hour.toString()) || 
                       bookedSlots.includes(nextHour.toString());

        // Check matching prefs (only if box is checked)
        const matches = (!prefs.window || table.dataset.window === 'true') &&
                        (!prefs.corner || table.dataset.corner === 'true') &&
                        (!prefs.divan  || table.dataset.divan  === 'true');

        // Visual logic: Dim if not matching, then gray if busy, green if free
        if (!matches) {
            table.style.opacity = "0.2";
            table.style.backgroundColor = "#bdc3c7";
        } else {
            table.style.opacity = "1";
            table.style.backgroundColor = isBusy ? "#95a5a6" : "#2ecc71";
        }
    });
}

function filterByTime(event, selectedHour) {
    document.querySelectorAll('.hour-btn').forEach(btn => btn.classList.remove('active'));
    event.currentTarget.classList.add('active');
    updateTableVisuals();
}

window.addEventListener('load', () => {
    document.querySelectorAll('.hour-btn')[0]?.classList.add('active');
    updateTableVisuals();
});