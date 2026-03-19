package ee.cgi.practice.reservation.model;

import java.time.LocalDateTime;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@NoArgsConstructor // Lombok will create a no-args constructor for JPA
public class Reservation {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne 
    @JoinColumn(name = "table_id")
    private RestaurantTable restaurantTable;

    private String clientName;
    private int numberOfGuests;
    
    private LocalDateTime startTime;
    private LocalDateTime endTime; // Always startTime + 2 hours

    // Constructor for convenient creation of reservations in the service
    public Reservation(RestaurantTable table, LocalDateTime start, int guests, String name) {
        this.restaurantTable = table;
        this.startTime = start;
        this.endTime = start.plusHours(2); // (2 hours)
        this.numberOfGuests = guests;
        this.clientName = name;
    }
}