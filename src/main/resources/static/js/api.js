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