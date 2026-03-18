package ee.cgi.practice.reservation.repository;

import ee.cgi.practice.reservation.model.RestaurantTable;
import ee.cgi.practice.reservation.model.Zone;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface TableRepository extends JpaRepository<RestaurantTable, Long> {
    // Пошук вільних столів у конкретній зоні
    List<RestaurantTable> findByZone(Zone zone);
    
    // Пошук столів за місткістю
    List<RestaurantTable> findByCapacityGreaterThanEqual(int capacity);
}