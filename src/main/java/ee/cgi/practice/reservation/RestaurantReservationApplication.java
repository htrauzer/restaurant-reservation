package ee.cgi.practice.reservation;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling // This will allow us to use @Scheduled for automatic table release
public class RestaurantReservationApplication {

    public static void main(String[] args) {
        SpringApplication.run(RestaurantReservationApplication.class, args);
    }
}