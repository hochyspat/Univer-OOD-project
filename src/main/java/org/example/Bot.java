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
        firstAcquaintance();
        while (true) {
            System.out.println("Введите команду: ");
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

    private void firstAcquaintance() {
        System.out.println("Для начала давай познакомимся");
        addUser();
    }

    private void setUser(String name, int height, int weight, int age){
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
            case "addПользователь":
                addUser();
                break;
            case "КБЖУ"://можно будет запрашивать данные о тек пользователе а не запрашивать каждый раз,доделать
                String userName = readData("Введите имя ползователя, для которого нужно расчитать КБЖУ");
                User user = getUserByName(userName);
                if (user == null) {
                    System.out.println("Пользователя не существует");
                    break;
                }
                calculateCalories(user.height(), user.weight(), user.age());
                break;
            case "информация":
                if (args.length > 0) {
                    System.out.println(args[0]);
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
        final int upperHeightLimit = 220;
        final int lowerHeightLimit = 140;
        final int upperWeightLimit = 200;
        final int lowerWeightLimit = 35;
        final int upperAgeLimit = 100;
        final int lowerAgeLimit = 12;

        String name = readData("Как тебя зовут?");
        String inputHeight = readData("Твой рост в см:");
        int height = validInputParameter(inputHeight, upperHeightLimit, lowerHeightLimit);
        String inputWeight = readData("Твой вес в кг:");
        int weight = validInputParameter(inputWeight, upperWeightLimit, lowerWeightLimit);
        String inputAge = readData("Твой возраст:");
        int age = validInputParameter(inputAge, upperAgeLimit, lowerAgeLimit);

        setUser(name, height, weight, age);
        System.out.println("Пользователь " + name + " успешно добавлен!");
    }
    private int validInputParameter(String inputData, int lowerBound, int upperBound) {
        if (isNumber(inputData))
        {
            int result = Integer.parseInt(inputData);
            if (isInCorrectBounds(result, lowerBound, upperBound))
                return result;
            else {
                String inputNewData = reEnter();
                return validInputParameter(inputNewData, lowerBound, upperBound);
            }
        }
        else {
            String inputNewData = reEnter();
            return validInputParameter(inputNewData, lowerBound, upperBound);
        }
    }

    private String reEnter() {
        System.out.println(Errors.INPUT.getErrorMassage());
        return in.nextLine();
    }

    private boolean isInCorrectBounds(int value, int lowerBound, int upperBound) {
        return value >= upperBound && value <= lowerBound;
    }

    private boolean isNumber(String value) {
        return value.matches("-?\\d+");
    }
    private void calculateCalories(int height, int weight, int age) {
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
