package ee.cgi.practice.reservation;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;

@Entity // This tells the database to create a table for this class
public class RestaurantTable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private int seats;          // How many people can sit here
    private String zone;       // e.g., "Terrace", "Main Hall"
    private boolean isWindow;  // Preference: Near a window?
    private int xCoordinate;   // Position on the floor plan map
    private int yCoordinate;   // Position on the floor plan map
    private boolean isOccupied; // Is someone sitting there right now?

    // Standard empty constructor (needed for the database)
    public RestaurantTable() {}

    // Constructor to help us create tables easily
    public RestaurantTable(int seats, String zone, boolean isWindow, int x, int y) {
        this.seats = seats;
        this.zone = zone;
        this.isWindow = isWindow;
        this.xCoordinate = x;
        this.yCoordinate = y;
        this.isOccupied = false;
    }

    // GETTERS (So our website can read the data)
    public Long getId() { return id; }
    public int getSeats() { return seats; }
    public String getZone() { return zone; }
    public boolean isWindow() { return isWindow; }
    public int getXCoordinate() { return xCoordinate; }
    public int getYCoordinate() { return yCoordinate; }
    public boolean isOccupied() { return isOccupied; }
    public void setOccupied(boolean occupied) { isOccupied = occupied; }
}
