export const ReservationAPI = {
   
    async getOccupied(hour) {
        const res = await fetch(`/api/reservations/occupied?hour=${hour}`);
        return res.json();
    },

    
    async getRecommendations(hour, guests, zone, features) {
        const url = `/api/reservations/recommend?hour=${hour}&guests=${guests}&zone=${zone}&features=${features}`;
        const res = await fetch(url);
        return res.json();
    },

    
    async findAvailable(hour, guests, zone) {
        const url = `/api/reservations/find-available?hour=${hour}&guests=${guests}&zone=${zone}`;
        const res = await fetch(url);
        return res.json();
    }
};

async function makeReservation() {
    const name = document.getElementById('customerName').value;
    const guests = document.getElementById('guestCount').value;
    
    const selectedTables = Array.from(document.querySelectorAll('.table-item'))
        .filter(div => {
            const bg = div.style.backgroundColor;
            return bg.includes('241, 196, 15') || bg.includes('52, 152, 219');
        })
        .map(div => div.getAttribute('data-id'));

    if (selectedTables.length === 0) {
        alert("Please select a table first!");
        return;
    }

    const params = new URLSearchParams();
    params.append('name', name);
    params.append('guests', guests);
    params.append('hour', currentSelectedHour);
    selectedTables.forEach(id => params.append('tableIds', id));

    const response = await fetch('/api/reservations/book', {
        method: 'POST',
        body: params
    });

    if (response.ok) {
        alert("Success! Table(s) booked.");
        location.reload(); 
    } else {
        const error = await response.text();
        alert("Error: " + error);
    }
}