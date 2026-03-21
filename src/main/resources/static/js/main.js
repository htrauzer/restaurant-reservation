import { ReservationAPI } from './api.js';
import { MapUI } from './map.js';
import { initUI } from './ui-handlers.js';
import { showToast } from './ui-handlers.js';

let currentSelectedHour = null;

// Main logic for handling user interactions, including time selection, table filtering, and booking modal management. It interacts with the ReservationAPI to fetch data and update the UI accordingly.
async function refreshTableLayout() {
    if (!currentSelectedHour) return;

    const guests = document.getElementById('guestCount').value;
    const zoneEl = document.querySelector('input[name="zone"]:checked');
    const zone = zoneEl ? zoneEl.value : "";
    const features = Array.from(document.querySelectorAll('input[name="feature"]:checked'))
                          .map(cb => cb.value).join(',');

    try {
        const [occupied, recommended] = await Promise.all([
            ReservationAPI.getOccupied(currentSelectedHour),
            ReservationAPI.getRecommendations(currentSelectedHour, guests, zone, features)
        ]);

        MapUI.resetTables();
        MapUI.updateStatus(occupied, recommended);
    } catch (err) {
        console.error("Layout refresh failed:", err);
    }
}

// Functions of Modal window

window.openBookingModal = (clickedTableId) => {
    const guests = document.getElementById('guestCount').value;
    
    // Saving ID of the table, which we want to book
    // Can save it in a modal attribute or a global variable
    window.lastSelectedTableId = clickedTableId;

    document.getElementById('modalDetails').innerText = 
        `Booking Table ID: ${clickedTableId} for ${guests} guests at ${currentSelectedHour}:00`;
    
    document.getElementById('bookingModal').style.display = 'flex';
};

// Closes the booking modal when the user clicks outside of it or on the close button.
window.confirmBooking = async () => {
    const nameInput = document.getElementById('customerName');
    const name = nameInput.value;
    const statusDiv = document.getElementById('bookingStatus');

    if (!name) return alert("Please enter your name!");

    const guests = document.getElementById('guestCount').value;
    const hour = currentSelectedHour;
    const tableId = window.lastSelectedTableId;
    const params = new URLSearchParams();

    params.append('name', name);
    params.append('guests', guests);
    params.append('hour', hour);
    params.append('tableIds', tableId); 

    try {
        const response = await fetch('/api/reservations/book', {
            method: 'POST',
            body: params
        });

        if (response.ok) {
            window.closeModal();
            const tableDiv = document.querySelector(`.table-item[data-id="${tableId}"]`);
            if (tableDiv) {
                tableDiv.style.backgroundColor = ""; 
                tableDiv.classList.add('status-just-booked');
            }

            nameInput.value = "";
            showToast("Success! Table " + tableId + " booked.", "success");
        } else {
            const errorMsg = await response.text();
            showErrorMessage(errorMsg);
        }
    } catch (err) {
        showErrorMessage("Connection error. Try again.");
    }
};

// Displays an error message in the booking modal's status area or as a toast notification if the status div is not available, and automatically clears the message after a few seconds to keep the UI clean.
function showErrorMessage(message) {
    const statusDiv = document.getElementById('bookingStatus');
    if (statusDiv) {
        statusDiv.innerText = message;
        statusDiv.style.color = "#e74c3c"; 
        setTimeout(() => {
            statusDiv.innerText = "";
        }, 3000);
    } else {
        showToast(message, 'error');
    }
}
// Closes the booking modal when the user clicks outside of it or on the close button.
window.closeModal = () => {
    const modal = document.getElementById('bookingModal');
    if (modal) {
        modal.style.display = 'none';
    }
};

//  Global Functions
// Handles the selection of a time slot by updating the current selected hour, refreshing the table layout to reflect availability for that hour, and visually indicating the active time slot in the UI.
window.selectTime = (element, hour) => {
    MapUI.clearJustBooked(); // Updating purple tables when time is changed
    currentSelectedHour = hour;
    document.querySelectorAll('.time-slot').forEach(s => s.classList.remove('active'));
    element.classList.add('active');
    refreshTableLayout();
};

// Opens the admin login modal when the user clicks the admin button, allowing them to enter credentials to access the admin interface.
window.filterTables = async () => {
    MapUI.clearJustBooked();
    if (!currentSelectedHour) return alert("Select time first!");
    
    const guests = document.getElementById('guestCount').value;
    const zoneEl = document.querySelector('input[name="zone"]:checked');
    if (!zoneEl) return alert("Select a zone for smart search!");

    const tableIds = await ReservationAPI.findAvailable(currentSelectedHour, guests, zoneEl.value);
    
    MapUI.resetTables();
    if (tableIds.length > 0) {
        MapUI.highlightFound(tableIds);
    } else {
        alert("No tables or pairs found.");
    }
};

document.addEventListener('DOMContentLoaded', () => {
    initUI(refreshTableLayout);
});