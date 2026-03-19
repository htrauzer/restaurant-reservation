import { ReservationAPI } from './api.js';
import { MapUI } from './map.js';
import { initUI } from './ui-handlers.js';

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


window.selectTime = (element, hour) => {
    currentSelectedHour = hour;
    document.querySelectorAll('.time-slot').forEach(s => s.classList.remove('active'));
    element.classList.add('active');
    refreshTableLayout();
};

window.filterTables = async () => {
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