package ee.cgi.practice.reservation.service;

import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import java.util.Map;
import java.util.List;

@Service
public class MealService {

    private final RestTemplate restTemplate;
    private final String API_URL = "https://www.themealdb.com/api/json/v1/1/random.php";

    public MealService(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    public String getMealOfTheDay() {
        try {
            // Отримуємо відповідь від API
            Map<String, Object> response = restTemplate.getForObject(API_URL, Map.class);
            
            // TheMealDB повертає структуру { "meals": [ { "strMeal": "Name", ... } ] }
            List<Map<String, Object>> meals = (List<Map<String, Object>>) response.get("meals");
            
            if (meals != null && !meals.get(0).isEmpty()) {
                String mealName = (String) meals.get(0).get("strMeal");
                String mealThumb = (String) meals.get(0).get("strMealThumb");
                return "Рекомендуємо сьогодні: " + mealName;
            }
        } catch (Exception e) {
            return "Смачного відпочинку у нашому ресторані!"; // Fallback, якщо API не відповідає
        }
        return "Сьогодні особливий день!";
    }
}