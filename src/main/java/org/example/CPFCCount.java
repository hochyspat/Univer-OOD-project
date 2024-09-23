package org.example;

import java.util.Scanner;

public class CPFCCount {
    private static boolean flag = false;
    private static double calories;
    private static int proteins;
    private static int fats;
    private static int carbohydrates;
    public static void counting(int height, int weight, int age)
    {
        calories = (10 * weight + 6.25 * height - 5 * age - 161);
    }
    public static void start()
    {
        Scanner in = new Scanner(System.in);
        Checker a = new Checker() {
            @Override
            public boolean checkData(int data, int less, int more) {
                if (data >= more && data <= less)
                    return true;
                return false;
            }
        };
        System.out.println("Твой рост в сантиметрах:");
        int height = in.nextInt();
        height = useChecker(a, height, 220, 140);
        System.out.println("Твой вес в килограммах:");
        int weight = in.nextInt();
        weight = useChecker(a, weight, 200, 35);
        System.out.println("Твой возраст:");
        int age = in.nextInt();
        age = useChecker(a, age, 90, 12);
        counting(height, weight, age);
        System.out.println("Ваша норма калорий в день:" + " " + calories);
        flag = true;
    }
    private interface Checker {
        boolean checkData(int data, int less, int more);
    }
    private static int useChecker(Checker checker, int data, int less, int more)
    {
        Scanner in = new Scanner(System.in);
        while (true){
            if (checker.checkData(data, less, more))
                return data;
            else {
                System.out.println("Введены неверные данные. Попробуй еще раз");
                data = in.nextInt();
            }
        }
    }
    private static boolean checkData(int data, int less, int more)
    {
        if (data >= more && data <= less)
            return true;
        return false;
    }
    public static double getCalories()
    {
        return calories;
    }

}
