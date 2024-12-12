package fitnesbot;


import fitnesbot.bot.CommandHandler;
import fitnesbot.bot.TelegramBot;
import fitnesbot.out.ConsoleOutputService;
import fitnesbot.bot.ConsoleBot;
import fitnesbot.in.ConsoleInputService;
import fitnesbot.services.Repositories;
import fitnesbot.services.enums.BotPlatform;
import fitnesbot.services.MealsInTakeService;
import fitnesbot.services.SleepInTakeService;
import fitnesbot.services.TrainingService;
import fitnesbot.services.UserService;
import fitnesbot.services.enums.TypeRepositories;
import org.jetbrains.annotations.NotNull;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import org.telegram.telegrambots.updatesreceivers.DefaultBotSession;
import org.telegram.telegrambots.meta.TelegramBotsApi;

public class Main {
    public static void main(String[] args) throws TelegramApiException {
        BotPlatform platform = BotPlatform.CONSOLE;
        TypeRepositories typeRepositories = TypeRepositories.IN_MEMORY;
        if (args.length > 0) {
            try {
                platform = BotPlatform.fromString(args[0]);
                typeRepositories = TypeRepositories.fromString(args[1]);
            } catch (IllegalArgumentException e) {
                System.out.println("Неверная платформа. Используйте: 'CONSOLE|TELEGRAM IN_MEMORY|DATABASE.");
                return;
            }
        }
        Repositories repositories = Repositories.createRepositories(typeRepositories);

        if (platform == BotPlatform.CONSOLE || platform == BotPlatform.BOTH) {
            new Thread(() -> getConsoleBot(repositories).start()).start();
        }

        if (platform == BotPlatform.TELEGRAM || platform == BotPlatform.BOTH) {
            new Thread(() -> {
                try {
                    TelegramBot bot = getTelegramBot(repositories);
                    new TelegramBotsApi(DefaultBotSession.class).registerBot(bot);
                } catch (TelegramApiException e) {
                    System.out.println("Ошибка Telegram API: " + e.getMessage());
                }
            }).start();
        }

    }

    @NotNull
    private static TelegramBot getTelegramBot(Repositories repositories) {
        CommandHandler commandHandler = new CommandHandler(
                repositories.help(),
                repositories.menu(),
                repositories.calorieCountingService(),
                new UserService(repositories.userRepository()),
                new MealsInTakeService(repositories.mealsIntakeRepository(),
                        repositories.waterInTakeRepository()),
                new SleepInTakeService(repositories.sleepInTakeRepository()),
                new TrainingService(repositories.trainingRepository())
        );
        return new TelegramBot(commandHandler);
    }

    @NotNull
    private static ConsoleBot getConsoleBot(Repositories repositories) {
        ConsoleInputService consoleInputService = new ConsoleInputService();
        ConsoleOutputService consoleOutputService = new ConsoleOutputService();
        CommandHandler commandHandler = new CommandHandler(
                repositories.help(),
                repositories.menu(),
                repositories.calorieCountingService(),
                new UserService(repositories.userRepository()),
                new MealsInTakeService(repositories.mealsIntakeRepository(),
                        repositories.waterInTakeRepository()),
                new SleepInTakeService(repositories.sleepInTakeRepository()),
                new TrainingService(repositories.trainingRepository())
        );
        return new ConsoleBot(consoleInputService, consoleOutputService, commandHandler);
    }
}