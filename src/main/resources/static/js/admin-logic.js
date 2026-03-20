import { ReservationAPI } from './api.js';

export function checkLogin() {
    const user = document.getElementById('adminUser').value;
    const pass = document.getElementById('adminPass').value;
    
    if (user === 'admin' && pass === '1234') {
        sessionStorage.setItem('isAdmin', 'true');
        window.location.href = 'admin.html';
    } else {
        const errorEl = document.getElementById('loginError');
        if (errorEl) errorEl.innerText = "Invalid credentials";
    }
}

// --- Логіка Drag-and-Drop для admin.html ---
export async function initAdminMap() {
    if (sessionStorage.getItem('isAdmin') !== 'true') {
        window.location.href = 'admin-login.html';
        return;
    }

    const map = document.getElementById('admin-map');
    if (!map) return;

    try {
        const response = await fetch('/api/tables'); 
        const tables = await response.json();

        tables.forEach(table => {
            const div = document.createElement('div');
            div.className = 'table-item admin-draggable';
            div.style.left = `${table.posX}px`;
            div.style.top = `${table.posY}px`;
            div.innerText = table.id;
            div.setAttribute('data-id', table.id);
            div.draggable = true;

            // Обробка завершення перетягування
            div.addEventListener('dragend', async (e) => {
                const rect = map.getBoundingClientRect();
                const x = Math.round(e.clientX - rect.left - 25);
                const y = Math.round(e.clientY - rect.top - 25);
                
                div.style.left = `${x}px`;
                div.style.top = `${y}px`;
                
                await updatePositionOnServer(table.id, x, y);
            });

            map.appendChild(div);
        });
    } catch (err) {
        console.error("Failed to load admin map:", err);
    }
}

async function updatePositionOnServer(id, x, y) {
    const params = new URLSearchParams({ x, y });
    try {
        const response = await fetch(`/api/admin/tables/${id}/position?${params}`, {
            method: 'PATCH'
        });
        if (!response.ok) throw new Error("Server rejected update");
        console.log(`Table ${id} saved at ${x}, ${y}`);
    } catch (err) {
        console.error("Save failed:", err);
    }
}