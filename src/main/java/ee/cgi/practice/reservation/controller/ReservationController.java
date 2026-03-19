package ee.cgi.practice.reservation.controller;

import java.time.LocalDateTime;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import ee.cgi.practice.reservation.model.Reservation;
import ee.cgi.practice.reservation.repository.ReservationRepository;
import ee.cgi.practice.reservation.repository.TableRepository;

@RestController
@RequestMapping("/api/reservations")
public class ReservationController {

    private final ReservationRepository reservationRepository;
    private final TableRepository tableRepository;

    public ReservationController(ReservationRepository reservationRepository, TableRepository tableRepository) {
        this.reservationRepository = reservationRepository;
        this.tableRepository = tableRepository;
    }

    @PostMapping("/book")
    public String bookTable(@RequestParam Long tableId, @RequestParam String name) {
        return tableRepository.findById(tableId).map(table -> {
            Reservation res = new Reservation();
            res.setRestaurantTable(table);
            res.setCustomerName(name);
            res.setStartTime(LocalDateTime.now());
            res.setEndTime(LocalDateTime.now().plusHours(2)); // 2 hours reservation
            reservationRepository.save(res);
            return "Table " + tableId + " booked for " + name;
        }).orElse("Error: Table not found");
    }
}