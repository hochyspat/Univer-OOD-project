package fitnesbot.models;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.HashMap;
import java.util.Map;


public class Meal {
    private String nameMeal;
    private double weight;
    private Map<String, Nutrient> nutrients = new HashMap<>();

    public Meal(double weight, Map<String, Nutrient> nutrients, String nameMeal) {
        this.nameMeal = nameMeal;
        this.weight = weight;
        this.nutrients = nutrients;
    }

    public Meal() {
    }

    @JsonProperty("foodMatch")
    public String getNameMeal() {
        return nameMeal;
    }

    @JsonProperty("nutrients")
    public Map<String, Nutrient> nutrients() {
        return nutrients;
    }

    @JsonProperty("weight")
    public double getWeight() {
        return weight;
    }

    public void setNameMeal(String nameMeal) {
        this.nameMeal = nameMeal;
    }

    public void setNutrients(Map<String, Nutrient> nutrients) {
        this.nutrients = nutrients;
    }

    public void setWeight(double weight) {
        this.weight = weight;
    }

}
