package org.example;


import java.util.Scanner;

public class CalorieCountingService {
    private double calories;
    private int proteins;
    private int fats;
    private int carbohydrates;

    public void startCalculate(String inputHeight, String inputWeight,String inputAge) {
        int height = validInputParameter(inputHeight, 220, 140);
        int weight = validInputParameter(inputWeight, 200, 35);
        int age = validInputParameter(inputAge, 100, 12);
        calculateCalories(height, weight, age);
    }

    private int validInputParameter(String inputData, int less, int more) {
        Scanner in = new Scanner(System.in);
        if (isNumber(inputData))
        {
            int result = Integer.parseInt(inputData);
            if (isCorrectParameter(result, less, more))
                return result;
            else {
                System.out.println(Errors.INPUT.getMassage());
                String inputNewData = in.nextLine();
                return validInputParameter(inputNewData, less, more);
            }
        }
        else {
            System.out.println(Errors.INPUT.getMassage());
            String inputNewData = in.nextLine();
            return validInputParameter(inputNewData, less, more);
        }
    }

    private boolean isCorrectParameter(int value, int less, int more) {
        return value >= more && value <= less;
    }

    private boolean isNumber(String value) {
       return value.matches("[-+]?\\d+");
    }

    private void calculateCalories(int height, int weight, int age)
    {
        calories = (10 * weight + 6.25 * height - 5 * age - 161);
    }

    public double getCalories(){
        return calories;
    }
}
