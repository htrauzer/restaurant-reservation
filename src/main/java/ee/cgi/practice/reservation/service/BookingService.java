package ee.cgi.practice.reservation.service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import ee.cgi.practice.reservation.model.Reservation;
import ee.cgi.practice.reservation.model.RestaurantTable;
import ee.cgi.practice.reservation.repository.ReservationRepository;
import ee.cgi.practice.reservation.repository.TableRepository;

@Service
public class BookingService {

    private final ReservationRepository reservationRepository;
    private final TableRepository tableRepository;
    private final ReservationService reservationService;

    public BookingService(ReservationRepository reservationRepository, 
                          TableRepository tableRepository, 
                          ReservationService reservationService) {
        this.reservationRepository = reservationRepository;
        this.tableRepository = tableRepository;
        this.reservationService = reservationService;
    }

    @Transactional
    public List<Reservation> createBooking(String customerName, int guests, List<Long> tableIds, LocalDateTime startTime) {
        List<Reservation> createdReservations = new ArrayList<>();
        LocalDateTime endTime = startTime.plusHours(2); 

        for (Long id : tableIds) {
            RestaurantTable table = tableRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Table not found: " + id));

            boolean isBusy = reservationRepository.findOverlappingReservations(startTime, endTime)
                .stream()
                .anyMatch(r -> r.getRestaurantTable().getId().equals(id));

            if (isBusy) {
                throw new RuntimeException("Next booking too soon");
            }

            Reservation reservation = new Reservation(table, startTime, guests, customerName);
            createdReservations.add(reservationRepository.save(reservation));
        }

        return createdReservations;
    }
}