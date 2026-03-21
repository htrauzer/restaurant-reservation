package ee.cgi.practice.reservation.service;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;

import ee.cgi.practice.reservation.model.Reservation;
import ee.cgi.practice.reservation.model.RestaurantTable;
import ee.cgi.practice.reservation.model.Zone;
import ee.cgi.practice.reservation.repository.ReservationRepository;
import ee.cgi.practice.reservation.repository.TableRepository;

// Service responsible for handling the booking logic, including checking for overlapping reservations and creating new bookings.
@Service
public class ReservationService {

    private final TableRepository tableRepository;
    private final ReservationRepository reservationRepository;

    public ReservationService(TableRepository tableRepository, ReservationRepository reservationRepository) {
        this.tableRepository = tableRepository;
        this.reservationRepository = reservationRepository;
    }

    // Finds available tables based on the number of guests, preferred zone, and requested time slot.
    public List<RestaurantTable> findAvailableTables(int guests, Zone zone, LocalDateTime requestedStart) {
        LocalDateTime requestedEnd = requestedStart.plusHours(2);
        
        List<RestaurantTable> allTables = tableRepository.findByZone(zone);
        List<Reservation> overlapping = reservationRepository.findOverlappingReservations(requestedStart, requestedEnd);
        List<RestaurantTable> busyTables = overlapping.stream()
                .map(Reservation::getRestaurantTable)
                .toList();

        List<RestaurantTable> freeTables = allTables.stream()
                .filter(t -> !busyTables.contains(t))
                .toList();
        return findTablesForGroupFromList(freeTables, guests, zone);
    }

    private List<RestaurantTable> findTablesForGroupFromList(List<RestaurantTable> freeTables, int guests, Zone zone) {
        // Looking for one table
        Optional<RestaurantTable> single = freeTables.stream()
                .filter(t -> t.getCapacity() >= guests)
                .min(Comparator.comparingInt(RestaurantTable::getCapacity));

        if (single.isPresent()) return Collections.singletonList(single.get());

        // If no single table is found, look for a pair of neighboring tables among the free ones
        for (int i = 0; i < freeTables.size(); i++) {
            for (int j = i + 1; j < freeTables.size(); j++) {
                RestaurantTable t1 = freeTables.get(i);
                RestaurantTable t2 = freeTables.get(j);
                if (t1.getCapacity() + t2.getCapacity() >= guests && calculateDistance(t1, t2) < 150) {
                    return Arrays.asList(t1, t2);
                }
            }
        }
        return Collections.emptyList();
    }

    private double calculateDistance(RestaurantTable t1, RestaurantTable t2) {
        return Math.sqrt(Math.pow(t2.getPosX() - t1.getPosX(), 2) + Math.pow(t2.getPosY() - t1.getPosY(), 2));
    }
}