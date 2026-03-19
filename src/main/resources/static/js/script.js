// Global variable to store the selected table ID
let currentTable = null;

/**
 * Sends filter criteria to the server and highlights available tables
 */
function filterTables() {
    const guests = document.getElementById('guestCount').value;
    const date = document.getElementById('bookingDate').value;
    const time = document.getElementById('bookingTime').value;
    const zone = document.getElementById('zoneSelect').value;

    // Fetch call to the search API endpoint
    fetch(`/api/tables/search?guests=${guests}&date=${date}&time=${time}&zone=${zone}`)
    .then(res => res.json())
    .then(data => {
        // Reset styles for all tables before highlighting
        document.querySelectorAll('.table-item').forEach(t => {
            t.style.outline = "none";
            t.style.boxShadow = "0 3px 6px rgba(0,0,0,0.16)";
        });
        
        // Highlight tables returned by the filter logic
        data.forEach(table => {
            const el = document.querySelector(`[data-id="${table.id}"]`);
            if (el) {
                el.style.outline = "4px solid #2ecc71";
                el.style.boxShadow = "0 0 20px #2ecc71";
            }
        });
    });
}

/**
 * Opens the modal and sets the current table ID
 */
function openBooking(el) {
    currentTable = el.getAttribute('data-id');
    document.getElementById('modalId').innerText = currentTable;
    document.getElementById('bookingModal').style.display = 'block';
}

function closeModal() {
    document.getElementById('bookingModal').style.display = 'none';
}

/**
 * Validates input and simulates booking submission
 */
function confirmBooking() {
    const name = document.getElementById('userName').value;
    if (!name) return alert("Name is required!");

    // Log the action (integration with POST /book should go here)
    console.log(`Booking request for table ${currentTable} by user ${name}`);
    closeModal();
    alert("Table reserved successfully!");
}