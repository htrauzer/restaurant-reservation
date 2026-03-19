package ee.cgi.practice.reservation.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import ee.cgi.practice.reservation.model.Reservation;

public interface ReservationRepository extends JpaRepository<Reservation, Long> {}