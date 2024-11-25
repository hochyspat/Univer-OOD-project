package fitnesbot.bot;

import fitnesbot.config.MealApiConfig;
import fitnesbot.exeptions.InvalidCommandError;
import fitnesbot.exeptions.InvalidNumberOfArgumentsError;
import fitnesbot.exeptions.NonExistenceUserError;
import fitnesbot.models.*;
import fitnesbot.services.*;


public class CommandHandler {
    private final CalorieCountingService calorieService;
    private final UserService userService;
    private final Help help;
    private final Menu menu;
    private final MealApiService mealApiService;

    public CommandHandler(Help help, Menu menu,
                          CalorieCountingService caloriesService,
                          UserService userService) {
        this.help = help;
        this.menu = menu;
        this.calorieService = caloriesService;
        this.userService = userService;
        MealApiConfig mealapiConfig = new MealApiConfig();
        this.mealApiService = new MealApiService(mealapiConfig.getMealApiId(),
                mealapiConfig.getMealApikey());
    }

    private MessageOutputData firstAcquaintance(long chatId) {
        return new MessageOutputData(help.getHelp() + "\n\n"
                + "Для начала давай познакомимся,введи команду addUser [имя] [возраст] [рост] [вес]",
                chatId);
    }

    public MessageOutputData handleMessage(MessageCommandData commandData) {
        Command command = commandData.command();
        long chatId = commandData.chatId();
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
                    return new MessageOutputData(new InvalidNumberOfArgumentsError(
                            "addUser", "[имя]", "[возраст]",
                            "[рост]", "[вес]").getErrorMessage(), chatId);
                }
                return userService.registerUser(args[0], args[1], args[2], args[3], chatId);

            case "addMeals"://например addMeals 100 gram rice,1 cup tea,200 ml milk
                if (args.length < 1) {
                    return new MessageOutputData(new InvalidNumberOfArgumentsError("addMeal",
                            "[ингридиент1], ...", "[ингридиент n],").getErrorMessage(), chatId);
                }
                try {
                    MealsInTake analyseMeal = mealApiService.analyzeRecipe("intake food", args);
                    return new MessageOutputData(
                            processedRequest(analyseMeal, "intake food"), chatId);
                } catch (Exception e) {
                    System.out.println("Невозможно сделать запись в дневник");
                }

            case "/mycalories":
                User user = userService.getUser(chatId);
                if (user == null) {
                    return new MessageOutputData(new NonExistenceUserError(chatId).getErrorMessage(),
                            chatId);
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
        return new MessageOutputData("Твоя норма калорий на день: "
                + user.getCalories(), chatId);
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

    private String processedRequest(MealsInTake analyseMeal, String mealType) {
        StringBuilder response = new StringBuilder("Результаты анализа блюда: ");
        response.append(mealType)
                .append("\n");
        response.append("Общая калорийность: ")
                .append(analyseMeal.getCalories()).append("kcal \n");
        for (ParsedMeal parsedMeal : analyseMeal.getMeals()) {
            response.append("Ингредиент: ").append(parsedMeal.getText()).append("\n");
            for (Meal meal : parsedMeal.getParsedMeals()) {
                Nutrient protein = meal.nutrients().get("PROCNT");
                Nutrient fat = meal.nutrients().get("FAT");
                Nutrient carbs = meal.nutrients().get("CHOCDF");
                response.append("   Белки: ")
                        .append(String.format("%.1f", protein.getQuantity())).append(" ")
                        .append(protein.getUnit().getValue()).append("\n");
                response.append("   Жиры: ")
                        .append(String.format("%.1f", fat.getQuantity())).append(" ")
                        .append(fat.getUnit().getValue()).append("\n");
                response.append("   Углеводы: ")
                        .append(String.format("%.1f", carbs.getQuantity())).append(" ")
                        .append(carbs.getUnit().getValue()).append("\n");
                response.append("\n");
            }
        }
        return response.toString();
    }
}