package ee.cgi.practice.reservation;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;
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
import ee.cgi.practice.reservation.model.Zone;
import ee.cgi.practice.reservation.repository.ReservationRepository;
import ee.cgi.practice.reservation.repository.TableRepository;
import ee.cgi.practice.reservation.service.ReservationService;

@ExtendWith(MockitoExtension.class)
@DisplayName("ReservationService Test Suite")
class ReservationServiceTest {

    @Mock
    private TableRepository tableRepository;

    @Mock
    private ReservationRepository reservationRepository;

    private ReservationService service;

    @BeforeEach
    void setUp() {
        service = new ReservationService(tableRepository, reservationRepository);
    }

    // ============= MAIN HAPPY PATH TEST =============

    @Test
    @DisplayName("Should find single available table for guest group")
    void findAvailableTables_Should_ReturnSingleAvailableTable() {
        // Arrange: Setup tables and reservation
        RestaurantTable table1 = new RestaurantTable(4, Zone.MAIN_HALL, Set.of(), 0, 0);
        table1.setId(1L);
        
        RestaurantTable table2 = new RestaurantTable(2, Zone.MAIN_HALL, Set.of(), 50, 50);
        table2.setId(2L);

        LocalDateTime requestedStart = LocalDateTime.of(LocalDate.now(), LocalTime.of(14, 0));
        LocalDateTime requestedEnd = requestedStart.plusHours(2);

        // No overlapping reservations
        when(tableRepository.findByZone(Zone.MAIN_HALL)).thenReturn(List.of(table1, table2));
        when(reservationRepository.findOverlappingReservations(requestedStart, requestedEnd))
                .thenReturn(List.of());

        // Act: Search for 2 guests
        List<RestaurantTable> result = service.findAvailableTables(2, Zone.MAIN_HALL, requestedStart);

        // Assert: Should return table2 (smallest fit for 2 guests)
        assertEquals(1, result.size());
        assertEquals(table2, result.get(0));
    }

    // ============= CAPACITY FILTERING TESTS =============

    @Test
    @DisplayName("Should filter tables by minimum required capacity")
    void findAvailableTables_Should_FilterByCapacity() {
        // Arrange
        RestaurantTable smallTable = new RestaurantTable(2, Zone.MAIN_HALL, Set.of(), 0, 0);
        smallTable.setId(1L);
        
        RestaurantTable mediumTable = new RestaurantTable(4, Zone.MAIN_HALL, Set.of(), 50, 50);
        mediumTable.setId(2L);
        
        RestaurantTable largeTable = new RestaurantTable(8, Zone.MAIN_HALL, Set.of(), 100, 100);
        largeTable.setId(3L);

        LocalDateTime start = LocalDateTime.of(LocalDate.now(), LocalTime.of(14, 0));
        LocalDateTime end = start.plusHours(2);

        when(tableRepository.findByZone(Zone.MAIN_HALL))
                .thenReturn(List.of(smallTable, mediumTable, largeTable));
        when(reservationRepository.findOverlappingReservations(start, end))
                .thenReturn(List.of());

        // Act: Search for 6 guests
        List<RestaurantTable> result = service.findAvailableTables(6, Zone.MAIN_HALL, start);

        // Assert: Only largeTable has sufficient capacity
        assertEquals(1, result.size());
        assertEquals(largeTable, result.get(0));
    }

    @Test
    @DisplayName("Should return empty list when no table has sufficient capacity")
    void findAvailableTables_Should_ReturnEmpty_WhenNoCapacity() {
        // Arrange
        RestaurantTable smallTable = new RestaurantTable(2, Zone.MAIN_HALL, Set.of(), 0, 0);
        smallTable.setId(1L);

        LocalDateTime start = LocalDateTime.of(LocalDate.now(), LocalTime.of(14, 0));
        LocalDateTime end = start.plusHours(2);

        when(tableRepository.findByZone(Zone.MAIN_HALL)).thenReturn(List.of(smallTable));
        when(reservationRepository.findOverlappingReservations(start, end))
                .thenReturn(List.of());

        // Act: Search for 10 guests when max is 2
        List<RestaurantTable> result = service.findAvailableTables(10, Zone.MAIN_HALL, start);

        // Assert
        assertTrue(result.isEmpty());
    }

