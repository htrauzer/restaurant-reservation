function updateTableVisuals() {
    // 1. Get State (FIXED: typo was 'aparseInt')
    const activeBtn = document.querySelector('.hour-btn.active');
    const selectedHour = parseInt(activeBtn ? activeBtn.innerText : '12');

    // 2. Get Filter Preferences
    const filters = {
        window: document.getElementById('pref-window').checked,
        corner: document.getElementById('pref-corner').checked,
        divan:  document.getElementById('pref-divan').checked
    };

    // 3. Process each table
    document.querySelectorAll('.table-div').forEach(table => {
        // A. Booking Logic (RANDOMIZED)
        // Handle empty or null data safely
        const rawSlots = table.getAttribute('data-slots') || "";
        const bookedSlots = rawSlots.replace(/[\[\]]/g, '').split(',')
                                    .filter(s => s.trim() !== "")
                                    .map(s => parseInt(s.trim()));
        
        const isBusy = bookedSlots.includes(selectedHour);
        
        // Apply Color (Gray if busy, Green if free)
        table.style.backgroundColor = isBusy ? "#95a5a6" : "#2ecc71";

        // B. Preference Logic (STATIC)
       const matches = (!filters.window || String(table.dataset.window) === 'true') &&
                       (!filters.corner || String(table.dataset.corner) === 'true') &&
                       (!filters.divan  || String(table.dataset.divan)  === 'true');

        // Apply Opacity (1 if it matches filters, 0.2 if not)
        table.style.opacity = matches ? "1" : "0.2";
    });
}

function filterByTime(event, selectedHour) {
    document.querySelectorAll('.hour-btn').forEach(btn => btn.classList.remove('active'));
    event.currentTarget.classList.add('active');
    updateTableVisuals();
}

window.addEventListener('load', () => {
    // Initialize active state
    const firstBtn = document.querySelector('.hour-btn');
    if (firstBtn) firstBtn.classList.add('active');
    updateTableVisuals();
});