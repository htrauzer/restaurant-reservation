package ee.cgi.practice.reservation;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
public class RestaurantReservationApplication {

	public static void main(String[] args) {
		SpringApplication.run(RestaurantReservationApplication.class, args);
	}

	@Bean
	public CommandLineRunner demo(TableRepository repository) {
		return (args) -> {
			// Create a few tables for our "Restaurant"
			repository.save(new RestaurantTable(2, "Terrace", true, 50, 50));
			repository.save(new RestaurantTable(4, "Main Hall", false, 150, 50));
			repository.save(new RestaurantTable(6, "Private Room", false, 100, 200));
			
			System.out.println("Tables have been added to the database!");
		};
	}

}
