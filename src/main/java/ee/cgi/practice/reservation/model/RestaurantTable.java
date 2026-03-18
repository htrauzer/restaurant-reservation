package ee.cgi.practice.reservation.model;

import java.util.List;
import java.util.Set;

import jakarta.persistence.CollectionTable;
import jakarta.persistence.ElementCollection;
import jakarta.persistence.Entity;  
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.Data;

@Entity 
@Data
public class RestaurantTable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private int capacity;      

    @Enumerated(EnumType.STRING)
    private Zone zone; // Main Hall, Terrace тощо

    @ElementCollection(targetClass = TableFeature.class)
    @Enumerated(EnumType.STRING)
    @CollectionTable(name = "table_features")
    private Set<TableFeature> features; // WINDOW, SOFA тощо

    // Координати для адміністратора (Вимога №7)
    private int posX;
    private int posY;

    // Конструктор за замовчуванням
    public RestaurantTable() {}

    // Зручний конструктор для ініціалізації
    public RestaurantTable(int capacity, Zone zone, Set<TableFeature> features, int x, int y) {
        this.capacity = capacity;
        this.zone = zone;
        this.features = features;
        this.posX = x;
        this.posY = y;
    }

    public void setBookedSlots(List<Integer> randomSlots) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'setBookedSlots'");
    }
}
