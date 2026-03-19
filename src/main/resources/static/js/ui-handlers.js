import { MapUI } from './map.js';

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