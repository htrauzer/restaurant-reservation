import { ReservationAPI } from './api.js';

export async function initAdminMap() {

    if (sessionStorage.getItem('isAdmin') !== 'true') {
        alert("Access Denied. Please log in.");
        window.location.href = 'index.html'; 
        return;
    }
}

window.openAdminModal = () => {
    const modal = document.getElementById('adminLoginModal');
    const status = document.getElementById('adminLoginStatus');
    if (modal) modal.style.display = 'flex';
    if (status) status.innerText = "";
};

window.closeAdminModal = () => {
    const modal = document.getElementById('adminLoginModal');
    if (modal) {
        modal.style.display = 'none';
        document.getElementById('adminUser').value = "";
        document.getElementById('adminPass').value = "";
    }
};

window.processAdminLogin = () => {
    const user = document.getElementById('adminUser').value;
    const pass = document.getElementById('adminPass').value;
    const status = document.getElementById('adminLoginStatus');

    if (user === 'admin' && pass === '1234') {
        sessionStorage.setItem('isAdmin', 'true');
        window.location.href = 'admin.html'; 
    } else {
        if (status) {
            status.innerText = "Invalid username or password";
            setTimeout(() => { 
                status.innerText = ""; 
            }, 3000);
        }
    }
};


// --- Logic of Drag-and-Drop for admin.html ---

export async function initAdminMap() {
    // Перевірка прав доступу
    if (sessionStorage.getItem('isAdmin') !== 'true') {
        window.location.href = 'index.html';
        return;
    }

    const map = document.getElementById('admin-map');
    if (!map) return;

    // Тут залишається ваш код завантаження столів та Drag-and-Drop
    console.log("Admin map initialized");
    // ... load tables and add drag events ...
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