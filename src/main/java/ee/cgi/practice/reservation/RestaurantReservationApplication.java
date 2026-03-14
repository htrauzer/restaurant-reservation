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
			// Clear old data so we don't have duplicates on restart
        	repository.deleteAll();

			// Zone 1: Main Hall (e.g., 5 tables)
        	repository.save(new RestaurantTable(2, "Main Hall", true, true, false, 50, 50));    //1
			repository.save(new RestaurantTable(2, "Main Hall", true, false, false, 100, 50));	//2
			repository.save(new RestaurantTable(4, "Main Hall", false, true, false, 250, 50));   //3
			repository.save(new RestaurantTable(4, "Main Hall", false, true, false, 50, 150));  //4
			repository.save(new RestaurantTable(6, "Main Hall", false, false, false, 300, 150));   //5
			
			// Zone 2: Private Room (e.g., 4 tables)
			repository.save(new RestaurantTable(4, "Private Room", false, true, true, 50, 100)); //6
			repository.save(new RestaurantTable(2, "Private Room", false, true, false, 300, 100));//7
			repository.save(new RestaurantTable(4, "Private Room", true, true, true, 50, 150));  //8
			repository.save(new RestaurantTable(4, "Private Room", true, true, true, 300, 150)); //9

			// Zone 3: Terrace (e.g., 5 tables)
			repository.save(new RestaurantTable(4, "Terrace", true, true, false, 50, 50));  //10
			repository.save(new RestaurantTable(8, "Terrace", true, false, false, 150, 50)); //11
			repository.save(new RestaurantTable(4, "Terrace", true, true, false, 300, 50)); //12
			repository.save(new RestaurantTable(2, "Terrace", true, true, false, 50, 150));   //13
			repository.save(new RestaurantTable(2, "Terrace", true, true, false, 300, 150));	//14

			System.out.println("Tables have been added to the database!");
		};
	}

}
