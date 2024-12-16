package fitnesbot.bot;

import fitnesbot.config.MealApiConfig;
import fitnesbot.exeptions.commanderrors.InvalidCommandError;
import fitnesbot.exeptions.commanderrors.InvalidNumberOfArgumentsError;
import fitnesbot.exeptions.mealsintakeerrors.MealsInTakeNotFoundError;
import fitnesbot.exeptions.usererrors.NonExistenceUserError;
import fitnesbot.exeptions.apierrors.InputIngredientsError;
import fitnesbot.models.Meal;
import fitnesbot.models.MealsInTake;
import fitnesbot.models.Nutrient;
import fitnesbot.models.ParsedMeal;
import fitnesbot.models.User;
import fitnesbot.models.WaterGoal;
import fitnesbot.services.CalorieCountingService;
import fitnesbot.services.enums.MealType;
import fitnesbot.services.MealsInTakeApiService;
import fitnesbot.services.MealsInTakeService;
import fitnesbot.services.Menu;
import fitnesbot.services.SleepInTakeService;
import fitnesbot.services.TrainingService;
import fitnesbot.services.UserService;
import fitnesbot.services.Help;

import java.util.Arrays;


public class CommandHandler {
    private final CalorieCountingService calorieService;
    private final UserService userService;
    private final Help help;
    private final Menu menu;
    private final MealsInTakeApiService mealsIntakeApiService;
    private final MealsInTakeService mealService;
    private final SleepInTakeService sleepService;
    private final TrainingService trainingService;

    public CommandHandler(Help help, Menu menu,
                          CalorieCountingService caloriesService,
                          UserService userService, MealsInTakeService mealService,
                          SleepInTakeService sleepService, TrainingService trainingService) {
        this.help = help;
        this.menu = menu;
        this.calorieService = caloriesService;
        this.userService = userService;
        this.mealService = mealService;
        this.sleepService = sleepService;
        this.trainingService = trainingService;
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
                if (!userService.isExistence(chatId)) {
                    return firstAcquaintance(chatId);
                }
                return showHelp(chatId);
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
                    MealsInTake analyseMeal = mealsIntakeApiService.analyzeRecipe("intake meal",
                            Arrays.copyOfRange(args, 1, args.length));
                    if (analyseMeal == null) {
                        return new MessageOutputData(
                                new InputIngredientsError().getErrorMessage(), chatId);
                    }
                    return new MessageOutputData(
                            processedRequest(analyseMeal, "intake meal"), chatId);
                } catch (Exception e) {
                    System.out.println("Невозможно сделать анализ" + e.getMessage());
                    return new MessageOutputData("Невозможно сделать анализ", chatId);
                }
            case "getMeal": // getMeal 27.11.2024 завтрак
                if (args.length != 2) {
                    return new MessageOutputData(new InvalidNumberOfArgumentsError(
                            "getMeal", "[дата]", "[название]").getErrorMessage(), chatId);
                }
                MealType mealType = MealType.fromString(args[1]);
                MealsInTake mealsInTake = mealService.getMealsInTake(args[0], mealType, chatId);
                if (mealsInTake == null) {
                    System.out.println(new MealsInTakeNotFoundError(mealType, args[0], chatId).getErrorMessage());
                    return new MessageOutputData("Meal not found", chatId);

                }
                return new MessageOutputData(processedRequest(mealsInTake, args[1]), chatId);

            case "addMeal": //addMeal завтрак 100 gram rice,1 cup tea,200 ml milk
                if (args.length < 1) {
                    return new MessageOutputData(new InvalidNumberOfArgumentsError("addMeal",
                            "[название] [ингридиент1], ...", "[ингридиент n],").getErrorMessage(), chatId);
                }
                try {
                    String mealTypeFromArgs = args[0];
                    MealType mealTypeAddMeal = MealType.fromString(mealTypeFromArgs);
                    if (mealTypeAddMeal == null) {
                        return new MessageOutputData("Неверный тип приема пищи."
                                + "Возможные названия: Завтрак,Обед,Ужин,Перекус", chatId);
                    }
                    MealsInTake analyseMeal = mealsIntakeApiService.analyzeRecipe(
                            mealTypeFromArgs, Arrays.copyOfRange(args, 1, args.length));
                    if (analyseMeal == null) {
                        return new MessageOutputData(
                                new InputIngredientsError().getErrorMessage(), chatId);
                    }
                    return mealService.saveMealIntake(analyseMeal, chatId, mealTypeAddMeal);
                } catch (Exception e) {
                    System.out.println("Невозможно записать в дневник" + e.getMessage());
                    return new MessageOutputData("Произошла ощибка при записи в дневник", chatId);
                }

