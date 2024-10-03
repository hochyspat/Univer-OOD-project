
package org.example;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

public class Bot {
    private Map<String,User> users = new HashMap<>();
    Scanner in = new Scanner(System.in);
    Help help;
    Menu menu;


    public Bot(Help help,Menu menu){
        this.help = help;
        this.menu = menu;
    }


    public void start() {

        showHelp();
        firstAcquaintance();
        while (true) {
            System.out.println("Введите команду: ");
            String userRequest = in.nextLine();
            Command command = new Command(userRequest);
            if (command.isValid()) {
                executeCommand(command);
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

    public void setUser(String name, int height, int weight, int age){
        User user = new User(name, height, weight, age);
        users.put(name, user);
    }

    public void executeCommand(Command commandData) {
        String command = commandData.command();
        String[] args = commandData.args();
        switch (command) {
            case "/help":
                showHelp();
                break;
            case "/menu":
                showMenu();
                break;
            case "addПользователь":
                addUser();
                break;
            case "КБЖУ":
                if (args.length>0) {
                    User user = getUserByName(args[0]);
                    if (user == null) {
                        System.out.println("Пользователя не существует");
                        break;
                    }
                    calculateCalories(user);

                }
                else {
                    System.out.println("Введите команду КБЖУ [имя]");
                }
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
        final int UPPER_HEIGHT_LIMIT = 220;
        final int LOWER_HEIGHT_LIMIT = 140;
        final int UPPER_WEIGHT_LIMIT = 200;
        final int LOWER_WEIGHT_LIMIT = 35;
        final int UPPER_AGE_LIMIT = 100;
        final int LOWER_AGE_LIMIT = 12;

        String inputName = readData("Как тебя зовут?");
        String name = getValidName(inputName);
        String inputHeight = readData("Твой рост в см:");
        int height = getValidParameter(inputHeight, UPPER_HEIGHT_LIMIT, LOWER_HEIGHT_LIMIT);
        String inputWeight = readData("Твой вес в кг:");
        int weight = getValidParameter(inputWeight, UPPER_WEIGHT_LIMIT, LOWER_WEIGHT_LIMIT);
        String inputAge = readData("Твой возраст:");
        int age = getValidParameter(inputAge, UPPER_AGE_LIMIT, LOWER_AGE_LIMIT);

        setUser(name, height, weight, age);
        System.out.println("Пользователь " + inputName + " успешно добавлен!");
    }

    private boolean isValidName(String inputName) {return inputName != null && !inputName.trim().isEmpty();}

    private String getValidName(String inputName)
    {
        while (!(isValidName(inputName)))
        {
            inputName = reEnter();
        }
        return inputName;
    }

    private boolean isValidInputParameter(String inputParameter, int lowerBound, int upperBound) {
        if (isNumber(inputParameter))
        {
            int result = Integer.parseInt(inputParameter);
            if (isInCorrectBounds(result, lowerBound, upperBound))
                return true;
        }
        return false;
    }

    private int getValidParameter(String inputParameter, int lowerBound, int upperBound)
    {
        while (!(isValidInputParameter(inputParameter, lowerBound, upperBound)))
        {
            inputParameter = reEnter();
        }
        return Integer.parseInt(inputParameter);
    }

    private String reEnter() {
        System.out.println(Errors.INPUT.getErrorMessage());
        return in.nextLine();
    }

    private boolean isInCorrectBounds(int value, int lowerBound, int upperBound) {
        return value >= upperBound && value <= lowerBound;
    }

    private boolean isNumber(String value) {
        return value.matches("-?\\d+");
    }

    private void calculateCalories(User user) {
        CalorieCountingService countedCalories = new CalorieCountingService();
        double calories = countedCalories.calculate(user.getHeight(), user.getWeight(), user.getAge());

        user.updateCalories(calories);

        System.out.println("Твоя норма калорий на день: " + user.getCalories());
    }


    public void showUserByName(String name) {
        User user = getUserByName(name);
        if (user != null) {
            System.out.println(user.getInfo());
        }
        else {
            System.out.println("ERROR user not found");
        }

    }
    public void showHelp()
    {
        System.out.print(help.getHelp());
    }
    public void showMenu()
    {
        System.out.print(menu.getMenu());
    }

    private String readData(String prompt) {
        System.out.println(prompt);
        return in.nextLine();
    }
    public User getUserByName(String name){
        User user = users.get(name);
        if (user != null) {
            return user;
        }
        else {
            return null;
        }
    }
}
