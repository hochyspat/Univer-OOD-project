package org.example;


import java.util.Scanner;

public class CalorieCountingService {
    private double calories;
    private int proteins;
    private int fats;
    private int carbohydrates;

    public void startCalculate() {
        Scanner in = new Scanner(System.in);
        System.out.println("Твой рост в см:");
        String inputHeight = in.nextLine();
        int height = validInputParameter(inputHeight, 220, 140);
        System.out.println("Твой вес в кг:");
        String inputWeight = in.nextLine();
        int weight = validInputParameter(inputWeight, 200, 35);
        System.out.println("Твой возраст:");
        String inputAge = in.nextLine();
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
