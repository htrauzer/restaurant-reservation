package ee.cgi.practice.reservation.service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import ee.cgi.practice.reservation.model.RestaurantTable;
import ee.cgi.practice.reservation.model.TableFeature;
import ee.cgi.practice.reservation.model.Zone;
import ee.cgi.practice.reservation.repository.ReservationRepository;
import ee.cgi.practice.reservation.repository.TableRepository;

@Service
public class RecommendationService {

    private final TableRepository tableRepository;
    private final ReservationRepository reservationRepository;

    public RecommendationService(TableRepository tableRepository, ReservationRepository reservationRepository) {
        this.tableRepository = tableRepository;
        this.reservationRepository = reservationRepository;
    }

    /**
     * Рекомендує список найкращих вільних столів на основі параметрів.
     */
    public List<RestaurantTable> recommendTables(int hour, int guests, Zone preferredZone, Set<TableFeature> prefs) {
      
        LocalDateTime targetTime = LocalDateTime.of(LocalDate.now(), LocalTime.of(hour, 0));
        
        
        List<Long> occupiedIds = reservationRepository.findAll().stream()
                .filter(res -> !targetTime.isBefore(res.getStartTime()) && targetTime.isBefore(res.getEndTime()))
                .map(res -> res.getRestaurantTable().getId())
                .collect(Collectors.toList());

       
        return tableRepository.findAll().stream()           
            .filter(t -> !occupiedIds.contains(t.getId()))            
            .filter(t -> t.getCapacity() >= guests)          
            
            .filter(t -> preferredZone == null || t.getZone() == preferredZone)            
            .sorted((t1, t2) -> Integer.compare(
                    calculatePoints(t2, guests, preferredZone, prefs), 
                    calculatePoints(t1, guests, preferredZone, prefs)))
            
            .limit(1)
            .collect(Collectors.toList());
    }

    private int calculatePoints(RestaurantTable table, int guests, Zone zone, Set<TableFeature> prefs) {
        int points = 0;

        // +20 балів за збіг із обраною зоною (актуально при пошуку по всьому залу)
        if (zone != null && table.getZone() == zone) {
            points += 20;
        }

        // +10 балів за ідеальний розмір (без зайвих порожніх місць)
        if (table.getCapacity() == guests) {
            points += 10;
        } else if (table.getCapacity() == guests + 1) {
            points += 5;
        }

        // +5 балів за кожну фішку (Window, Sofa тощо)
        if (prefs != null && table.getFeatures() != null) {
            for (TableFeature pref : prefs) {
                if (table.getFeatures().contains(pref)) {
                    points += 5;
                }
            }
        }

        return points;
    }
}