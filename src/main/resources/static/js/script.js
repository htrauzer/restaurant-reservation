function filterByTime(selectedHour) {
    const hour = parseInt(selectedHour);
    const nextHour = hour + 1;

    // 1. Highlight the button
    document.querySelectorAll('.hour-btn').forEach(btn => btn.classList.remove('active'));
    
    // Safety check for the event trigger
    if (event && event.currentTarget) {
        event.currentTarget.classList.add('active');
    }

    // 2. Filter the tables
    const tables = document.querySelectorAll('.table-div');
    
    tables.forEach(table => {
        const bookedSlots = table.getAttribute('data-slots'); 
        
        // Logic: Is it busy at hour OR next hour?
        const isBusyNow = bookedSlots.includes(hour.toString());
        const isBusyNext = bookedSlots.includes(nextHour.toString());

        if (isBusyNow || isBusyNext) {
            table.style.backgroundColor = "#95a5a6"; // Gray
            table.style.opacity = "0.4";
        } else {
            table.style.backgroundColor = "#2ecc71"; // Green
            table.style.opacity = "1";
        }
    });
}

// This runs as soon as the browser finishes loading the page
window.addEventListener('load', () => {
// Automatically select 12:00 to show the initial random state
filterByTime('12');

// This part makes the first button (12:00) look active/blue on load
    const firstBtn = document.querySelector('.hour-btn');
    if (firstBtn) firstBtn.classList.add('active');

});