package ee.cgi.practice.reservation.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import ee.cgi.practice.reservation.model.RestaurantTable;
import ee.cgi.practice.reservation.model.Zone;

public interface TableRepository extends JpaRepository<RestaurantTable, Long> {
    // lookup tables by zone
    List<RestaurantTable> findByZone(Zone zone);
    
    // lookup tables by capacity
    List<RestaurantTable> findByCapacityGreaterThanEqual(int capacity);
}