    @Test
    @DisplayName("Should prefer smallest table that fits the group")
    void findAvailableTables_Should_PreferSmallestFittingTable() {
        // Arrange: Multiple tables with sufficient capacity
        RestaurantTable exact4 = new RestaurantTable(4, Zone.MAIN_HALL, Set.of(), 0, 0);
        exact4.setId(1L);
        
        RestaurantTable size6 = new RestaurantTable(6, Zone.MAIN_HALL, Set.of(), 50, 50);
        size6.setId(2L);
        
        RestaurantTable size8 = new RestaurantTable(8, Zone.MAIN_HALL, Set.of(), 100, 100);
        size8.setId(3L);

        LocalDateTime start = LocalDateTime.of(LocalDate.now(), LocalTime.of(14, 0));
        LocalDateTime end = start.plusHours(2);

        when(tableRepository.findByZone(Zone.MAIN_HALL))
                .thenReturn(List.of(size8, size6, exact4)); // Intentionally reversed order
        when(reservationRepository.findOverlappingReservations(start, end))
                .thenReturn(List.of());

        // Act: Search for 4 guests
        List<RestaurantTable> result = service.findAvailableTables(4, Zone.MAIN_HALL, start);

        // Assert: Should return exact4 (smallest fit)
        assertEquals(1, result.size());
        assertEquals(exact4, result.get(0));
    }

    // ============= OCCUPANCY FILTERING TESTS =============

    @Test
    @DisplayName("Should exclude occupied tables from available list")
    void findAvailableTables_Should_ExcludeOccupiedTables() {
        // Arrange
        RestaurantTable table1 = new RestaurantTable(4, Zone.MAIN_HALL, Set.of(), 0, 0);
        table1.setId(1L);
        
        RestaurantTable table2 = new RestaurantTable(4, Zone.MAIN_HALL, Set.of(), 50, 50);
        table2.setId(2L);

        LocalDateTime start = LocalDateTime.of(LocalDate.now(), LocalTime.of(14, 0));
        LocalDateTime end = start.plusHours(2);

        // Create reservation for table1: 14:00 - 16:00
        Reservation reservation = new Reservation();
        reservation.setRestaurantTable(table1);
        reservation.setStartTime(start);
        reservation.setEndTime(end);

        when(tableRepository.findByZone(Zone.MAIN_HALL)).thenReturn(List.of(table1, table2));
        when(reservationRepository.findOverlappingReservations(start, end))
                .thenReturn(List.of(reservation));

        // Act: Search for 4 guests at 14:00
        List<RestaurantTable> result = service.findAvailableTables(4, Zone.MAIN_HALL, start);

        // Assert: Only table2 should be returned
        assertEquals(1, result.size());
        assertEquals(table2, result.get(0));
    }

    @Test
    @DisplayName("Should respect 2-hour booking duration")
    void findAvailableTables_Should_CheckCorrectTimeWindow() {
        // Arrange
        RestaurantTable table = new RestaurantTable(4, Zone.MAIN_HALL, Set.of(), 0, 0);
        table.setId(1L);

        LocalDateTime start = LocalDateTime.of(LocalDate.now(), LocalTime.of(14, 0));

        when(tableRepository.findByZone(Zone.MAIN_HALL)).thenReturn(List.of(table));
        when(reservationRepository.findOverlappingReservations(start, start.plusHours(2)))
                .thenReturn(List.of());

        // Act
        List<RestaurantTable> result = service.findAvailableTables(2, Zone.MAIN_HALL, start);

        // Assert: Should check overlaps for 14:00 - 16:00
        assertEquals(1, result.size());
    }

    // ============= ZONE FILTERING TESTS =============

    @Test
    @DisplayName("Should filter tables by zone")
    void findAvailableTables_Should_FilterByZone() {
        // Arrange: Tables in different zones
        RestaurantTable mainHallTable = new RestaurantTable(4, Zone.MAIN_HALL, Set.of(), 0, 0);
        mainHallTable.setId(1L);
        
        RestaurantTable terraceTable = new RestaurantTable(4, Zone.TERRACE, Set.of(), 50, 50);
        terraceTable.setId(2L);

        LocalDateTime start = LocalDateTime.of(LocalDate.now(), LocalTime.of(14, 0));
        LocalDateTime end = start.plusHours(2);

        when(tableRepository.findByZone(Zone.MAIN_HALL)).thenReturn(List.of(mainHallTable));
        when(reservationRepository.findOverlappingReservations(start, end))
                .thenReturn(List.of());

        // Act: Search specifically in MAIN_HALL
        List<RestaurantTable> result = service.findAvailableTables(4, Zone.MAIN_HALL, start);

        // Assert: Only MAIN_HALL tables considered
        assertEquals(1, result.size());
        assertEquals(Zone.MAIN_HALL, result.get(0).getZone());
    }

