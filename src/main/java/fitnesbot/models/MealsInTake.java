package fitnesbot.models;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.util.List;
import java.util.Map;

public class MealsInTake {
    @JsonIgnore
    private Map<String, Nutrient> totalNutrients;

    private double totalWeight;
    private double calories;
    private boolean deleted;
    private List<ParsedMeal> meals;

    public MealsInTake(double totalWeight, double calories, List<ParsedMeal> meals) {
        this.totalWeight = totalWeight;
        this.calories = calories;
        this.meals = meals;
        this.deleted = false;
    }

    public MealsInTake() {
    }

    @JsonProperty("calories")
    public double getCalories() {
        return calories;
    }

    @JsonProperty("totalWeight")
    public double getTotalWeight() {
        return totalWeight;
    }

    @JsonProperty("ingredients")
    public List<ParsedMeal> getMeals() {
        return meals;
    }

    public void setCalories(double calories) {
        this.calories = calories;
    }

    public void setTotalNutrients(List<ParsedMeal> meals) {
        this.meals = meals;
    }

    public void setTotalWeight(double totalWeight) {
        this.totalWeight = totalWeight;
    }
    public boolean isDeleted() {
        return deleted;
    }

    public void setDeleted() {
        this.deleted = true;
    }

    @Override
    public String toString() {
        ObjectMapper objectMapper = new ObjectMapper();
        try {
            return objectMapper.writerWithDefaultPrettyPrinter().writeValueAsString(this);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }

}
