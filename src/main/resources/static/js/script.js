function updateTableVisuals() {
    
    const activeBtn = document.querySelector('.hour-btn.active');
    const selectedHour = parseInt(activeBtn ? activeBtn.innerText : '12');

    const filters = {
        window: document.getElementById('pref-window').checked,
        corner: document.getElementById('pref-corner').checked,
        divan:  document.getElementById('pref-divan').checked
    };

    const selectedRadio = document.querySelector('input[name="seatCount"]:checked');
    const selectedSeats = selectedRadio ? parseInt(selectedRadio.value) : null;

    document.querySelectorAll('.table-div').forEach(table => {
    
    // --- 1. COLOR LOGIC (Booking Availability) ---
    const rawSlots = table.getAttribute('data-slots') || "[]";
    const bookedSlots = rawSlots.replace(/[\[\]]/g, '').split(',').map(s => parseInt(s.trim()));
    const isBusy = bookedSlots.includes(selectedHour);
    
    // This makes the table Green if free, Gray if busy
    table.style.backgroundColor = isBusy ? "#95a5a6" : "#2ecc71";

    // --- 2. FILTER LOGIC (Opacity) ---
    // A. Preferences (Window/Corner/Divan)
    const matchesPrefs = (!filters.window || table.dataset.window === 'true') &&
                         (!filters.corner || table.dataset.corner === 'true') &&
                         (!filters.divan  || table.dataset.divan  === 'true');

    // B. Seats Logic
    const tableSeats = parseInt(table.dataset.seats || "0");
    let matchesSeats = true;
    if (selectedSeats) {
        if ([3, 4].includes(selectedSeats) && tableSeats === 2) matchesSeats = false;
        else if ([5, 6].includes(selectedSeats) && (tableSeats === 2 || tableSeats === 4)) matchesSeats = false;
        else if ([7, 8].includes(selectedSeats) && tableSeats !== 8) matchesSeats = false;
        else if (selectedSeats > 8) matchesSeats = false;
    }

    // --- 3. APPLY BOTH ---
    const allMatch = matchesPrefs && matchesSeats;
    table.style.opacity = allMatch ? "1" : "0.2";
});

    const msgDiv = document.getElementById('booking-message');
    const selectedVal = document.querySelector('input[name="seatCount"]:checked')?.value;
    
    if (selectedVal >= 9 && selectedVal <= 14) {
        msgDiv.innerText = "For big companies tables can be combined.";
    } else if (selectedVal === 'more') {
        msgDiv.innerText = "For events or big groups of people you can rent the Main Hall or Terasse, please contact the manager.";
    } else {
        msgDiv.innerText = "";
    }
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