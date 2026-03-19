let selectedHour = null;

/**
 * Handle time selection from the left timetable
 * @param {HTMLElement} element - The clicked DOM element
 * @param {number} hour - The hour value (12-23)
 */
function selectTime(element, hour) {
    // English comment: UI feedback for selection
    document.querySelectorAll('.time-slot').forEach(s => s.classList.remove('active'));
    element.classList.add('active');
    
    selectedHour = hour;
    
    // English comment: After selecting time, update table statuses
    updateTableStatuses();
}

/**
 * Main filter logic
 */
function updateTableStatuses() {
    if (!selectedHour) return;

    const guests = document.getElementById('guestCount').value;
    const zone = document.querySelector('input[name="zone"]:checked').value;

    fetch(`/api/tables/search?guests=${guests}&zone=${zone}&time=${selectedHour}`)
    .then(res => res.json())
    .then(recommended => {
        // English comment: Set all tables to GREEN (available) by default when time is picked
        document.querySelectorAll('.table-item').forEach(t => {
            t.style.backgroundColor = "#2ecc71"; // Green
            t.style.boxShadow = "none";
        });

        // English comment: Highlight recommended ones in YELLOW
        recommended.forEach(table => {
            const el = document.querySelector(`[data-id="${table.id}"]`);
            if (el) {
                el.style.backgroundColor = "#f1c40f"; // Yellow
                el.style.boxShadow = "0 0 15px #f1c40f";
            }
        });
    });
}