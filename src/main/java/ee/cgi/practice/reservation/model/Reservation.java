package ee.cgi.practice.reservation.model;

import jakarta.persistence.*;
import lombok.Data;
import java.time.LocalDateTime;

@Entity
@Data
public class Reservation {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne // Багато бронювань можуть посилатися на один стіл у різний час
    @JoinColumn(name = "table_id")
    private RestaurantTable restaurantTable;

    private LocalDateTime startTime;
    private LocalDateTime endTime; // Має бути startTime + 2 години
    
    private int numberOfGuests;
    private String clientName;

    public Reservation() {}

    public Reservation(RestaurantTable table, LocalDateTime start, int guests, String name) {
        this.restaurantTable = table;
        this.startTime = start;
        this.endTime = start.plusHours(2); // Автоматично встановлюємо 2 години
        this.numberOfGuests = guests;
        this.clientName = name;
    }
}