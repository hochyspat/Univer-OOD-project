package fitnesbot;


import fitnesbot.bot.CommandHandler;
import fitnesbot.bot.TelegramBot;
import fitnesbot.out.*;
import fitnesbot.bot.ConsoleBot;
import fitnesbot.in.ConsoleInputService;
import fitnesbot.repositories.UserRepository;
import fitnesbot.services.*;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import org.telegram.telegrambots.updatesreceivers.DefaultBotSession;
import org.telegram.telegrambots.meta.TelegramBotsApi;

public class Main {
    public static void main(String[] args) throws TelegramApiException {
        String platforma = "co";
        Help help = new Help();
        Menu menu = new Menu();
        CalorieCountingService calorieCountingService = new CalorieCountingService();


        if (platforma.equals("console"))
        {
            ConsoleInputService consoleInputService = new ConsoleInputService();
            ConsoleOutputService consoleOutputService = new ConsoleOutputService();
            UserRepository userRepository = new UserRepository();
            UserService userService = new UserService(userRepository,consoleOutputService);
            CommandHandler commandHandler = new CommandHandler(consoleInputService, consoleOutputService,help,menu,calorieCountingService,userService);
            ConsoleBot consoleBot = new ConsoleBot(consoleInputService,consoleOutputService,commandHandler);
            consoleBot.start();
        }
        else {
            try {
                TelegramOutputService telegramOutputService = new TelegramOutputService();
                ConsoleInputService consoleInputService = new ConsoleInputService();
                UserRepository userRepository = new UserRepository();
                UserService userService = new UserService(userRepository,telegramOutputService);
                CommandHandler commandHandler = new CommandHandler(consoleInputService, telegramOutputService, help, menu, calorieCountingService,userService);
                TelegramBot telegramBot = new TelegramBot(telegramOutputService, commandHandler);
                telegramOutputService.setTelegramBot(telegramBot);
                TelegramBotsApi telegramBotsApi = new TelegramBotsApi(DefaultBotSession.class);
                telegramBotsApi.registerBot(telegramBot);
            }
            catch (TelegramApiException e) {
                e.printStackTrace();
            }

        }


    }
}