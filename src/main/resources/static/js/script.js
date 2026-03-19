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