    @Test
    @DisplayName("Should return empty list when zone has no tables")
    void findAvailableTables_Should_ReturnEmpty_WhenZoneEmpty() {
        // Arrange
        LocalDateTime start = LocalDateTime.of(LocalDate.now(), LocalTime.of(14, 0));
        LocalDateTime end = start.plusHours(2);

        when(tableRepository.findByZone(Zone.TERRACE)).thenReturn(List.of());
        when(reservationRepository.findOverlappingReservations(start, end))
                .thenReturn(List.of());

        // Act
        List<RestaurantTable> result = service.findAvailableTables(4, Zone.TERRACE, start);

        // Assert
        assertTrue(result.isEmpty());
    }

    // ============= TABLE PAIR MATCHING TESTS =============

    @Test
    @DisplayName("Should find pair of adjacent tables when no single table fits")
    void findAvailableTables_Should_FindAdjacentTablePair() {
        // Arrange: Two small tables close together
        RestaurantTable table1 = new RestaurantTable(4, Zone.MAIN_HALL, Set.of(), 0, 0);
        table1.setId(1L);
        
        RestaurantTable table2 = new RestaurantTable(4, Zone.MAIN_HALL, Set.of(), 100, 100); // Distance: ~141 units
        table2.setId(2L);

        LocalDateTime start = LocalDateTime.of(LocalDate.now(), LocalTime.of(14, 0));
        LocalDateTime end = start.plusHours(2);

        when(tableRepository.findByZone(Zone.MAIN_HALL)).thenReturn(List.of(table1, table2));
        when(reservationRepository.findOverlappingReservations(start, end))
                .thenReturn(List.of());

        // Act: Search for 8 guests (exactly the combined capacity)
        List<RestaurantTable> result = service.findAvailableTables(8, Zone.MAIN_HALL, start);

        // Assert: Should return pair of both tables
        assertEquals(2, result.size());
        assertTrue(result.contains(table1));
        assertTrue(result.contains(table2));
    }

    @Test
    @DisplayName("Should not match distant table pairs (distance >= 150)")
    void findAvailableTables_Should_NotMatch_DistantTables() {
        // Arrange: Two tables far apart (distance > 150)
        RestaurantTable table1 = new RestaurantTable(4, Zone.MAIN_HALL, Set.of(), 0, 0);
        table1.setId(1L);
        
        RestaurantTable table2 = new RestaurantTable(4, Zone.MAIN_HALL, Set.of(), 200, 200); // Distance: ~283 units
        table2.setId(2L);

        LocalDateTime start = LocalDateTime.of(LocalDate.now(), LocalTime.of(14, 0));
        LocalDateTime end = start.plusHours(2);

        when(tableRepository.findByZone(Zone.MAIN_HALL)).thenReturn(List.of(table1, table2));
        when(reservationRepository.findOverlappingReservations(start, end))
                .thenReturn(List.of());

        // Act: Search for 8 guests
        List<RestaurantTable> result = service.findAvailableTables(8, Zone.MAIN_HALL, start);

        // Assert: Should return empty (no valid pair found)
        assertTrue(result.isEmpty());
    }

    @Test
    @DisplayName("Should prefer single table over pair when both available")
    void findAvailableTables_Should_PreferSingleOverPair() {
        // Arrange
        RestaurantTable singleLarge = new RestaurantTable(8, Zone.MAIN_HALL, Set.of(), 0, 0);
        singleLarge.setId(1L);
        
        RestaurantTable small1 = new RestaurantTable(4, Zone.MAIN_HALL, Set.of(), 50, 50);
        small1.setId(2L);
        
        RestaurantTable small2 = new RestaurantTable(4, Zone.MAIN_HALL, Set.of(), 100, 100);
        small2.setId(3L);

        LocalDateTime start = LocalDateTime.of(LocalDate.now(), LocalTime.of(14, 0));
        LocalDateTime end = start.plusHours(2);

        when(tableRepository.findByZone(Zone.MAIN_HALL))
                .thenReturn(List.of(small1, small2, singleLarge)); // Pair would be available first
        when(reservationRepository.findOverlappingReservations(start, end))
                .thenReturn(List.of());

        // Act: Search for 8 guests
        List<RestaurantTable> result = service.findAvailableTables(8, Zone.MAIN_HALL, start);

        // Assert: Should return single large table, not the pair
        assertEquals(1, result.size());
        assertEquals(singleLarge, result.get(0));
    }
}

