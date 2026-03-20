import { MapUI } from './map.js';

export function showToast(message, type = 'success') {
    const toast = document.createElement('div');
    toast.className = `toast toast-${type}`;
    toast.innerText = message;
    
    document.body.appendChild(toast);
    
    // Автоматичне видалення через 3 секунди
    setTimeout(() => {
        toast.classList.add('fade-out');
        setTimeout(() => toast.remove(), 500);
    }, 3000);
}

export function initUI(refreshCallback) {
    
    document.querySelectorAll('input[name="zone"]').forEach(radio => {
        radio.addEventListener('click', function() {
            if (this.dataset.wasChecked === 'true') {
                this.checked = false;
                this.dataset.wasChecked = 'false';
            } else {
                document.querySelectorAll('input[name="zone"]').forEach(r => r.dataset.wasChecked = 'false');
                this.dataset.wasChecked = 'true';
            }
            refreshCallback();
        });
    });

    
    document.getElementById('guestCount').addEventListener('change', refreshCallback);
    document.querySelectorAll('input[name="feature"]').forEach(cb => {
        cb.addEventListener('change', refreshCallback);
    });
}


// Open the booking modal with selected table details
window.openBookingModal = () => {
    const selectedTables = Array.from(document.querySelectorAll('.table-item'))
        .filter(div => {
            const bg = div.style.backgroundColor;
            return bg.includes('241, 196, 15') || bg.includes('52, 152, 219');
        });

    if (selectedTables.length === 0) {
        alert("Please select a table on the map first!");
        return;
    }

    const ids = selectedTables.map(d => d.getAttribute('data-id')).join(', ');
    const guests = document.getElementById('guestCount').value;
    
    document.getElementById('modalDetails').innerText = 
        `Booking Table(s): ${ids} for ${guests} guests at ${currentSelectedHour}:00`;
    
    document.getElementById('bookingModal').style.display = 'flex';
};

window.closeModal = () => {
    document.getElementById('bookingModal').style.display = 'none';
};

window.confirmBooking = async () => {
    const name = document.getElementById('customerName').value;
    if (!name) return alert("Please enter your name!");

    const guests = document.getElementById('guestCount').value;
    const selectedIds = Array.from(document.querySelectorAll('.table-item'))
        .filter(div => {
            const bg = div.style.backgroundColor;
            return bg.includes('241, 196, 15') || bg.includes('52, 152, 219');
        })
        .map(div => div.getAttribute('data-id'));

    const params = new URLSearchParams();
    params.append('name', name);
    params.append('guests', guests);
    params.append('hour', currentSelectedHour);
    selectedIds.forEach(id => params.append('tableIds', id));

    try {
        const response = await fetch('/api/reservations/book', {
            method: 'POST',
            body: params
        });

        if (response.ok) {
            alert("Reservation successful!");
            location.reload(); 
        } else {
            const errorMsg = await response.text();
            alert("Reservation failed: " + errorMsg);
        }
    } catch (err) {
        console.error("Error:", err);
    }
};