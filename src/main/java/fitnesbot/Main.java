package fitnesbot;


import fitnesbot.bot.CommandHandler;
import fitnesbot.bot.TelegramBot;
import fitnesbot.repositories.InMemoryUserRepository;
import fitnesbot.services.DataBaseService;
import fitnesbot.out.ConsoleOutputService;
import fitnesbot.bot.ConsoleBot;
import fitnesbot.in.ConsoleInputService;
import fitnesbot.repositories.DataBaseUserRepository;
import fitnesbot.repositories.InMemoryMealsInTakeRepository;
import fitnesbot.repositories.InMemorySleepRepository;
import fitnesbot.repositories.InMemoryTrainingRepository;
import fitnesbot.repositories.InMemoryWaterRepository;
import fitnesbot.services.BotPlatform;
import fitnesbot.services.CalorieCountingService;
import fitnesbot.services.Help;
import fitnesbot.services.MealsInTakeRepository;
import fitnesbot.services.MealsInTakeService;
import fitnesbot.services.Menu;
import fitnesbot.services.SleepInTakeRepository;
import fitnesbot.services.SleepInTakeService;
import fitnesbot.services.TrainingRepository;
import fitnesbot.services.TrainingService;
import fitnesbot.services.UserRepository;
import fitnesbot.services.UserService;
import fitnesbot.services.WaterInTakeRepository;
import org.jetbrains.annotations.NotNull;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import org.telegram.telegrambots.updatesreceivers.DefaultBotSession;
import org.telegram.telegrambots.meta.TelegramBotsApi;

public class Main {
    public static void main(String[] args) throws TelegramApiException {
        BotPlatform platform = BotPlatform.CONSOLE;
        if (args.length > 0) {
            try {
                platform = BotPlatform.fromString(args[0]);
            } catch (IllegalArgumentException e) {
                System.out.println("Неверная платформа. Используйте 'CONSOLE' или 'TELEGRAM'.");
                return;
            }
        }
        Help help = new Help();
        Menu menu = new Menu();
        CalorieCountingService calorieCountingService = new CalorieCountingService();
        UserRepository userRepository = new DataBaseUserRepository();
        UserRepository userRepositoryforConsole = new InMemoryUserRepository();
        MealsInTakeRepository mealsIntakeRepository = new InMemoryMealsInTakeRepository();
        SleepInTakeRepository sleepInTakeRepository = new InMemorySleepRepository();
        WaterInTakeRepository waterInTakeRepository = new InMemoryWaterRepository();
        TrainingRepository trainingRepository = new InMemoryTrainingRepository();
        if (platform == BotPlatform.CONSOLE || platform == BotPlatform.BOTH) {
            Thread consoleThread = new Thread(() -> {
                ConsoleBot consoleBot = getConsoleBot(
                        help, menu, calorieCountingService,
                        userRepositoryforConsole, mealsIntakeRepository, sleepInTakeRepository,
                        waterInTakeRepository, trainingRepository
                );
                consoleBot.start();
            });
            DataBaseService dataBaseService = new DataBaseService();
            dataBaseService.createAllTable();
            consoleThread.start();
        }
        if (platform == BotPlatform.TELEGRAM || platform == BotPlatform.BOTH) {
            Thread telegramThread = new Thread(() -> {
                try {
                    TelegramBot telegramBot = getTelegramBot(
                            help, menu, calorieCountingService,
                            userRepository, mealsIntakeRepository, sleepInTakeRepository,
                            waterInTakeRepository, trainingRepository);
                    TelegramBotsApi telegramBotsApi = new TelegramBotsApi(DefaultBotSession.class);
                    telegramBotsApi.registerBot(telegramBot);

                } catch (TelegramApiException e) {
                    System.out.println("Error with TelegramApi: " + e.getMessage());
                }
            });
            DataBaseService dataBaseService = new DataBaseService();
            dataBaseService.createAllTable();
            telegramThread.start();

        }
    }

    @NotNull
    private static TelegramBot getTelegramBot(
            Help help, Menu menu,
            CalorieCountingService calorieCountingService,
            UserRepository userRepository, MealsInTakeRepository mealsIntakeRepository,
            SleepInTakeRepository sleepInTakeRepository,
            WaterInTakeRepository waterInTakeRepository,
            TrainingRepository trainingRepository
    ) {
        UserService userService = new UserService(userRepository);
        SleepInTakeService sleepService = new SleepInTakeService(sleepInTakeRepository);
        TrainingService trainingService = new TrainingService(trainingRepository);
        MealsInTakeService mealService = new MealsInTakeService(mealsIntakeRepository,
                waterInTakeRepository);
        CommandHandler commandHandler = new CommandHandler(help, menu,
                calorieCountingService,
                userService, mealService, sleepService, trainingService);
        return new TelegramBot(commandHandler);
    }

    @NotNull
    private static ConsoleBot getConsoleBot(
            Help help, Menu menu,
            CalorieCountingService calorieCountingService,
            UserRepository userRepository, MealsInTakeRepository mealsIntakeRepository,
            SleepInTakeRepository sleepInTakeRepository,
            WaterInTakeRepository waterInTakeRepository,
            TrainingRepository trainingRepository
    ) {
        ConsoleInputService consoleInputService = new ConsoleInputService();
        ConsoleOutputService consoleOutputService = new ConsoleOutputService();
        UserService userService = new UserService(userRepository);
        TrainingService trainingService = new TrainingService(trainingRepository);
        MealsInTakeService mealService = new MealsInTakeService(mealsIntakeRepository,
                waterInTakeRepository);
        SleepInTakeService sleepService = new SleepInTakeService(sleepInTakeRepository);
        CommandHandler commandHandler = new CommandHandler(help, menu,
                calorieCountingService,
                userService, mealService,
                sleepService, trainingService);
        return new ConsoleBot(consoleInputService, consoleOutputService, commandHandler);
    }
}