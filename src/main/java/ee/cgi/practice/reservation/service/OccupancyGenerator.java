package ee.cgi.practice.reservation.service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;
import java.util.Random;

import org.springframework.stereotype.Service;

import ee.cgi.practice.reservation.model.Reservation;
import ee.cgi.practice.reservation.model.RestaurantTable;
import ee.cgi.practice.reservation.repository.ReservationRepository;
import ee.cgi.practice.reservation.repository.TableRepository;
import jakarta.annotation.PostConstruct;

@Service
public class OccupancyGenerator {

    private final TableRepository tableRepository;
    private final ReservationRepository reservationRepository;
    private final Random random = new Random();

    public OccupancyGenerator(TableRepository tableRepository, ReservationRepository reservationRepository) {
        this.tableRepository = tableRepository;
        this.reservationRepository = reservationRepository;
    }

        @PostConstruct
        public void generateRandomOccupancy() {
            List<RestaurantTable> allTables = tableRepository.findAll();
            LocalDate today = LocalDate.now();

            for (RestaurantTable table : allTables) {
                // Починаємо з відкриття (12:00) і йдемо до закриття (23:00)
                int currentHour = 12;
                
                while (currentHour <= 21) { 
                    // Шанс 60% на бронювання в будь-який вільний проміжок часу
                    if (random.nextDouble() < 0.6) {
                        int duration = 2 + random.nextInt(2); // Бронювання на 2 або 3 години
                        
                        // Перевіряємо, щоб броня не виходила за межі робочого дня (23:00)
                        if (currentHour + duration > 23) {
                            duration = 23 - currentHour;
                        }

                        if (duration >= 1) {
                            createRes(table, today, currentHour, duration);
                        }
                        
                        // Після броні стіл не може бути зайнятий миттєво (даємо 1 годину на прибирання)
                        currentHour += (duration + 1); 
                    } else {
                        // Якщо не заброньовано, пробуємо наступну годину
                        currentHour += 1;
                    }
                }
            }
            System.out.println("DEBUG: Dynamic high-density occupancy generated.");
        }

        private void createRes(RestaurantTable table, LocalDate date, int hour, int duration) {
            LocalDateTime start = LocalDateTime.of(date, LocalTime.of(hour, 0));
            Reservation res = new Reservation();
            res.setRestaurantTable(table);
            res.setCustomerName("Guest " + (random.nextInt(900) + 100));
            res.setStartTime(start);
            res.setEndTime(start.plusHours(duration));
            res.setNumberOfGuests(table.getCapacity());
            
            reservationRepository.save(res);
        }
}