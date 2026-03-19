package ee.cgi.practice.reservation.model;

import java.time.LocalDateTime;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.Data;

@Entity
@Data
public class Reservation {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne 
    @JoinColumn(name = "table_id")
    private RestaurantTable restaurantTable;

    private String customerName;
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