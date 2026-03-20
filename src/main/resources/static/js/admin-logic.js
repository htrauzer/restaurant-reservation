import { ReservationAPI } from './api.js';

window.openAdminModal = () => {
    const modal = document.getElementById('adminLoginModal');
    if (modal) modal.style.display = 'flex';
};

window.closeAdminModal = () => {
    const modal = document.getElementById('adminLoginModal');
    if (modal) modal.style.display = 'none';
};

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

export async function initAdminMap() {
    const map = document.getElementById('admin-map');
    if (!map) return;

    map.innerHTML = '';

    try {
        const response = await fetch('/api/tables'); 
        const tables = await response.json();

        tables.forEach(table => {
            const div = document.createElement('div');

            div.className = `table-item admin-draggable ${table.shape === 'round' ? 'shape-round' : 'shape-square'}`;

            div.style.left = (table.posX || 0) + 'px';
            div.style.top = (table.posY || 0) + 'px';
            div.innerText = table.id;
            div.draggable = true;

            div.addEventListener('dragend', (e) => {
                const rect = map.getBoundingClientRect();

                const x = Math.round(e.clientX - rect.left);
                const y = Math.round(e.clientY - rect.top);

                const safeX = Math.max(0, Math.min(x, rect.width - 50));
                const safeY = Math.max(0, Math.min(y, rect.height - 50));

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