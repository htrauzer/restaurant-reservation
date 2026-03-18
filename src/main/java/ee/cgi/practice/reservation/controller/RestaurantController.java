package ee.cgi.practice.reservation.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import ee.cgi.practice.reservation.repository.TableRepository;
import ee.cgi.practice.reservation.service.MealService;

@Controller
public class RestaurantController {

    private final TableRepository tableRepository;
    private final MealService mealService;

    public RestaurantController(TableRepository tableRepository, MealService mealService) {
        this.tableRepository = tableRepository;
        this.mealService = mealService;
    }

    @GetMapping("/")
    public String showMap(Model model) {
        model.addAttribute("tables", tableRepository.findAll());
        model.addAttribute("specialMeal", mealService.getMealOfTheDay());
        return "index";
    }
}