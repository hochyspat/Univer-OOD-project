package fitnesbot.bot;

import fitnesbot.Errors;
import fitnesbot.in.ConsoleInputService;
import fitnesbot.in.InputService;
import fitnesbot.out.ConsoleOutputService;
import fitnesbot.out.OutputService;

public class ConsoleBot {
    long CHAT_ID = 12345;
    private CommandHandler commandHandler;
    private InputService inputService;
    private OutputService outputService;
    private Errors error = new Errors();



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
            outputService.output(new MessageData(new Command("Введите команду: "),CHAT_ID));
            String userRequest = inputService.read();
            Command command = new Command(userRequest);
            if (command.isValid()) {
                commandHandler.handleMessage(command, CHAT_ID);
                if (command.isExit()) {
                    break;
                }
            } else {
                outputService.output(new MessageData(new Command(error.invalidCommand()),CHAT_ID));
            }
        }
    }
}