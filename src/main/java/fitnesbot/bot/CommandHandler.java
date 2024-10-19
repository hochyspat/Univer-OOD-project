package fitnesbot.bot;

import fitnesbot.Errors;
import fitnesbot.in.InputService;
import fitnesbot.models.User;
import fitnesbot.out.OutputService;
import fitnesbot.services.*;

import java.util.HashMap;
import java.util.Map;

public class CommandHandler {
    private InputService inputService;
    private OutputService outputService;
    private CalorieCountingService calorieService;
    private UserService userService;
    private Help help;
    private Menu menu;

    public CommandHandler(InputService inputService, OutputService outputService, Help help, Menu menu,
                          CalorieCountingService caloriesService, UserService userService) {
        this.inputService = inputService;
        this.outputService = outputService;
        this.help = help;
        this.menu = menu;
        this.calorieService = caloriesService;
        this.userService = userService;
    }
    private void firstAcquaintance(long chatId) {
        showHelp(chatId);
        outputService.output(new MessageData("Для начала давай познакомимся,введи команду addПользователь [имя] [возраст] [рост] [вес]",chatId));
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
                break;
            case "/menu":
                showMenu(chatId);
                break;
            case "addПользователь":
                userService.registerUser(args[0],args[1],args[2],args[3],chatId);
                break;
            case "КБЖУ":
                User user = userService.getUser(chatId);
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

    private void calculateCalories(User user, long chatId) {
        double calories = calorieService.calculate(user.getHeight(), user.getWeight(), user.getAge());
        user.updateCalories(calories);
        outputService.output(new MessageData("Твоя норма калорий на день: " + user.getCalories(),chatId));
    }


    public void showUserById(long chatId) {
        User user = userService.getUser(chatId);
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

     /* private void addUser() {//удалим потом


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
    /*private int getValidParameter(String inputParameter, int lowerBound, int upperBound) {
    while (!(isValidInputParameter(inputParameter, lowerBound, upperBound))) {
        inputParameter = reEnter();
    }
    return Integer.parseInt(inputParameter);
}*/
}
