package ee.cgi.practice.reservation;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class RestaurantController {

    private final TableRepository tableRepository;

    public RestaurantController(TableRepository tableRepository) {
        this.tableRepository = tableRepository;
    }

    @GetMapping("/")
    public String home(Model model) {
            
        List<RestaurantTable> allTables = tableRepository.findAll();
        Random random = new Random();

        for (RestaurantTable table : allTables) {
            List<Integer> randomSlots = new ArrayList<>();
            
            // Loop through hours 12 to 23
            for (int hour = 12; hour <= 23; hour++) {
                // 40% chance that this hour is booked
                if (random.nextInt(10) < 4) { 
                    randomSlots.add(hour);
                    randomSlots.add(hour + 1); // Automatically book next hour
                    hour++;
                }
            }
            // Set the temporary random slots for this page load
            table.setBookedSlots(randomSlots);
        }

        model.addAttribute("tables", allTables);
        return "index"; 
        }
}