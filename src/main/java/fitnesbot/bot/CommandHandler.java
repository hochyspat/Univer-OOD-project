package fitnesbot.bot;

import fitnesbot.exeptions.InvalidCommandError;
import fitnesbot.exeptions.InvalidNumberOfArgumentsError;
import fitnesbot.exeptions.NonExistenceUserError;
import fitnesbot.models.User;
import fitnesbot.out.OutputService;
import fitnesbot.services.*;

public class CommandHandler {
    private CalorieCountingService calorieService;
    private UserService userService;
    private Help help;
    private Menu menu;
    private MessageOutputData messageOutputData;

    public CommandHandler(Help help, Menu menu,
                          CalorieCountingService caloriesService, UserService userService) {
        this.help = help;
        this.menu = menu;
        this.calorieService = caloriesService;
        this.userService = userService;
        this.messageOutputData = new MessageOutputData("NULL",12345L);
    }
    private void firstAcquaintance(long chatId) {
        showHelp(chatId);
        messageOutputData = new MessageOutputData("Для начала давай познакомимся,введи команду addПользователь [имя] [возраст] [рост] [вес]", chatId);
    }
    public void handleMessage(MessageCommandData commandData) {
        Command command = commandData.getCommand();
        long chatId = commandData.getChatId();
        String[] args = command.args();
        switch (command.command()) {
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
                if (args.length != 4) {
                    messageOutputData = new MessageOutputData(new InvalidNumberOfArgumentsError("addПользователь", "[имя]", "[возраст]", "[рост]", "[вес]").getErrorMessage(), chatId);
                    return;
                }
                userService.registerUser(args[0], args[1], args[2], args[3], chatId);
                break;
            case "КБЖУ":
                User user = userService.getUser(chatId);
                if (user == null) {
                    messageOutputData = new MessageOutputData(new NonExistenceUserError(chatId).getErrorMessage(), chatId);
                    break;
                }
                calculateCalories(user, chatId);
                break;
            case "информация":
                showUserById(chatId);
                break;
            case "/exit":
                messageOutputData = new MessageOutputData("Finish bot", chatId);
                break;
            default:
                messageOutputData = new MessageOutputData(new InvalidCommandError().getErrorMessage(), chatId);
        }
    }

    private void calculateCalories(User user, long chatId) {
        double calories = calorieService.calculate(user.getHeight(), user.getWeight(), user.getAge());
        user.updateCalories(calories);
        messageOutputData = new MessageOutputData("Твоя норма калорий на день: " + user.getCalories(), chatId);
    }

    public void showUserById(long chatId) {
        User user = userService.getUser(chatId);
        if (user != null) {
            messageOutputData = new MessageOutputData(user.getInfo(), chatId);
        } else {
            messageOutputData = new MessageOutputData(new NonExistenceUserError(chatId).getErrorMessage(), chatId);
        }
    }

    public void showHelp(long chatId) {
        messageOutputData = new MessageOutputData(help.getHelp(), chatId);
    }

    public void showMenu(long chatId) {
        messageOutputData = new MessageOutputData(menu.getMenu(), chatId);
    }

    public MessageOutputData getMessageOutputData() {
        return messageOutputData;
    }
}
