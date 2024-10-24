package fitnesbot;


import fitnesbot.bot.CommandHandler;
import fitnesbot.bot.TelegramBot;
import fitnesbot.out.*;
import fitnesbot.bot.ConsoleBot;
import fitnesbot.in.ConsoleInputService;
import fitnesbot.repositories.UserRepository;
import fitnesbot.in.InputService;
import fitnesbot.services.*;
import org.jetbrains.annotations.NotNull;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import org.telegram.telegrambots.updatesreceivers.DefaultBotSession;
import org.telegram.telegrambots.meta.TelegramBotsApi;

public class Main {
    public static void main(String[] args) throws TelegramApiException {
        BotPlatform platform = BotPlatform.TELEGRAM;
        Help help = new Help();
        Menu menu = new Menu();
        CalorieCountingService calorieCountingService = new CalorieCountingService();


        if (platform == BotPlatform.CONSOLE)
        {
            ConsoleBot consoleBot = getConsoleBot(help, menu, calorieCountingService);
            consoleBot.start();
        }
        else {
            try {
                TelegramBot telegramBot = getTelegramBot(help, menu, calorieCountingService);
                TelegramBotsApi telegramBotsApi = new TelegramBotsApi(DefaultBotSession.class);
                telegramBotsApi.registerBot(telegramBot);
            }
            catch (TelegramApiException e) {
                e.printStackTrace();
            }

        }


    }

    @NotNull
    private static TelegramBot getTelegramBot(Help help, Menu menu, CalorieCountingService calorieCountingService) {
        TelegramOutputService telegramOutputService = new TelegramOutputService();
        ConsoleInputService consoleInputService = new ConsoleInputService();
        UserRepository userRepository = new UserRepository();
        UserService userService = new UserService(userRepository,telegramOutputService);
        CommandHandler commandHandler = new CommandHandler(consoleInputService, telegramOutputService, help, menu, calorieCountingService,userService);
        TelegramBot telegramBot = new TelegramBot(telegramOutputService, commandHandler);
        telegramOutputService.setTelegramBot(telegramBot);
        return telegramBot;
    }

    @NotNull
    private static ConsoleBot getConsoleBot(Help help, Menu menu, CalorieCountingService calorieCountingService) {
        ConsoleInputService consoleInputService = new ConsoleInputService();
        ConsoleOutputService consoleOutputService = new ConsoleOutputService();
        UserRepository userRepository = new UserRepository();
        UserService userService = new UserService(userRepository,consoleOutputService);
        CommandHandler commandHandler = new CommandHandler(consoleInputService, consoleOutputService, help, menu, calorieCountingService,userService);
        ConsoleBot consoleBot = new ConsoleBot(consoleInputService,consoleOutputService,commandHandler);
        return consoleBot;
    }
}