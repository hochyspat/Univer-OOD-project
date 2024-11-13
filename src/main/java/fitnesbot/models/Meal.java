package fitnesbot.models;

import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class Meal {
    private String mealType;
    private double totalWeight;
    private double calories;
    private Map<String,Nutrient> totalNutrients = new HashMap<>();


    public Meal(String mealType, double totalWeight, double calories, Map<String,Nutrient> totalNutrients) {
        this.mealType = mealType;
        this.totalWeight = totalWeight;
        this.calories = calories;
        this.totalNutrients = totalNutrients;
    }

    public Map<String,Nutrient> totalNutrients() {
        return totalNutrients;
    }
    public double getCalories() {
        return calories;
    }
    public String getMealType() {
        return mealType;
    }
    public double getTotalWeight() {
        return totalWeight;
    }


}