            case "addWaterGoal": //например addWaterGoal 2 l
                if (args.length != 2) {
                    return new MessageOutputData(new InvalidNumberOfArgumentsError("addWaterGoal",
                            "количество", "мера измерения").getErrorMessage(), chatId);
                }
                return userService.saveWaterInTakeGoal(chatId, args[0], args[1]);
            case "addWaterInTake":
                return mealService.saveWaterInTake(chatId);
            case "getWaterInTakeInfo":
                if (args.length != 1) {
                    return new MessageOutputData(new InvalidNumberOfArgumentsError("getWaterInTakeInfo",
                            "дата").getErrorMessage(), chatId);
                }
                int countWaterInTake = mealService.getWaterInTake(chatId, args[0]);
                if (countWaterInTake == -1) {
                    return new MessageOutputData("Нет приемов воды по указанной дате" + args[0], chatId);
                }
                User userforWaterStatic = userService.getUser(chatId);
                if (userforWaterStatic != null) {
                    WaterGoal waterGoal = userforWaterStatic.getWaterGoal();
                    double waterCount = userforWaterStatic.getWaterGoal().quantity() - 200 * countWaterInTake;
                    if (waterCount > 0) {
                        return new MessageOutputData("Вам осталось выпить "
                                + waterCount + " " + waterGoal.units(), chatId);
                    }
                    return new MessageOutputData("Поздравляю вы выполнили цель!", chatId);
                }
                return new MessageOutputData(new NonExistenceUserError(chatId).getErrorMessage(),
                        chatId);
            case "addSleepGoal": // addSleepGoal 8 часов
                if (args.length != 2) {
                    return new MessageOutputData(new InvalidNumberOfArgumentsError("addSleepGoal",
                            "количество", "часов").getErrorMessage(), chatId);
                }
                return userService.saveSleepInTakeGoal(chatId, args[0]);
            case "addSleep":
                if (args.length != 1) {
                    return new MessageOutputData(new InvalidNumberOfArgumentsError("addSleep",
                            "количество", "часов").getErrorMessage(), chatId);
                }
                return sleepService.saveSleepInTake(chatId, Double.parseDouble(args[0]));
            case "getWeekSleepStat":
                return sleepService.getWeekSleepStat(chatId);
            case "getSleepChart":
                String imagePath = sleepService.getSleepChart(chatId);
                if (imagePath!=null) {
                    return new MessageOutputData("Вот статистика по сну",chatId,imagePath);
                }
                else {
                    return new MessageOutputData("К сожалению произошла ошибка",chatId);
                }
            case "getDaySleepStat":
                if (args.length != 1) {
                    return new MessageOutputData(new InvalidNumberOfArgumentsError("getDayStat",
                            "дата").getErrorMessage(), chatId);
                }
                return sleepService.getDaySleepStat(chatId, args[0]);
            case "addTraining": // addTraining Бег 1.5 500
                if (args.length != 3) {
                    return new MessageOutputData(new InvalidNumberOfArgumentsError("addTraining",
                            "[название]", "[длительность в часах]", "[сожженные калории]").getErrorMessage(), chatId);
                }
                try {
                    String name = args[0];
                    double trainingTime = Double.parseDouble(args[1]);
                    double calories = Double.parseDouble(args[2]);
                    trainingService.addTraining(chatId, name, trainingTime, calories);
                    return new MessageOutputData("Тренировка успешно добавлена!", chatId);
                } catch (NumberFormatException e) {
                    return new MessageOutputData("Неверный формат чисел.", chatId);
                }
            case "getTrainings":
                return trainingService.getTrainingSessions(chatId);
            case "getTrainingsByDate":
                if (args.length != 1) {
                    return new MessageOutputData(new InvalidNumberOfArgumentsError(
                            "getTrainingsByDate", "[дата]").getErrorMessage(), chatId);
                }
                return trainingService.getTrainingSessionsByDate(chatId, args[0]);
            case "deleteTraining": // deleteTraining 05.12.2024 Бег
                if (args.length != 2) {
                    return new MessageOutputData(new InvalidNumberOfArgumentsError(
                            "deleteTraining", "[название]").getErrorMessage(), chatId);
                }
                return trainingService.deleteTrainingSession(chatId, args[0], args[1]);
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