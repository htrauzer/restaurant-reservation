let selectedHour = null;

/**

 * @param {HTMLElement} element 
 * @param {number} hour 
    */

function selectTime(element, hour) {
    // 1. Visual selection in Timetable
    document.querySelectorAll('.time-slot').forEach(s => s.classList.remove('active'));
    element.classList.add('active');
    
    // 2. Fetch occupied tables from server
    fetch(`/api/reservations/occupied?hour=${hour}`)
    .then(response => response.json())
    .then(occupiedTableIds => {
        // 3. Update every table color
        const allTables = document.querySelectorAll('.table-item');
        
        allTables.forEach(tableDiv => {
            const tableId = parseInt(tableDiv.getAttribute('data-id'));
            
            // First, reset to green (available)
            tableDiv.style.backgroundColor = "#2ecc71"; // Green
            tableDiv.classList.remove('status-reserved');
            tableDiv.style.pointerEvents = "auto"; // Enable clicking

            // If table ID is in the "busy" list from server, make it gray
            if (occupiedTableIds.includes(tableId)) {
                tableDiv.style.backgroundColor = "#95a5a6"; // Gray
                tableDiv.classList.add('status-reserved');
                tableDiv.style.pointerEvents = "none"; // Disable clicking
            }
        });
    });
}
