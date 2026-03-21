package ee.cgi.practice.reservation.model;

import java.time.LocalDateTime;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.Data;

// Reservation made by a customer for a specific table and time slot.
@Entity
@Table(name = "reservations")
@Data 
public class Reservation {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // Each reservation is linked to a specific restaurant table.
    @ManyToOne
    @JoinColumn(name = "table_id")
    private RestaurantTable restaurantTable; 

    private String customerName;
    private LocalDateTime startTime;
    private LocalDateTime endTime;
    private int numberOfGuests;

    public Reservation() {}

    public Reservation(RestaurantTable restaurantTable, LocalDateTime startTime, int numberOfGuests, String customerName) {
        this.restaurantTable = restaurantTable;
        this.startTime = startTime;
        this.numberOfGuests = numberOfGuests;
        this.customerName = customerName;
        this.endTime = startTime.plusHours(2);
    }
}