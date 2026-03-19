package ee.cgi.practice.reservation.model;

import java.util.Set;

import jakarta.persistence.CollectionTable; // Using wildcard for all JPA annotations
import jakarta.persistence.Column;
import jakarta.persistence.ElementCollection;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.Table;
import lombok.Data;

@Entity 
@Table(name = "restaurant_tables")
@Data
public class RestaurantTable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private int capacity;      

    @Enumerated(EnumType.STRING)
    private Zone zone; 

    // FIXED: Corrected the join column name and removed the extra string wrapper
    @ElementCollection(targetClass = TableFeature.class, fetch = FetchType.EAGER)
    @Enumerated(EnumType.STRING)
    @CollectionTable(name = "table_features", joinColumns = @JoinColumn(name = "table_id"))
    private Set<TableFeature> features; 

    // Explicitly mapping Java fields to SQL column names for import.sql compatibility
    @Column(name = "pos_x")
    private int posX;

    @Column(name = "pos_y")
    private int posY;

    // Empty constructor (needed for Hibernate)
    public RestaurantTable() {}

    // Constructor for manual creation
    public RestaurantTable(int capacity, Zone zone, Set<TableFeature> features, int x, int y) {
        this.capacity = capacity;
        this.zone = zone;
        this.features = features;
        this.posX = x;
        this.posY = y;
    }
}