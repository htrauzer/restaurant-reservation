function showTimetable(tableId, bookedSlots) {
    const tableElement = document.getElementById('master-timetable');
    const titleElement = document.getElementById('selected-table-name');
    
    tableElement.style.display = 'table';
    titleElement.innerText = "Table #" + tableId;

    const tbody = document.getElementById('timetable-body');
    tbody.innerHTML = '';

    for (let hour = 12; hour <= 23; hour++) {
        // BookedSlots comes in as a string like "[12, 13, 14]" from Thymeleaf
        const isBusy = bookedSlots.includes(hour.toString());
        
        const row = document.createElement('tr');
        row.className = isBusy ? 'busy-row' : 'free-row';
        
        row.innerHTML = `
            <td>${hour}:00</td>
            <td>${isBusy ? 'Occupied' : 'Available'}</td>
        `;
        tbody.appendChild(row);
    }
}