import { ReservationAPI } from './api.js';
import { MapUI } from './map.js';
import { initUI } from './ui-handlers.js';
import { showToast } from './ui-handlers.js';

let currentSelectedHour = null;

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

// --- ФУНКЦІЇ МОДАЛЬНОГО ВІКНА ---

window.openBookingModal = (clickedTableId) => {
    const guests = document.getElementById('guestCount').value;
    
    // Зберігаємо ID столу, який ми хочемо забронювати
    // Можна зберегти його в атрибут модалки або глобальну змінну
    window.lastSelectedTableId = clickedTableId;

    document.getElementById('modalDetails').innerText = 
        `Booking Table ID: ${clickedTableId} for ${guests} guests at ${currentSelectedHour}:00`;
    
    document.getElementById('bookingModal').style.display = 'flex';
};

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

window.closeModal = () => {
    const modal = document.getElementById('bookingModal');
    if (modal) {
        modal.style.display = 'none';
    }
};
// --- ГЛОБАЛЬНІ ФУНКЦІЇ ---

window.selectTime = (element, hour) => {
    MapUI.clearJustBooked(); // Скидаємо фіолетові столи при зміні часу
    currentSelectedHour = hour;
    document.querySelectorAll('.time-slot').forEach(s => s.classList.remove('active'));
    element.classList.add('active');
    refreshTableLayout();
};

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