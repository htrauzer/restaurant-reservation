package ee.cgi.practice.reservation.service;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;

import ee.cgi.practice.reservation.model.Reservation;
import ee.cgi.practice.reservation.model.RestaurantTable;
import ee.cgi.practice.reservation.model.Zone;
import ee.cgi.practice.reservation.repository.TableRepository;

@Service
public class ReservationService {

    private final TableRepository tableRepository;

    public ReservationService(TableRepository tableRepository) {
        this.tableRepository = tableRepository;
    }

    public List<RestaurantTable> findAvailableTables(int guests, Zone zone, LocalDate date, int time) {
    
    List<RestaurantTable> allTables = tableRepository.findByZone(zone);
    
    List<Reservation> existingReservations = reservationRepository.findByBookingDate(date);

    List<RestaurantTable> busyTables = existingReservations.stream()
        .filter(r -> time >= r.getStartTime() && time < r.getEndTime())
        .map(Reservation::getRestaurantTable)
        .toList();

    List<RestaurantTable> freeTables = allTables.stream()
        .filter(t -> !busyTables.contains(t))
        .toList();

    return searchLogic(freeTables, guests); 
}

    private List<RestaurantTable> findCombinedTables(int guests, Zone zone) {
        List<RestaurantTable> tablesInZone = tableRepository.findByZone(zone);
        
        for (int i = 0; i < tablesInZone.size(); i++) {
            for (int j = i + 1; j < tablesInZone.size(); j++) {
                RestaurantTable t1 = tablesInZone.get(i);
                RestaurantTable t2 = tablesInZone.get(j);

                if (t1.getCapacity() + t2.getCapacity() >= guests) {
                    // Check if they are "adjacent" (distance less than 150 units)
                    if (calculateDistance(t1, t2) < 150) { 
                        return Arrays.asList(t1, t2);
                    }
                }
            }
        }
        return Collections.emptyList(); // No combination fits
    }

    private double calculateDistance(RestaurantTable t1, RestaurantTable t2) {
        return Math.sqrt(Math.pow(t2.getPosX() - t1.getPosX(), 2) + 
                         Math.pow(t2.getPosY() - t1.getPosY(), 2));
    }
}