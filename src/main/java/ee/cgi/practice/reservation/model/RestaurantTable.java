package ee.cgi.practice.reservation.model;

import java.util.Set;

import jakarta.persistence.CollectionTable;
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
@Table(name = "restaurant_tables") // Явно вказуємо назву для БД
@Data
public class RestaurantTable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private int capacity;      

    @Enumerated(EnumType.STRING)
    private Zone zone; 

    @ElementCollection(targetClass = TableFeature.class, fetch = FetchType.EAGER)
    @Enumerated(EnumType.STRING)
    @CollectionTable(name = "table_features", joinColumns = @JoinColumn(name = "table_id"))
    private Set<TableFeature> features; 

    private int posX;
    private int posY;

    // Порожній конструктор (потрібен для Hibernate)
    public RestaurantTable() {}

    // Конструктор для DataInitializer
    public RestaurantTable(int capacity, Zone zone, Set<TableFeature> features, int x, int y) {
        this.capacity = capacity;
        this.zone = zone;
        this.features = features;
        this.posX = x;
        this.posY = y;
    }
}