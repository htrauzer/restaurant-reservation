package ee.cgi.practice.reservation.controller;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.http.ResponseEntity;
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
import ee.cgi.practice.reservation.service.BookingService;
import ee.cgi.practice.reservation.service.RecommendationService;
import ee.cgi.practice.reservation.service.ReservationService;

@RestController
@RequestMapping("/api/reservations")
public class ReservationController {

    private final ReservationRepository reservationRepository;
    private final TableRepository tableRepository;
    private final RecommendationService recommendationService;
    private final ReservationService reservationService;
    // 1. ДОДАНО ПОЛЕ ДЛЯ СЕРВІСУ
    private final BookingService bookingService;

    // 2. ОНОВЛЕНО КОНСТРУКТОР (додано BookingService)
    public ReservationController(ReservationRepository reservationRepository, 
                                 TableRepository tableRepository, 
                                 RecommendationService recommendationService,
                                 ReservationService reservationService,
                                 BookingService bookingService) {
        this.reservationRepository = reservationRepository;
        this.tableRepository = tableRepository;
        this.recommendationService = recommendationService;
        this.reservationService = reservationService;
        this.bookingService = bookingService;
    }

    /**
     * 1. Розумний пошук (ReservationService)
     */
    @GetMapping("/find-available")
    public List<Long> findAvailable(@RequestParam int hour, 
                                    @RequestParam int guests, 
                                    @RequestParam Zone zone) {
        LocalDateTime requestedStart = LocalDateTime.of(LocalDate.now(), LocalTime.of(hour, 0));
        List<RestaurantTable> tables = reservationService.findAvailableTables(guests, zone, requestedStart);
        
        return tables.stream()
                .map(RestaurantTable::getId)
                .collect(Collectors.toList());
    }

    /**
     * 2. Рекомендація найкращого варіанту (RecommendationService)
     */
    @GetMapping("/recommend")
    public List<Long> getRecommendations(
            @RequestParam int hour,
            @RequestParam int guests,
            @RequestParam(required = false) Zone zone,
            @RequestParam(required = false) Set<TableFeature> features) {
        
        return recommendationService.recommendTables(hour, guests, zone, features).stream()
                .map(RestaurantTable::getId)
                .collect(Collectors.toList());
    }

    /**
     * 3. Статус зайнятості (Сірі столи)
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
     * 4. Збереження нового бронювання
     */
    @PostMapping("/book")
    public ResponseEntity<?> bookTable(@RequestParam String name, 
                                    @RequestParam int guests, 
                                    @RequestParam int hour,
                                    @RequestParam List<Long> tableIds) {
        try {
            LocalDateTime start = LocalDateTime.of(LocalDate.now(), LocalTime.of(hour, 0));
            // Тепер змінна bookingService доступна
            List<Reservation> result = bookingService.createBooking(name, guests, tableIds, start);
            return ResponseEntity.ok("Reserved " + result.size() + " table(s) for " + name);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
}