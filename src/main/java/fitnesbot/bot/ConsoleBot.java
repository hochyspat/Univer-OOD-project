package fitnesbot.bot;

import fitnesbot.in.ConsoleInputService;
import fitnesbot.in.InputService;
import fitnesbot.out.ConsoleOutputService;
import fitnesbot.out.OutputService;
import fitnesbot.services.CalorieCountingService;
import fitnesbot.services.Help;
import fitnesbot.services.Menu;

public class ConsoleBot {
    long CHAT_ID = 12345;
    private CommandHandler commandHandler;
    private InputService inputService;
    private OutputService outputService;



    public ConsoleBot(ConsoleInputService inputService,
                      ConsoleOutputService outputService,
                      CommandHandler commandHandler) {
        this.inputService = inputService;
        this.outputService = outputService;
        this.commandHandler = commandHandler;
    }
    public void start() {
        commandHandler.showHelp(CHAT_ID);

        while (true) {

            outputService.output(new MessageData("Введите команду: ",CHAT_ID));

            String userRequest = inputService.read();
            Command command = new Command(userRequest);
            if (command.isValid()) {
                commandHandler.handleMessage(command, CHAT_ID);
                if (command.isExit()) {
                    break;
                }
            } else {
                outputService.output(new MessageData("Извини, но я тебя не понял. Для выбора действия введите название действия.",CHAT_ID));
            }
        }
    }
}