function updateTableVisuals() {


    // 1. Get Global State
    const activeBtn = document.querySelector('.hour-btn.active');
    const selectedHour = activeBtn ? parseInt(activeBtn.innerText) : 12;
    const filters = {
        window: document.getElementById('pref-window').checked,
        corner: document.getElementById('pref-corner').checked,
        divan:  document.getElementById('pref-divan').checked
    };
    const selectedRadio = document.querySelector('input[name="seatCount"]:checked');
    const selectedSeats = selectedRadio ? parseInt(selectedRadio.value) : null;

    // 2. Recommendation Logic Variables
    let highestScore = -Infinity;
    let bestTable = null;

    // 3. Process each table
    document.querySelectorAll('.table-div').forEach(table => {
        // --- Calculate Busy Status ---
        const rawSlots = table.getAttribute('data-slots') || "[]";
        const bookedSlots = rawSlots.replace(/[\[\]]/g, '').split(',').map(s => parseInt(s.trim()));
        const isBusy = bookedSlots.includes(selectedHour); // <--- Defined inside loop

        // --- Handle Busy Tables ---
        if (isBusy) {
            table.style.backgroundColor = "#95a5a6"; // Gray
            table.style.opacity = "0.5";
            return; // Skip the rest for this table
        }

        // --- Calculate Score ---
        let score = 0;
        const tableSeats = parseInt(table.dataset.seats || "0");
        
        if (filters.window && table.dataset.window === 'true') score += 5;
        if (filters.corner && table.dataset.corner === 'true') score += 5;
        if (filters.divan  && table.dataset.divan  === 'true') score += 5;
        if (selectedSeats && tableSeats > selectedSeats) score -= (tableSeats - selectedSeats);
        if (!isSeatFit(selectedSeats, tableSeats)) score -= 5;

        // --- Reset and Apply Style ---
        table.style.backgroundColor = "#2ecc71"; // Green (Default)
        table.style.opacity = "1";

        if (score > highestScore) {
            highestScore = score;
            bestTable = table;
        }
    });

    // Highlight winner
    if (bestTable) bestTable.style.backgroundColor = "#f1c40f"; // Yellow
}

// 4. Ensure this helper function exists in your file
function isSeatFit(selected, capacity) {
    if (!selected) return true;
    if ([3, 4].includes(selected) && capacity === 2) return false;
    if ([5, 6].includes(selected) && (capacity === 2 || capacity === 4)) return false;
    if ([7, 8].includes(selected) && capacity !== 8) return false;
    return true;
}

    const msgDiv = document.getElementById('booking-message');
    const selectedVal = document.querySelector('input[name="seatCount"]:checked')?.value;
    
    if (selectedVal >= 9 && selectedVal <= 14) {
        msgDiv.innerText = "For big companies tables can be combined.";
    } else if (selectedVal === 'more') {
        msgDiv.innerText = "For events or big groups of people you can rent the Main Hall or Terasse, please contact the manager.";
    } else {
        msgDiv.innerText = "";
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