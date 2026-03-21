package ee.cgi.practice.reservation.controller;

import java.util.List;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import ee.cgi.practice.reservation.model.RestaurantTable;
import ee.cgi.practice.reservation.repository.TableRepository;
import ee.cgi.practice.reservation.service.MealService;


// handles the main page, displaying the map with tables and the meal of the day.
@Controller
public class RestaurantController {

    private final TableRepository tableRepository;
    private final MealService mealService;

    public RestaurantController(TableRepository tableRepository, MealService mealService) {
        this.tableRepository = tableRepository;
        this.mealService = mealService;
    }

    //ShowING the main page with the restaurant map and meal of the day
   @GetMapping("/")
    public String showMap(Model model) {
        List<RestaurantTable> allTables = tableRepository.findAll();
        System.out.println("DEBUG: Tables count in DB = " + allTables.size()); // Add this line
        model.addAttribute("tables", allTables);
        model.addAttribute("specialMeal", mealService.getMealOfTheDay());
        return "index";
    }
}