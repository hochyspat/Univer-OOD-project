package org.example;
import java.util.HashMap;
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
            if (command.isValid()) {
                executeCommand(command, help, menu);
                if (command.isExit()) {
                    break;
                }
            }
                else {
                    System.out.println("Извини, но я тебя не понял. Для выбора действия введите название действия.");
                }
        }

        }
    private void setUser(String name, String height, String weight, String age){
        User user = new User(name, height, weight, age);
        users.put(name, user);

    }
    public void executeCommand(Command commandData,Help help,Menu menu) {

       String command = commandData.command();
       String[] args = commandData.args();
        switch (command) {
            case "/help":
                help.showHelp();
                break;
            case "/menu":
                menu.showMenu();
                break;
            case "Добавить пользователя":
                addUser();

                break;
            case "Рассчитать КБЖУ"://можно будет запрашивать данные о тек пользователе а не запрашивать каждый раз,доделать
                String height = readData("Твой рост в см:");
                String weight = readData("Твой вес в кг:");
                String age = readData("Твой возраст:");
                calculateCalories(height, weight, age);
                break;
            case "информация":
                if (args.length > 0) {
                    showUserByName(args[0]);
                }
                else
                {
                    System.out.println("Введи имя пользователя для команды 'информация'");
                }
                break;
            case "/exit":
                System.out.print("Finish bot");
                break;
            default:
                System.out.println("Неверная команда.");
        }
    }

    private void addUser(){
        String name = readData("как тебя звать?");
        String height = readData("рост в см:");
        String weight = readData("вес в кг:");
        String age = readData("возраст:");

        setUser(name, height, weight, age);
        System.out.println("Пользователь " + name + " успешно добавлен!");
    }
    private void calculateCalories(String height, String weight, String age) {
        CalorieCountingService countedCalories = new CalorieCountingService();
        countedCalories.startCalculate(height, weight, age);
        System.out.println("Твоя норма калорий на день: " + countedCalories.getCalories());
    }
    public void showUserByName(String name) {
        User user = getUserByName(name);
        if (user != null) {
            user.showUserInfo();
        }
        else {
            System.out.println("ERROR user not found");
        }

    }

    private String readData(String prompt) {
        System.out.println(prompt);
        return in.nextLine();
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
