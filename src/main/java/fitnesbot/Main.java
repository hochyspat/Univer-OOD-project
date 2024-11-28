package fitnesbot;


import fitnesbot.bot.CommandHandler;
import fitnesbot.bot.TelegramBot;
import fitnesbot.out.ConsoleOutputService;
import fitnesbot.bot.ConsoleBot;
import fitnesbot.in.ConsoleInputService;
import fitnesbot.repositories.InMemoryMealsInTakeRepository;
import fitnesbot.repositories.InMemoryUserRepository;
import fitnesbot.services.CalorieCountingService;
import fitnesbot.services.Help;
import fitnesbot.services.MealsInTakeRepository;
import fitnesbot.services.MealsInTakeService;
import fitnesbot.services.Menu;
import fitnesbot.services.UserRepository;
import fitnesbot.services.UserService;
import fitnesbot.services.BotPlatform;
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
        UserRepository userRepository = new InMemoryUserRepository();
        MealsInTakeRepository mealsIntakeRepository = new InMemoryMealsInTakeRepository();
        if (platform == BotPlatform.CONSOLE || platform == BotPlatform.BOTH) {
            Thread consoleThread = new Thread(() -> {
                ConsoleBot consoleBot = getConsoleBot(help, menu, calorieCountingService, userRepository, mealsIntakeRepository);
                consoleBot.start();
            });
            consoleThread.start();
        }
        if (platform == BotPlatform.TELEGRAM || platform == BotPlatform.BOTH) {
            Thread telegramThread = new Thread(() -> {
                try {
                    TelegramBot telegramBot = getTelegramBot(help, menu, calorieCountingService,
                            userRepository, mealsIntakeRepository);
                    TelegramBotsApi telegramBotsApi = new TelegramBotsApi(DefaultBotSession.class);
                    telegramBotsApi.registerBot(telegramBot);

                } catch (TelegramApiException e) {
                    System.out.println("Error with TelegramApi: " + e.getMessage());
                }
            });
            telegramThread.start();

        }


    }

    @NotNull
    private static TelegramBot getTelegramBot(Help help, Menu menu,
                                              CalorieCountingService calorieCountingService,
                                              UserRepository userRepository, MealsInTakeRepository mealsIntakeRepository) {
        UserService userService = new UserService(userRepository);
        MealsInTakeService mealService = new MealsInTakeService(mealsIntakeRepository);
        CommandHandler commandHandler = new CommandHandler(help, menu,
                calorieCountingService,
                userService, mealService);
        return new TelegramBot(commandHandler);
    }

    @NotNull
    private static ConsoleBot getConsoleBot(Help help, Menu menu,
                                            CalorieCountingService calorieCountingService,
                                            UserRepository userRepository, MealsInTakeRepository mealsIntakeRepository) {
        ConsoleInputService consoleInputService = new ConsoleInputService();
        ConsoleOutputService consoleOutputService = new ConsoleOutputService();
        UserService userService = new UserService(userRepository);
        MealsInTakeService mealService = new MealsInTakeService(mealsIntakeRepository);
        CommandHandler commandHandler = new CommandHandler(help, menu,
                calorieCountingService,
                userService, mealService);
        return new ConsoleBot(consoleInputService, consoleOutputService, commandHandler);
    }
}