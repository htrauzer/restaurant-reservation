package ee.cgi.practice.reservation.service;

import java.util.List;
import java.util.Set;

import org.springframework.stereotype.Service;

import ee.cgi.practice.reservation.model.RestaurantTable;
import ee.cgi.practice.reservation.model.TableFeature;
import ee.cgi.practice.reservation.model.Zone;
import ee.cgi.practice.reservation.repository.TableRepository;

@Service
public class RecommendationService {

    private final TableRepository tableRepository;

    public RecommendationService(TableRepository tableRepository) {
        this.tableRepository = tableRepository;
    }

    public RestaurantTable recommendTable(int guests, Zone preferredZone, Set<TableFeature> prefs) {
        List<RestaurantTable> allTables = tableRepository.findAll();

        return allTables.stream()
            // Крок 1: Відсіяти занадто малі столи
            .filter(t -> t.getCapacity() >= guests)
            // Крок 2: Нарахувати бали
            .sorted((t1, t2) -> Integer.compare(calculatePoints(t2, guests, preferredZone, prefs), 
                                               calculatePoints(t1, guests, preferredZone, prefs)))
            .findFirst()
            .orElse(null); // Якщо нічого не підходить
    }

    private int calculatePoints(RestaurantTable table, int guests, Zone zone, Set<TableFeature> prefs) {
        int points = 0;

        // +20 балів за правильну зону
        if (table.getZone() == zone) points += 20;

        // +10 балів, якщо стіл ідеально підходить за розміром (не марнуємо місця)
        if (table.getCapacity() == guests) points += 10;
        else if (table.getCapacity() == guests + 1) points += 5;

        // +5 балів за кожну збіжну преференцію (Window, Sofa тощо)
        for (TableFeature pref : prefs) {
            if (table.getFeatures().contains(pref)) {
                points += 5;
            }
        }

        return points;
    }
}