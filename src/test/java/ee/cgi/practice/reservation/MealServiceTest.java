package ee.cgi.practice.reservation;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import static org.mockito.ArgumentMatchers.any;
import org.mockito.Mock;
import static org.mockito.Mockito.when;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

import ee.cgi.practice.reservation.service.MealService;

@ExtendWith(MockitoExtension.class)
@DisplayName("MealService (External API) Test Suite")
class MealServiceTest {

    @Mock
    private RestTemplate restTemplate;

    private MealService service;

    @BeforeEach
    void setUp() {
        service = new MealService(restTemplate);
    }

    // ============= HAPPY PATH TEST =============

    @Test
    @DisplayName("Should return meal recommendation when API succeeds")
    void getMealOfTheDay_Should_ReturnMealName_WhenAPISucceeds() {
        // Arrange: Create mock API response matching TheMealDB structure
        Map<String, Object> meal = new HashMap<>();
        meal.put("strMeal", "Pasta Carbonara");
        meal.put("strMealThumb", "https://example.com/meal.jpg");

        Map<String, Object> response = new HashMap<>();
        response.put("meals", List.of(meal));

        when(restTemplate.getForObject("https://www.themealdb.com/api/json/v1/1/random.php", Map.class))
                .thenReturn(response);

        // Act
        String result = service.getMealOfTheDay();

        // Assert
        assertTrue(result.contains("Pasta Carbonara"));
        assertTrue(result.contains("Recommending today:"));
        assertEquals("Recommending today: Pasta Carbonara", result);
    }

    // ============= EXTERNAL API FAILURE TESTS =============

    @Test
    @DisplayName("Should return fallback message when API throws exception")
    void getMealOfTheDay_Should_ReturnFallback_WhenAPIThrowsException() {
        // Arrange
        when(restTemplate.getForObject(any(String.class), any(Class.class)))
                .thenThrow(new RestClientException("Network error"));

        // Act
        String result = service.getMealOfTheDay();

        // Assert: Should return fallback message
        assertEquals("Enjoy your meal at our restaurant!", result);
    }

    @Test
    @DisplayName("Should handle null API response gracefully")
    void getMealOfTheDay_Should_HandleNull_WhenAPIReturnsNull() {
        // Arrange
        when(restTemplate.getForObject(any(String.class), any(Class.class)))
                .thenReturn(null);

        // Act
        String result = service.getMealOfTheDay();

        // Assert: Should return fallback message
        assertEquals("Enjoy your meal at our restaurant!", result);
    }

    @Test
    @DisplayName("Should handle empty meals list")
    void getMealOfTheDay_Should_HandleEmptyMealsList() {
        // Arrange
        Map<String, Object> response = new HashMap<>();
        response.put("meals", new ArrayList<>()); // Empty list

        when(restTemplate.getForObject(any(String.class), any(Class.class)))
                .thenReturn(response);

        // Act
        String result = service.getMealOfTheDay();

        // Assert: Empty list causes IndexOutOfBoundsException, returns fallback
        assertEquals("Enjoy your meal at our restaurant!", result);
    }

    @Test
    @DisplayName("Should handle null meals field in response")
    void getMealOfTheDay_Should_HandleNullMealsField() {
        // Arrange
        Map<String, Object> response = new HashMap<>();
        response.put("meals", null); // Null meals array

        when(restTemplate.getForObject(any(String.class), any(Class.class)))
                .thenReturn(response);

        // Act
        String result = service.getMealOfTheDay();

        // Assert: Null meals causes meals != null to be false, returns "Today is a special day!"
        assertEquals("Today is a special day!", result);
    }

    @Test
    @DisplayName("Should handle missing strMeal field in meal object")
    void getMealOfTheDay_Should_HandleMissingMealName() {
        // Arrange: Meal object without strMeal field
        Map<String, Object> meal = new HashMap<>();
        meal.put("strMealThumb", "https://example.com/meal.jpg");
        // Missing "strMeal" field -> returns null when calling get("strMeal")

        Map<String, Object> response = new HashMap<>();
        response.put("meals", List.of(meal));

        when(restTemplate.getForObject(any(String.class), any(Class.class)))
                .thenReturn(response);

        // Act
        String result = service.getMealOfTheDay();

        // Assert: Still returns "Recommending today: null" (null is concatenated)
        assertEquals("Recommending today: null", result);
    }

    @Test
    @DisplayName("Should handle multiple meals in response (use first)")
    void getMealOfTheDay_Should_ReturnFirstMeal_WhenMultipleAvailable() {
        // Arrange: API returns multiple meals
        Map<String, Object> meal1 = new HashMap<>();
        meal1.put("strMeal", "Pizza");
        meal1.put("strMealThumb", "https://example.com/pizza.jpg");

        Map<String, Object> meal2 = new HashMap<>();
        meal2.put("strMeal", "Burger");
        meal2.put("strMealThumb", "https://example.com/burger.jpg");

        Map<String, Object> response = new HashMap<>();
        response.put("meals", List.of(meal1, meal2));

        when(restTemplate.getForObject(any(String.class), any(Class.class)))
                .thenReturn(response);

        // Act
        String result = service.getMealOfTheDay();

        // Assert: Should return first meal
        assertTrue(result.contains("Pizza"));
        assertEquals("Recommending today: Pizza", result);
    }

    @Test
    @DisplayName("Should handle special characters in meal name")
    void getMealOfTheDay_Should_HandleSpecialCharacters() {
        // Arrange: Meal name with special characters
        Map<String, Object> meal = new HashMap<>();
        meal.put("strMeal", "Pad Thai (ผัดไทย)");
        meal.put("strMealThumb", "https://example.com/meal.jpg");

        Map<String, Object> response = new HashMap<>();
        response.put("meals", List.of(meal));

        when(restTemplate.getForObject(any(String.class), any(Class.class)))
                .thenReturn(response);

        // Act
        String result = service.getMealOfTheDay();

        // Assert
        assertTrue(result.contains("Pad Thai (ผัดไทย)"));
    }

    @Test
    @DisplayName("Should handle empty string meal name")
    void getMealOfTheDay_Should_HandleEmptyMealName() {
        // Arrange
        Map<String, Object> meal = new HashMap<>();
        meal.put("strMeal", ""); // Empty name - but not null, so meal is not empty
        meal.put("strMealThumb", "https://example.com/meal.jpg");

        Map<String, Object> response = new HashMap<>();
        response.put("meals", List.of(meal));

        when(restTemplate.getForObject(any(String.class), any(Class.class)))
                .thenReturn(response);

        // Act
        String result = service.getMealOfTheDay();

        // Assert: Meal map is not empty, so returns recommendation (even with empty name)
        assertEquals("Recommending today: ", result);
    }
}

