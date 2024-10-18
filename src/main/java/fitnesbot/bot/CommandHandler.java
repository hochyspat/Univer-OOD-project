package fitnesbot.bot;

import fitnesbot.Errors;
import fitnesbot.in.InputService;
import fitnesbot.out.OutputService;
import fitnesbot.services.CalorieCountingService;
import fitnesbot.services.Help;
import fitnesbot.services.Menu;

import java.util.HashMap;
import java.util.Map;

public class CommandHandler {
    final int UPPER_HEIGHT_LIMIT = 220;
    final int LOWER_HEIGHT_LIMIT = 140;
    final int UPPER_WEIGHT_LIMIT = 200;
    final int LOWER_WEIGHT_LIMIT = 35;
    final int UPPER_AGE_LIMIT = 100;
    final int LOWER_AGE_LIMIT = 12;
    private Map<String, User> users = new HashMap<>();
    private InputService inputService;
    private OutputService outputService;
    private CalorieCountingService calorieService;
    private Help help;
    private Menu menu;

    public CommandHandler(InputService inputService, OutputService outputService, Help help, Menu menu, CalorieCountingService caloriesService) {
        this.inputService = inputService;
        this.outputService = outputService;
        this.help = help;
        this.menu = menu;
        this.calorieService = caloriesService;
    }



    private void firstAcquaintance(long chatId) {
        outputService.output(new MessageData("Для начала давай познакомимся,введи команду addПользователь [имя] [возраст] [рост] [вес]",chatId));

    }

    public void setUser(String name, String age, String height, String weight,long chatId) {
        int isValid = 1;
        if ((!(isValidName(name)))){
            outputService.output(new MessageData(Errors.INPUT.getErrorMessage(),chatId));
            isValid = 0;
        }
        if ((!(isValidInputParameter(height, LOWER_HEIGHT_LIMIT, UPPER_HEIGHT_LIMIT)))){
            outputService.output(new MessageData(Errors.INPUT.getErrorMessage(),chatId));
            isValid = 0;
        }
        if ((!(isValidInputParameter(weight, LOWER_WEIGHT_LIMIT, UPPER_WEIGHT_LIMIT)))){
            outputService.output(new MessageData(Errors.INPUT.getErrorMessage(),chatId));
            isValid = 0;
        }
        if ((!(isValidInputParameter(age, LOWER_AGE_LIMIT, UPPER_AGE_LIMIT)))){
            outputService.output(new MessageData(Errors.INPUT.getErrorMessage(),chatId));
            isValid = 0;
        }

        if (isValid==1){
            User user = new User(name, Integer.parseInt(height), Integer.parseInt(weight), Integer.parseInt(age),chatId);
            users.put(name, user);
        }




    }

    public void handleMessage(Command commandData,long chatId) {
        String command = commandData.command();
        String[] args = commandData.args();
        switch (command) {
            case "/help":
                showHelp(chatId);
                break;
            case "/start":
                firstAcquaintance(chatId);
            case "/menu":
                showMenu(chatId);
                break;
            case "addПользователь":
                setUser(args[1],args[2],args[3],args[4],chatId);
                break;
            case "КБЖУ":
                User user = getUserById(chatId);
                if (user == null) {
                    outputService.output(new MessageData("Пользователя не существует",chatId));
                    break;
                }
                calculateCalories(user, chatId);
                break;
            case "информация":
                    showUserById(chatId);
                break;
            case "/exit":
                outputService.output(new MessageData("Finish bot",chatId));
                break;
            default:
                outputService.output(new MessageData("Неверная команда.",chatId));
        }
    }

   /* private void addUser() {


        outputService.output("Как тебя зовут?");
        String inputName = inputService.read();
        String name = getValidName(inputName);

        outputService.output("Твой рост в см:");
        String inputHeight = inputService.read();
        int height = getValidParameter(inputHeight, LOWER_HEIGHT_LIMIT, UPPER_HEIGHT_LIMIT);
        outputService.output("Твой вес в кг:");
        String inputWeight = inputService.read();
        int weight = getValidParameter(inputWeight, LOWER_WEIGHT_LIMIT, UPPER_WEIGHT_LIMIT);
        outputService.output("Твой возраст:");
        String inputAge = inputService.read();
        int age = getValidParameter(inputAge, LOWER_AGE_LIMIT, UPPER_AGE_LIMIT);

        setUser(name, height, weight, age);
        outputService.output("Пользователь " + inputName + " успешно добавлен!");
    }
*/
    private boolean isValidName(String inputName) {
        return inputName != null && !inputName.trim().isEmpty();
    }

    /*private String getValidName(String inputName) {
        while (!(isValidName(inputName))) {
            inputName = reEnter();
        }
        return inputName;
    }*/

    private boolean isValidInputParameter(String inputParameter, int lowerBound, int upperBound) {
        if (isNumber(inputParameter)) {
            int result = Integer.parseInt(inputParameter);
            return isInCorrectBounds(result, lowerBound, upperBound);
        }
        return false;
    }

/*private int getValidParameter(String inputParameter, int lowerBound, int upperBound) {
    while (!(isValidInputParameter(inputParameter, lowerBound, upperBound))) {
        inputParameter = reEnter();
    }
    return Integer.parseInt(inputParameter);
}*/

    private boolean isInCorrectBounds(int value, int lowerBound, int upperBound) {
        return value >= lowerBound && value <= upperBound;
    }

    private boolean isNumber(String value) {
        return value.matches("-?\\d+");
    }

    private void calculateCalories(User user, long chatId) {
        double calories = calorieService.calculate(user.getHeight(), user.getWeight(), user.getAge());
        user.updateCalories(calories);
        outputService.output(new MessageData("Твоя норма калорий на день: " + user.getCalories(),chatId));
    }


    public void showUserById(long chatId) {
        User user = getUserById(chatId);
        if (user != null) {
            outputService.output(new MessageData(user.getInfo(),chatId));
        } else {
            outputService.output(new MessageData("ERROR user not found",chatId));
        }

    }

    public void showHelp(long chatId) {
        outputService.output(new MessageData(help.getHelp(),chatId));
    }

    public void showMenu(long chatId) {
        outputService.output(new MessageData(menu.getMenu(),chatId));
    }

    public User getUserById(long chatId) {
        User user = users.get(chatId);
        if (user != null) {
            return user;
        } else {
            return null;
        }
    }
}
