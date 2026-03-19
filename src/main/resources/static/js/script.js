// Function, that is called when the user releases a table
function updateTableOnServer(tableId, newX, newY) {
    fetch(`/api/admin/tables/${tableId}/position?x=${newX}&y=${newY}`, {
        method: 'PATCH'
    })
    .then(response => {
        if (response.ok) {
            console.log("Position of the table saved!");
        }
    });
}

// Example of handling drag end event
document.querySelectorAll('.table-icon').forEach(table => {
    table.addEventListener('dragend', (e) => {
        const id = table.dataset.id;
        const x = e.clientX;
        const y = e.clientY;
        
        // Visual movement
        table.style.left = x + 'px';
        table.style.top = y + 'px';
        
        // Send to server
        updateTableOnServer(id, x, y);
    });
});

function filterTables() {
    const guests = document.getElementById('guestCount').value;
    const date = document.getElementById('bookingDate').value;
    const time = document.getElementById('bookingTime').value;
    const zone = document.getElementById('zoneSelect').value;

    fetch(`/api/tables/search?guests=${guests}&date=${date}&time=${time}&zone=${zone}`)
    .then(res => res.json())
    .then(availableTables => {
        // Скидаємо підсвітку всіх столів
        document.querySelectorAll('.table-item').forEach(t => t.style.border = "none");
        
        // Підсвічуємо рекомендовані столи (зеленим)
        availableTables.forEach(table => {
            const tableElement = document.querySelector(`[data-id="${table.id}"]`);
            if (tableElement) tableElement.style.border = "3px solid #2ecc71";
        });
    });
}