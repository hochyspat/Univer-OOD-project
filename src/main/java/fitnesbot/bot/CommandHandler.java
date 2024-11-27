package fitnesbot.bot;

import fitnesbot.config.MealApiConfig;
import fitnesbot.exeptions.CommandErrors.InvalidCommandError;
import fitnesbot.exeptions.CommandErrors.InvalidNumberOfArgumentsError;
import fitnesbot.exeptions.UserErrors.NonExistenceUserError;
import fitnesbot.exeptions.apiErrors.InputIngredientsError;
import fitnesbot.models.*;
import fitnesbot.services.*;


public class CommandHandler {
    private final CalorieCountingService calorieService;
    private final UserService userService;
    private final Help help;
    private final Menu menu;
    private final MealsInTakeApiService mealsIntakeApiService;
    private final MealsInTakeService mealService;

    public CommandHandler(Help help, Menu menu,
                          CalorieCountingService caloriesService,
                          UserService userService, MealsInTakeService mealService) {
        this.help = help;
        this.menu = menu;
        this.calorieService = caloriesService;
        this.userService = userService;
        this.mealService = mealService;
        MealApiConfig mealapiConfig = new MealApiConfig();
        this.mealsIntakeApiService = new MealsInTakeApiService(mealapiConfig.getMealApiId(),
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

            case "learnMeal"://например learnMeal 100 gram rice,1 cup tea,200 ml milk
                if (args.length < 1) {
                    return new MessageOutputData(new InvalidNumberOfArgumentsError("addMeal",
                            "[название] [ингридиент1], ...", "[ингридиент n],").getErrorMessage(), chatId);
                }
                try {
                    MealsInTake analyseMeal = mealsIntakeApiService.analyzeRecipe("intake meal", args);
                    if (analyseMeal == null) {
                        return new MessageOutputData(
                                new InputIngredientsError().getErrorMessage(), chatId);
                    }
                    return new MessageOutputData(
                            processedRequest(analyseMeal, "intake meal"), chatId);
                } catch (Exception e) {
                    System.out.println("Невозможно сделать анализ");
                }

            case "getMeal": // getMeal 27.11.2024 завтрак
                if (args.length != 2) {
                    return new MessageOutputData(new InvalidNumberOfArgumentsError(
                            "getMeal", "[дата]", "[название]").getErrorMessage(), chatId);
                }
                MealsInTake mealsInTake =  mealService.getMealsInTake(args[0], args[1], chatId);
                return new MessageOutputData(processedRequest(mealsInTake, args[1]), chatId);

            case "addMeal": //addMeal завтрак 100 gram rice,1 cup tea,200 ml milk
                if (args.length < 1) {
                    return new MessageOutputData(new InvalidNumberOfArgumentsError("addMeal",
                            "[название] [ингридиент1], ...", "[ингридиент n],").getErrorMessage(), chatId);
                }
                try {
                    String mealType = command.parseArgsInfo();
                    MealsInTake analyseMeal = mealsIntakeApiService.analyzeRecipe(mealType, args);
                    if (analyseMeal == null) {
                        return new MessageOutputData(
                                new InputIngredientsError().getErrorMessage(), chatId);
                    }

                    return mealService.saveMealIntake(analyseMeal, chatId, mealType);
                } catch (Exception e) {
                    System.out.println("Невозможно записать в дневник");
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

    public static String processedRequest(MealsInTake analyseMeal, String mealType) {
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