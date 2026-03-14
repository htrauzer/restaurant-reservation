package ee.cgi.practice.reservation;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TableRepository extends JpaRepository<RestaurantTable, Long> {
    // This looks empty, but Spring Boot uses it to give you 
    // commands like .save(), .findAll(), and .delete() automatically.
}