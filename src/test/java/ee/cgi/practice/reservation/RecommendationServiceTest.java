package ee.cgi.practice.reservation;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import static org.mockito.Mockito.when;
import org.mockito.junit.jupiter.MockitoExtension;

import ee.cgi.practice.reservation.model.Reservation;
import ee.cgi.practice.reservation.model.RestaurantTable;
import ee.cgi.practice.reservation.model.TableFeature;
import ee.cgi.practice.reservation.model.Zone;
import ee.cgi.practice.reservation.repository.ReservationRepository;
import ee.cgi.practice.reservation.repository.TableRepository;
import ee.cgi.practice.reservation.service.RecommendationService;

@ExtendWith(MockitoExtension.class)
@DisplayName("RecommendationService Test Suite")
class RecommendationServiceTest {

    @Mock
    private TableRepository tableRepository;

    @Mock
    private ReservationRepository reservationRepository;

    private RecommendationService service;

    @BeforeEach
    void setUp() {
        service = new RecommendationService(tableRepository, reservationRepository);
    }

    // ============= MAIN HAPPY PATH TEST =============

    @Test
    @DisplayName("Should recommend table with highest points - main happy path")
    void shouldRecommendTableWithHighestPoints() {
        // Arrange: Table #1: Perfect match (Terrace, Window, exact capacity)
        // Scores: +20 (zone) + 10 (exact capacity) + 5 (window) = 35 points
        RestaurantTable table1 = new RestaurantTable(2, Zone.TERRACE, Set.of(TableFeature.WINDOW), 0, 0);
        table1.setId(1L);
        
        // Table #2: Wrong zone, no preferences
        // Scores: 0 (wrong zone) + 10 (exact capacity) = 10 points
        RestaurantTable table2 = new RestaurantTable(2, Zone.MAIN_HALL, Set.of(), 100, 100);
        table2.setId(2L);

        when(tableRepository.findAll()).thenReturn(List.of(table1, table2));
        when(reservationRepository.findAll()).thenReturn(List.of());

        // Act: client wants 2 seats on the Terrace near the Window
        List<RestaurantTable> result = service.recommendTables(14, 2, Zone.TERRACE, Set.of(TableFeature.WINDOW));

        // Assert: system should choose Table #1 (highest score)
        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals(table1, result.get(0));
    }

    // ============= CAPACITY FILTERING TESTS =============

    @Test
    @DisplayName("Should filter out tables with insufficient capacity")
    void recommendTable_Should_FilterByCapacity() {
        // Arrange
        RestaurantTable smallTable = new RestaurantTable(2, Zone.MAIN_HALL, Set.of(), 0, 0);
        smallTable.setId(1L);
        
        RestaurantTable mediumTable = new RestaurantTable(4, Zone.MAIN_HALL, Set.of(), 50, 50);
        mediumTable.setId(2L);
        
        RestaurantTable largeTable = new RestaurantTable(8, Zone.MAIN_HALL, Set.of(), 100, 100);
        largeTable.setId(3L);

        when(tableRepository.findAll()).thenReturn(List.of(smallTable, mediumTable, largeTable));
        when(reservationRepository.findAll()).thenReturn(List.of());

        // Act: Search for 6 guests
        List<RestaurantTable> result = service.recommendTables(14, 6, Zone.MAIN_HALL, null);

        // Assert: Only large table should be returned
        assertEquals(1, result.size());
        assertEquals(largeTable, result.get(0));
    }

    @Test
    @DisplayName("Should return empty list when no table has sufficient capacity")
    void recommendTable_Should_ReturnEmpty_WhenNoCapacity() {
        // Arrange
        RestaurantTable smallTable = new RestaurantTable(2, Zone.MAIN_HALL, Set.of(), 0, 0);
        smallTable.setId(1L);

        when(tableRepository.findAll()).thenReturn(List.of(smallTable));
        when(reservationRepository.findAll()).thenReturn(List.of());

        // Act: Search for 10 guests when max capacity is 2
        List<RestaurantTable> result = service.recommendTables(14, 10, Zone.MAIN_HALL, null);

        // Assert
        assertTrue(result.isEmpty());
    }

    // ============= OCCUPANCY FILTERING TESTS =============

