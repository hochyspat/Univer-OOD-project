package fitnesbot.models;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.HashMap;
import java.util.Map;


public class Meal {
    private double totalWeight;
    private double calories;
    private Map<String, Nutrient> totalNutrients = new HashMap<>();


    public Meal(double totalWeight, double calories, Map<String, Nutrient> totalNutrients) {
        this.totalWeight = totalWeight;
        this.calories = calories;
        this.totalNutrients = totalNutrients;
    }

    public Meal() {
    }

    @JsonProperty("totalNutrients")
    public Map<String, Nutrient> totalNutrients() {
        return totalNutrients;
    }

    @JsonProperty("calories")
    public double getCalories() {
        return calories;
    }

    @JsonProperty("totalWeight")
    public double getTotalWeight() {
        return totalWeight;
    }

    public void setCalories(double calories) {
        this.calories = calories;
    }

    public void setTotalNutrients(Map<String, Nutrient> totalNutrients) {
        this.totalNutrients = totalNutrients;
    }

    public void setTotalWeight(double totalWeight) {
        this.totalWeight = totalWeight;
    }

}
