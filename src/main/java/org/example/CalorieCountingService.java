package org.example;


import java.util.Scanner;

public class CalorieCountingService {
    private double calories;
    private int proteins;
    private int fats;
    private int carbohydrates;

    public void startCalculate(int height, int weight, int age) {
        calories = (10 * weight + 6.25 * height - 5 * age - 161);
    }

    public double getCalories(){
        return calories;
    }
}
