package fitnesbot.bot;

import fitnesbot.bot.apiparser.JsonSimpleParser;
import fitnesbot.config.MealApiConfig;
import fitnesbot.exeptions.InvalidCommandError;
import fitnesbot.exeptions.InvalidNumberOfArgumentsError;
import fitnesbot.exeptions.NonExistenceUserError;
import fitnesbot.models.Meal;
import fitnesbot.models.Nutrient;
import fitnesbot.models.User;
import fitnesbot.services.*;
import org.json.JSONObject;

import java.util.Map;


public class CommandHandler {
    private CalorieCountingService calorieService;
    private UserService userService;
    private Help help;
    private Menu menu;
    private MealApiConfig mealapiConfig;


    public CommandHandler(Help help, Menu menu,
                          CalorieCountingService caloriesService, UserService userService) {
        this.help = help;
        this.menu = menu;
        this.calorieService = caloriesService;
        this.userService = userService;
        this.mealapiConfig = new MealApiConfig();
    }

    private MessageOutputData firstAcquaintance(long chatId) {
        return new MessageOutputData(help.getHelp() + "\n\n" + "Для начала давай познакомимся,введи команду addUser [имя] [возраст] [рост] [вес]", chatId);
    }

    public MessageOutputData handleMessage(MessageCommandData commandData) {
        Command command = commandData.getCommand();
        long chatId = commandData.getChatId();
        String[] args = command.args();

        switch (command.command()) {
            case "/help":
                return showHelp(chatId);
            case "/start":
                return firstAcquaintance(chatId);
            case "/menu":
                return showMenu(chatId);
            case "addUser":
                if (args.length != 4) {
                    return new MessageOutputData(new InvalidNumberOfArgumentsError("addUser", "[имя]", "[возраст]", "[рост]", "[вес]").getErrorMessage(), chatId);
                }
                return userService.registerUser(args[0], args[1], args[2], args[3], chatId);

            case "addMeals"://например addMeals 100 gram rice,1 cup tea,200 ml milk
                if (args.length < 1) {
                    return new MessageOutputData(new InvalidNumberOfArgumentsError("addMeal", "[ингридиент1], ...", "[ингридиент n],").getErrorMessage(), chatId);
                }
                String appId = mealapiConfig.getMealApiId();
                String appKey = mealapiConfig.getMealApikey();
                MealApiService mealApiService = new MealApiService(appId, appKey);
                JSONObject analyseMeals = mealApiService.analyzeRecipe("breakfast", args);


                return new MessageOutputData(processedRequest(analyseMeals), chatId);
            case "/mycalories":
                User user = userService.getUser(chatId);
                if (user == null) {
                    return new MessageOutputData(new NonExistenceUserError(chatId).getErrorMessage(), chatId);
                }
                return calculateCalories(user, chatId);
            case "/myprofile":
                return showUserById(chatId);
            case "/exit":
                return new MessageOutputData("Finish bot", chatId);
            default:
                return new MessageOutputData(new InvalidCommandError().getErrorMessage(), chatId);
        }
    }

    private MessageOutputData calculateCalories(User user, long chatId) {
        double calories = calorieService.calculate(user.getHeight(), user.getWeight(), user.getAge());
        user.updateCalories(calories);
        return new MessageOutputData("Твоя норма калорий на день: " + user.getCalories(), chatId);
    }

    public MessageOutputData showUserById(long chatId) {
        User user = userService.getUser(chatId);
        if (user != null) {
            return new MessageOutputData(user.getInfo(), chatId);
        } else {
            return new MessageOutputData(new NonExistenceUserError(chatId).getErrorMessage(), chatId);
        }
    }

    public MessageOutputData showHelp(long chatId) {
        return new MessageOutputData(help.getHelp(), chatId);
    }

    public MessageOutputData showMenu(long chatId) {
        return new MessageOutputData(menu.getMenu(), chatId);
    }

    private String processedRequest(JSONObject analyseMeals) {
        JsonSimpleParser parser = new JsonSimpleParser();
        Meal parsedMeal = parser.parse(analyseMeals.toString());
        Map<String, Nutrient> nutrients = parsedMeal.totalNutrients();

        double proteins = nutrients.containsKey("PROCNT") ? nutrients.get("PROCNT").getQuantity() : 0.0;
        double fats = nutrients.containsKey("FAT") ? nutrients.get("FAT").getQuantity() : 0.0;
        double carbs = nutrients.containsKey("CHOCDF") ? nutrients.get("CHOCDF").getQuantity() : 0.0;

        String response = "Калорийность блюда " + parsedMeal.getMealType() + " составляет: " + String.format("%.1f", parsedMeal.getCalories()) + "\n";
        response += "Белки: " + String.format("%.1f", proteins) + "\n";
        response += "Жиры: " + String.format("%.1f", fats) + "\n";
        response += "Углеводы: " + String.format("%.1f", carbs);

        return response;
    }
}
