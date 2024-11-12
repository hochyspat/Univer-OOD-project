package fitnesbot.models;

import javax.swing.*;
import java.util.List;
import java.util.Map;

public class Meal {
    private String mealType;
    private double totalWeight;
    private double calories;
    private List<Nutrient> totalNutrients;

    public List<Nutrient> totalNutrients() {
        return totalNutrients;
    }
    public double getCalories() {
        return calories;
    }
    public String getMealType() {
        return mealType;
    }

    public void setMealType(String newMealType) {
        mealType = newMealType;
    }

    public void setTotalWeight(double totalWeight) {
        this.totalWeight = totalWeight;
    }

    public void setCalories(double calories) {
        this.calories = calories;
    }
    public  void setTotalNutrients(List<Nutrient> totalNutrients) {
        this.totalNutrients = totalNutrients;
    }


}
