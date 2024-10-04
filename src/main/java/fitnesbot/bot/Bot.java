package fitnesbot.bot;
import fitnesbot.*;
import fitnesbot.InOut.InputService;
import fitnesbot.InOut.OutputService;
import fitnesbot.services.CalorieCountingService;
import fitnesbot.services.Help;
import fitnesbot.services.Menu;

import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

public class Bot {
    private Map<String, User> users = new HashMap<>();
    private InputService inputService;
    private OutputService outputService;
    private  CalorieCountingService calorieService;
    private Help help;
    private Menu menu;


    public Bot(InputService inputService, OutputService outputService, Help help, Menu menu,CalorieCountingService caloriesService) {
        this.inputService = inputService;
        this.outputService = outputService;
        this.help = help;
        this.menu = menu;
        this.calorieService = caloriesService;
    }


    public void start() {
        showHelp();
        firstAcquaintance();
        while (true) {

            String userRequest = inputService.read("Введите команду: ");
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
                        outputService.output("Пользователя не существует");
                        break;
                    }
                    calculateCalories(user);

                }
                else {
                    outputService.output("Введите команду КБЖУ [имя]");
                }
                break;
            case "информация":
                if (args.length > 0) {
                    showUserByName(args[0]);
                }
                else
                {
                    outputService.output("Введи имя пользователя для команды 'информация'");
                }
                break;
            case "/exit":
                outputService.output("Finish bot");
                break;
            default:
                outputService.output("Неверная команда.");
        }
    }

    private void addUser(){
        final int UPPER_HEIGHT_LIMIT = 220;
        final int LOWER_HEIGHT_LIMIT = 140;
        final int UPPER_WEIGHT_LIMIT = 200;
        final int LOWER_WEIGHT_LIMIT = 35;
        final int UPPER_AGE_LIMIT = 100;
        final int LOWER_AGE_LIMIT = 12;

        String inputName = inputService.read("Как тебя зовут?");
        String name = getValidName(inputName);
        String inputHeight = inputService.read("Твой рост в см:");
        int height = getValidParameter(inputHeight, LOWER_HEIGHT_LIMIT, UPPER_HEIGHT_LIMIT);
        String inputWeight = inputService.read("Твой вес в кг:");
        int weight = getValidParameter(inputWeight, LOWER_WEIGHT_LIMIT, UPPER_WEIGHT_LIMIT);
        String inputAge = inputService.read("Твой возраст:");
        int age = getValidParameter(inputAge, LOWER_AGE_LIMIT, UPPER_AGE_LIMIT);

        setUser(name, height, weight, age);
        outputService.output("Пользователь " + inputName + " успешно добавлен!");
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

    private boolean isValidInputParameter(String inputParameter, int lowerBound, int upperBound)
    {
        if (isNumber(inputParameter)) {
            int result = Integer.parseInt(inputParameter);
            return isInCorrectBounds(result, lowerBound, upperBound);
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
        return inputService.read("");
    }

    private boolean isInCorrectBounds(int value, int lowerBound, int upperBound) {
        return value >= upperBound && value <= lowerBound;
    }

    private boolean isNumber(String value) {
        return value.matches("-?\\d+");
    }

    private void calculateCalories(User user) {
        double calories = calorieService.calculate(user.getHeight(), user.getWeight(), user.getAge());
        user.updateCalories(calories);
        outputService.output("Твоя норма калорий на день: " + user.getCalories());
    }


    public void showUserByName(String name) {
        User user = getUserByName(name);
        if (user != null) {
            outputService.output(user.getInfo());
        }
        else {
            outputService.output("ERROR user not found");
        }

    }
    public void showHelp()
    {
        outputService.output(help.getHelp());
    }
    public void showMenu()
    {
        outputService.output(menu.getMenu());
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