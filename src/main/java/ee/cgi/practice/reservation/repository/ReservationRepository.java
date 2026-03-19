package ee.cgi.practice.reservation.repository;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import ee.cgi.practice.reservation.model.Reservation;

public interface ReservationRepository extends JpaRepository<Reservation, Long> {

    // Finding all reservations that overlap with the requested time period
    @Query("SELECT r FROM Reservation r WHERE r.startTime < :requestedEnd AND r.endTime > :requestedStart")
    List<Reservation> findOverlappingReservations(
        @Param("requestedStart") LocalDateTime start, 
        @Param("requestedEnd") LocalDateTime end // Назва в лапках має бути як у запиті (:requestedEnd)
    );
}