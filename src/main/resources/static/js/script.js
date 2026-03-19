let currentSelectedHour = null;


document.addEventListener('DOMContentLoaded', () => {
    document.querySelectorAll('input[name="zone"]').forEach(radio => {
        radio.addEventListener('click', function() {
            
            if (this.dataset.wasChecked === 'true') {
                this.checked = false;
                this.dataset.wasChecked = 'false';
            } else {
                
                document.querySelectorAll('input[name="zone"]').forEach(r => r.dataset.wasChecked = 'false');
                this.dataset.wasChecked = 'true';
            }
            
            refreshTableLayout();
        });
    });

    
    document.getElementById('guestCount').addEventListener('change', refreshTableLayout);
    document.querySelectorAll('input[name="feature"]').forEach(cb => {
        cb.addEventListener('change', refreshTableLayout);
    });
});


function selectTime(element, hour) {
    currentSelectedHour = hour;
    
   
    document.querySelectorAll('.time-slot').forEach(s => s.classList.remove('active'));
    element.classList.add('active');
    
    refreshTableLayout();
}


function refreshTableLayout() {
    if (!currentSelectedHour) return;

    
    const guests = document.getElementById('guestCount').value;
    const zoneElement = document.querySelector('input[name="zone"]:checked');
    const zone = zoneElement ? zoneElement.value : ""; // Порожньо, якщо нічого не обрано
    
    const features = Array.from(document.querySelectorAll('input[name="feature"]:checked'))
                          .map(cb => cb.value)
                          .join(',');

   
    Promise.all([
        fetch(`/api/reservations/occupied?hour=${currentSelectedHour}`).then(res => res.json()),
        fetch(`/api/reservations/recommend?hour=${currentSelectedHour}&guests=${guests}&zone=${zone}&features=${features}`).then(res => res.json())
    ])
    .then(([occupiedIds, recommendedIds]) => {
        const allTables = document.querySelectorAll('.table-item');
        
        allTables.forEach(tableDiv => {
            const tableId = parseInt(tableDiv.getAttribute('data-id'));
            
            
            tableDiv.style.backgroundColor = "#2ecc71"; 
            tableDiv.classList.remove('status-reserved');
            tableDiv.style.boxShadow = "none";
            tableDiv.style.pointerEvents = "auto";

            
            if (occupiedIds.includes(tableId)) {
                tableDiv.style.backgroundColor = "#95a5a6";
                tableDiv.classList.add('status-reserved');
                tableDiv.style.pointerEvents = "none";
            } 
            
            else if (recommendedIds.includes(tableId)) {
                tableDiv.style.backgroundColor = "#f1c40f"; 
                tableDiv.style.boxShadow = "0 0 15px rgba(241, 196, 15, 0.8)"; 
            }
        });
    })
    .catch(error => console.error("Error refreshing layout:", error));
}


function filterTables() {
    refreshTableLayout();
}