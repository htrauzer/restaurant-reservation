package ee.cgi.practice.reservation.controller;

import ee.cgi.practice.reservation.model.Reservation;
import ee.cgi.practice.reservation.repository.ReservationRepository;
import ee.cgi.practice.reservation.repository.TableRepository;
import org.springframework.web.bind.annotation.*;
import java.time.LocalDateTime;

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
            res.setEndTime(LocalDateTime.now().plusHours(2)); // Вимога №5
            reservationRepository.save(res);
            return "Стіл " + tableId + " заброньовано для " + name;
        }).orElse("Помилка: стіл не знайдено");
    }
}