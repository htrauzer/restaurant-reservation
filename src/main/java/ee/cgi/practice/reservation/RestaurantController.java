package ee.cgi.practice.reservation;

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
        // This tells Spring to find all tables and send them to the HTML page
        model.addAttribute("tables", tableRepository.findAll());
        return "index"; // This looks for a file named index.html
    }
}