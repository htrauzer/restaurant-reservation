package ee.cgi.practice.reservation.controller;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.web.bind.annotation.GetMapping;
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

    // FIX for the "map" error: Explicitly handle the Optional
    @PostMapping("/book")
    public String bookTable(@RequestParam Long tableId, @RequestParam String name, @RequestParam int guests) {
        return tableRepository.findById(tableId).map(table -> {
            Reservation res = new Reservation(table, LocalDateTime.now(), guests, name);
            reservationRepository.save(res);
            return "Table " + tableId + " booked for " + name;
        }).orElse("Error: Table not found");
    }

    // NEW: Needed for the colors to change when clicking time
    @GetMapping("/occupied")
    public List<Long> getOccupiedTables(@RequestParam int hour) {
        LocalDateTime targetTime = LocalDateTime.of(LocalDate.now(), LocalTime.of(hour, 0));
        
        return reservationRepository.findAll().stream()
        .filter(res -> !targetTime.isBefore(res.getStartTime()) && targetTime.isBefore(res.getEndTime()))
        .map(res -> res.getRestaurantTable().getId()) // Викликаємо метод
        .collect(Collectors.toList());
    }
}