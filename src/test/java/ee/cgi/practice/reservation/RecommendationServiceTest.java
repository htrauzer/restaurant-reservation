package ee.cgi.practice.reservation;

import java.util.List;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import static org.mockito.Mockito.when;

import ee.cgi.practice.reservation.model.RestaurantTable;
import ee.cgi.practice.reservation.model.TableFeature;
import ee.cgi.practice.reservation.model.Zone;
import ee.cgi.practice.reservation.repository.TableRepository;
import ee.cgi.practice.reservation.service.RecommendationService;

class RecommendationServiceTest {

    @Test
    void shouldRecommendTableWithHighestPoints() {
        // 1. Готуємо фальшиві дані (Mocks)
        TableRepository repository = Mockito.mock(TableRepository.class);
        RecommendationService service = new RecommendationService(repository);

        // Стіл №1: Ідеально підходить (Тераса, Вікно)
        RestaurantTable table1 = new RestaurantTable(2, Zone.TERRACE, Set.of(TableFeature.WINDOW), 0, 0);
        // Стіл №2: Не та зона, немає преференцій
        RestaurantTable table2 = new RestaurantTable(2, Zone.MAIN_HALL, Set.of(), 100, 100);

        when(repository.findAll()).thenReturn(List.of(table1, table2));

        // 2. Виконуємо дію: клієнт хоче 2 місця на Терасі біля Вікна
        RestaurantTable result = service.recommendTable(2, Zone.TERRACE, Set.of(TableFeature.WINDOW));

        // 3. Перевірка: система має обрати Стіл №1
        assertEquals(Zone.TERRACE, result.getZone());
        assertEquals(table1, result);
    }
}