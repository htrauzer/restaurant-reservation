export const MapUI = {
    resetTables() {
        document.querySelectorAll('.table-item').forEach(tableDiv => {
            // If the table is just booked (purple), we don't touch it
            if (tableDiv.classList.contains('status-just-booked')) return;

            if (!tableDiv.classList.contains('status-reserved')) {
                tableDiv.style.backgroundColor = "#2ecc71"; // Green
                tableDiv.style.boxShadow = "none";
                tableDiv.style.border = "none";
                tableDiv.style.pointerEvents = "auto";
            }
        });
    },

    updateStatus(occupiedIds, recommendedIds) {
        document.querySelectorAll('.table-item').forEach(tableDiv => {
            if (tableDiv.classList.contains('status-just-booked')) return;

            const id = parseInt(tableDiv.getAttribute('data-id'));
            
            tableDiv.style.backgroundColor = "#2ecc71"; 
            tableDiv.classList.remove('status-reserved');
            tableDiv.style.boxShadow = "none";
            tableDiv.style.border = "none";
            tableDiv.style.pointerEvents = "auto";

            tableDiv.onclick = () => {
                if (!tableDiv.classList.contains('status-reserved')) {
                    window.openBookingModal(id); 
                }
            };

            if (occupiedIds.includes(id)) {
                tableDiv.style.backgroundColor = "#95a5a6"; 
                tableDiv.classList.add('status-reserved');
                tableDiv.style.pointerEvents = "none";
                tableDiv.onclick = null; 
            } 
            else if (recommendedIds.includes(id)) {                
                tableDiv.style.backgroundColor = "#f1c40f"; 
                tableDiv.style.boxShadow = "0 0 15px rgba(241, 196, 15, 0.8)";
            }
        });
    },
    
    highlightFound(tableIds) {
        tableIds.forEach(id => {
            const tableDiv = document.querySelector(`.table-item[data-id="${id}"]`);
            if (tableDiv) {
                // If we found a table through Search, it also becomes clickable
                tableDiv.style.backgroundColor = "#f1c40f"; 
                tableDiv.style.boxShadow = "0 0 15px #f1c40f";
                tableDiv.style.border = "2px solid white";
                tableDiv.onclick = () => {
                    if (window.openBookingModal) window.openBookingModal();
                };
            }
        });
    },

    clearJustBooked() {
        document.querySelectorAll('.status-just-booked').forEach(table => {
            table.classList.remove('status-just-booked');
        });
    },
};