    @Test
    @DisplayName("Should exclude occupied tables at requested time")
    void recommendTable_Should_ExcludeOccupiedTables() {
        // Arrange
        RestaurantTable table1 = new RestaurantTable(4, Zone.MAIN_HALL, Set.of(), 0, 0);
        table1.setId(1L);
        
        RestaurantTable table2 = new RestaurantTable(4, Zone.MAIN_HALL, Set.of(), 50, 50);
        table2.setId(2L);

        // Create reservation: 14:00 - 16:00 (occupies table1)
        LocalDateTime start = LocalDateTime.of(LocalDate.now(), LocalTime.of(14, 0));
        LocalDateTime end = start.plusHours(2);
        
        Reservation reservation = new Reservation();
        reservation.setRestaurantTable(table1);
        reservation.setStartTime(start);
        reservation.setEndTime(end);

        when(tableRepository.findAll()).thenReturn(List.of(table1, table2));
        when(reservationRepository.findAll()).thenReturn(List.of(reservation));

        // Act: Search at 14:30 (during reservation)
        List<RestaurantTable> result = service.recommendTables(14, 4, Zone.MAIN_HALL, null);

        // Assert: Only table2 should be returned (table1 is occupied)
        assertEquals(1, result.size());
        assertEquals(table2, result.get(0));
    }

    // ============= ZONE FILTERING TESTS =============

    @Test
    @DisplayName("Should filter by preferred zone when specified")
    void recommendTable_Should_FilterByPreferredZone() {
        // Arrange: Tables in different zones
        RestaurantTable mainHall = new RestaurantTable(4, Zone.MAIN_HALL, Set.of(), 0, 0);
        mainHall.setId(1L);
        
        RestaurantTable terrace = new RestaurantTable(4, Zone.TERRACE, Set.of(), 50, 50);
        terrace.setId(2L);
        
        RestaurantTable privateRoom = new RestaurantTable(4, Zone.PRIVATE_ROOMS, Set.of(), 100, 100);
        privateRoom.setId(3L);

        when(tableRepository.findAll()).thenReturn(List.of(mainHall, terrace, privateRoom));
        when(reservationRepository.findAll()).thenReturn(List.of());

        // Act: Search specifically for Terrace
        List<RestaurantTable> result = service.recommendTables(14, 4, Zone.TERRACE, null);

        // Assert: Only Terrace table should be returned
        assertEquals(1, result.size());
        assertEquals(Zone.TERRACE, result.get(0).getZone());
    }

    @Test
    @DisplayName("Should handle null zone preference")
    void recommendTable_Should_HandleNullZonePreference() {
        // Arrange
        RestaurantTable table1 = new RestaurantTable(4, Zone.MAIN_HALL, Set.of(), 0, 0);
        table1.setId(1L);
        
        RestaurantTable table2 = new RestaurantTable(4, Zone.TERRACE, Set.of(), 50, 50);
        table2.setId(2L);

        when(tableRepository.findAll()).thenReturn(List.of(table1, table2));
        when(reservationRepository.findAll()).thenReturn(List.of());

        // Act: Search with null preferredZone
        List<RestaurantTable> result = service.recommendTables(14, 4, null, null);

        // Assert: Should return highest scoring table regardless of zone
        assertEquals(1, result.size());
    }

    // ============= FEATURE PREFERENCE TESTS =============

    @Test
    @DisplayName("Should handle null feature preferences")
    void recommendTable_Should_HandleNullPreferences() {
        // Arrange
        RestaurantTable table = new RestaurantTable(4, Zone.MAIN_HALL, Set.of(TableFeature.WINDOW), 0, 0);
        table.setId(1L);

        when(tableRepository.findAll()).thenReturn(List.of(table));
        when(reservationRepository.findAll()).thenReturn(List.of());

        // Act: Search with null preferences
        List<RestaurantTable> result = service.recommendTables(14, 4, Zone.MAIN_HALL, null);

        // Assert: Should not crash and return the table
        assertEquals(1, result.size());
    }

    @Test
    @DisplayName("Should handle table with null features set")
    void recommendTable_Should_HandleTableWithNullFeatures() {
        // Arrange
        RestaurantTable table = new RestaurantTable(4, Zone.MAIN_HALL, null, 0, 0); // null features
        table.setId(1L);

        when(tableRepository.findAll()).thenReturn(List.of(table));
        when(reservationRepository.findAll()).thenReturn(List.of());

        // Act: Search with feature preferences
        List<RestaurantTable> result = service.recommendTables(14, 4, Zone.MAIN_HALL, Set.of(TableFeature.WINDOW));

        // Assert: Should not crash and return the table
        assertEquals(1, result.size());
    }

    // ============= EDGE CASES =============

    @Test
    @DisplayName("Should handle empty table list")
    void recommendTable_Should_HandleEmptyTables() {
        // Arrange
        when(tableRepository.findAll()).thenReturn(List.of());
        when(reservationRepository.findAll()).thenReturn(List.of());

        // Act
        List<RestaurantTable> result = service.recommendTables(14, 4, Zone.MAIN_HALL, null);

        // Assert
        assertTrue(result.isEmpty());
    }
}


