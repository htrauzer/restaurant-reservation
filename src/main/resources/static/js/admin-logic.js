import { ReservationAPI } from './api.js';

// Admin login modal logic and admin map management for table positioning.
window.openAdminModal = () => {
    const modal = document.getElementById('adminLoginModal');
    if (modal) modal.style.display = 'flex';
};
// Closes the admin login modal when the user clicks outside of it or on the close button.
window.closeAdminModal = () => {
    const modal = document.getElementById('adminLoginModal');
    if (modal) modal.style.display = 'none';
};
// Handles the admin login process by checking hardcoded credentials and redirecting to the admin page if successful.
window.processAdminLogin = () => {
    const user = document.getElementById('adminUser').value;
    const pass = document.getElementById('adminPass').value;
    const status = document.getElementById('adminLoginStatus');

    if (user === 'admin' && pass === '1234') {
        sessionStorage.setItem('isAdmin', 'true');
        window.location.href = '/admin'; 
    } else {
        if (status) {
            status.innerText = "Invalid username or password";
            setTimeout(() => { if(status) status.innerText = ""; }, 3000);
        }
    }
};
// Initializes the admin map by fetching table data from the server and creating draggable elements for each table, allowing admins to reposition them visually.
export async function initAdminMap() {
    const map = document.getElementById('admin-map');
    if (!map) return;

    map.innerHTML = '';

    try {
        const response = await fetch('/api/tables'); 
        const tables = await response.json();

        tables.forEach(table => {
            const div = document.createElement('div');
            console.log("Table data:", table);

            div.className = `table-item admin-draggable ${table.shape === 'round' ? 'shape-round' : 'shape-square'}`;
            
            const x = (table.posX && table.posX !== 0) ? table.posX : defaultX;
            const y = (table.posY && table.posY !== 0) ? table.posY : defaultY;
            const scale = 5;

            div.style.left = (x * scale) + "px";
            div.style.top = (y * scale) + "px";
            div.innerText = table.id;
            div.draggable = true;

            div.addEventListener('dragend', (e) => {
                const rect = map.getBoundingClientRect();

                const x = Math.round(e.clientX - rect.left);
                const y = Math.round(e.clientY - rect.top);

                const safeX = Math.max(0, Math.min(x, rect.width - 25));
                const safeY = Math.max(0, Math.min(y, rect.height - 25));
                // Update the position of the table element on the map.
                div.style.left = `${safeX}px`;
                div.style.top = `${safeY}px`;
                
                updatePositionOnServer(table.id, safeX, safeY);
            });

            map.appendChild(div);
        });
    } catch (err) {
        console.error("Error loading tables:", err);
    }
}
// Sends an asynchronous PATCH request to the server to update the position of a table based on its ID and new coordinates, ensuring that the changes are saved persistently.
async function updatePositionOnServer(id, x, y) {
    const params = new URLSearchParams({ x, y });
    try {
        const response = await fetch(`/api/admin/tables/${id}/position?${params}`, {
            method: 'PATCH'
        });
        if (!response.ok) throw new Error("Server error");
        console.log(`Table ${id} saved at X:${x}, Y:${y}`);
    } catch (err) {
        console.error("Save failed:", err);
    }
}