package ee.cgi.practice.reservation.controller;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import ee.cgi.practice.reservation.model.Reservation;
import ee.cgi.practice.reservation.model.RestaurantTable;
import ee.cgi.practice.reservation.model.TableFeature;
import ee.cgi.practice.reservation.model.Zone;
import ee.cgi.practice.reservation.repository.ReservationRepository;
import ee.cgi.practice.reservation.repository.TableRepository;
import ee.cgi.practice.reservation.service.RecommendationService;

@RestController
@RequestMapping("/api/reservations")
public class ReservationController {

    private final ReservationRepository reservationRepository;
    private final TableRepository tableRepository;
    // 1. Обов'язково додаємо це поле
    private final RecommendationService recommendationService;

    // 2. Оновлюємо конструктор, щоб Spring міг "вставити" сюди RecommendationService
    public ReservationController(ReservationRepository reservationRepository, 
                                 TableRepository tableRepository, 
                                 RecommendationService recommendationService) {
        this.reservationRepository = reservationRepository;
        this.tableRepository = tableRepository;
        this.recommendationService = recommendationService;
    }

    /**
     * Ендпоінт для отримання рекомендацій (жовті столи)
     */
    @GetMapping("/recommend")
    public List<Long> getRecommendations(
            @RequestParam int hour,
            @RequestParam int guests,
            // required = false дозволяє працювати, коли зона не обрана
            @RequestParam(required = false) Zone zone,
            @RequestParam(required = false) Set<TableFeature> features) {
        
        // Викликаємо метод з сервісу та перетворюємо список об'єктів у список ID
        return recommendationService.recommendTables(hour, guests, zone, features).stream()
                .map(RestaurantTable::getId)
                .collect(Collectors.toList());
    }

    /**
     * Ендпоінт для отримання зайнятих столів (сірі столи)
     */
    @GetMapping("/occupied")
    public List<Long> getOccupiedTables(@RequestParam int hour) {
        LocalDateTime targetTime = LocalDateTime.of(LocalDate.now(), LocalTime.of(hour, 0));
        
        return reservationRepository.findAll().stream()
                .filter(res -> !targetTime.isBefore(res.getStartTime()) && targetTime.isBefore(res.getEndTime()))
                .map(res -> res.getRestaurantTable().getId())
                .collect(Collectors.toList());
    }

    /**
     * Ендпоінт для бронювання столу
     */
    @PostMapping("/book")
    public String bookTable(@RequestParam Long tableId, @RequestParam String name, @RequestParam int guests) {
        return tableRepository.findById(tableId).map(table -> {
            Reservation res = new Reservation(table, LocalDateTime.now(), guests, name);
            reservationRepository.save(res);
            return "Table " + tableId + " booked for " + name;
        }).orElse("Error: Table not found");
    }
}