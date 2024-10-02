package org.example;


public class CalorieCountingService {


    public double calculate(int height, int weight, int age) {
        return (10 * weight + 6.25 * height - 5 * age - 161);
    }



}
