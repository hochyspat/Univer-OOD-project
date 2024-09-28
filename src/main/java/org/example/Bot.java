package org.example;
import java.util.HashMap;
import java.io.InputStream;
import java.io.PrintStream;
import java.util.Scanner;

public class Bot {
    private HashMap<String,User> users = new HashMap<>();
    Scanner in = new Scanner(System.in);
    public void start() {
        Help help = new Help();
        Menu menu = new Menu();
        help.showHelp();
        while (true) {
            System.out.print("Введите команду: ");
            String userRequest = in.nextLine();
            Command command = new Command(userRequest);
            if (command.isValidCommand()) {
                command.executeCommand(this, help, menu);
                if (userRequest.equals("/exit")) {
                    break;
                }
            }
                else {
                    System.out.println("Извини, но я тебя не понял. Для выбора действия введите название действия.");
                }
        }

        }
    private void setMyName(String name, String height, String weight, String age){
        User user = new User(name, height, weight, age);
        users.put(name, user);
    }

    public void readDataForCalculateCPRF(){
        CalorieCountingService countedCalories = new CalorieCountingService();
        System.out.println("Твой рост в см:");
        String inputHeight = in.nextLine();
        System.out.println("Твой вес в кг:");
        String inputWeight = in.nextLine();
        System.out.println("Твой возраст:");
        String inputAge = in.nextLine();
        countedCalories.startCalculate(inputHeight, inputWeight, inputAge);
        System.out.println("Твоя норма калорий на день:" + " " + countedCalories.getCalories());
    }
    public void readInfoAboutUser(){
        System.out.print("как тебя звать?");
        String name = in.nextLine();
        System.out.print("рост пожалуйста в см");
        String height = in.nextLine();
        System.out.print("теперь что весы говорят в кг ");
        String weight = in.nextLine();
        System.out.print("сколько годиков");
        String age = in.nextLine();
        setMyName(name, height, weight, age);
        System.out.println("Пользователь " + name + " успешно добавлен!");
    }
    public void showUserByName(String name) {
        User user = getUserByName(name);
        if (user != null) {
            user.showInfoUser();
        }
        else {
            System.out.println("ERROR user not found");
        }

    }
    private User getUserByName(String name){
        User user = users.get(name);
        if (user != null) {
           return user;
        }
        else {
            return null;
        }
    }
}
