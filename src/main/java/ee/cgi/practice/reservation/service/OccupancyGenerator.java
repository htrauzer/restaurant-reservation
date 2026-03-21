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

        // This method generates random reservations for the current day to simulate high occupancy. 
        @PostConstruct
        public void generateRandomOccupancy() {
            List<RestaurantTable> allTables = tableRepository.findAll();
            LocalDate today = LocalDate.now();

            for (RestaurantTable table : allTables) {
                //Starting from opening (12:00) and going to closing (23:00)
                int currentHour = 12;
                
                while (currentHour <= 21) { 
                    // 60% chance of booking during any available time slot
                    if (random.nextDouble() < 0.6) {
                        int duration = 2 + random.nextInt(2); // 2 or 3 hours booking
                        
                        // Check that the booking doesn't exceed the working day (23:00)
                        if (currentHour + duration > 23) {
                            duration = 23 - currentHour;
                        }

                        if (duration >= 1) {
                            createRes(table, today, currentHour, duration);
                        }
                        
                        // After booking, the table cannot be booked immediately (we give 1 hour for cleaning)
                        currentHour += (duration + 1); 
                    } else {
                        // If not booked, try the next hour
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