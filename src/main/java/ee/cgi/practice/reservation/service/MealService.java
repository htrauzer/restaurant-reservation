package ee.cgi.practice.reservation.service;

import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

// Service responsible for fetching the meal of the day from an external API and providing a recommendation to customers.
@Service
public class MealService {

    private final RestTemplate restTemplate;
    private final String API_URL = "https://www.themealdb.com/api/json/v1/1/random.php";

    public MealService(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    // Fetches a random meal from TheMealDB API 
    public String getMealOfTheDay() {
        try {
            // Get API response
            Map<String, Object> response = restTemplate.getForObject(API_URL, Map.class);
            
            // TheMealDB returns a structure { "meals": [ { "strMeal": "Name", ... } ] }
            List<Map<String, Object>> meals = (List<Map<String, Object>>) response.get("meals");
            
            if (meals != null && !meals.get(0).isEmpty()) {
                String mealName = (String) meals.get(0).get("strMeal");
                String mealThumb = (String) meals.get(0).get("strMealThumb");
                return "Recommending today: " + mealName;
            }
        } catch (Exception e) {
            return "Enjoy your meal at our restaurant!"; // Fallback, if API doesn't respond
        }
        return "Today is a special day!";
    }
}