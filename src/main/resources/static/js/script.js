// Функція, що викликається, коли користувач відпускає стіл
function updateTableOnServer(tableId, newX, newY) {
    fetch(`/api/admin/tables/${tableId}/position?x=${newX}&y=${newY}`, {
        method: 'PATCH'
    })
    .then(response => {
        if (response.ok) {
            console.log("Позицію стола збережено!");
        }
    });
}

// Приклад обробки події перетягування (Drag End)
document.querySelectorAll('.table-icon').forEach(table => {
    table.addEventListener('dragend', (e) => {
        const id = table.dataset.id;
        const x = e.clientX;
        const y = e.clientY;
        
        // Візуально переміщуємо
        table.style.left = x + 'px';
        table.style.top = y + 'px';
        
        // Відправляємо на сервер
        updateTableOnServer(id, x, y);
    });
});