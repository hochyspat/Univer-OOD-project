package fitnesbot.bot;

import fitnesbot.exeptions.commanderrors.InvalidCommandError;
import fitnesbot.in.ConsoleInputService;
import fitnesbot.in.InputService;
import fitnesbot.out.ConsoleOutputService;
import fitnesbot.out.OutputService;

public class ConsoleBot {
    private static final long CHAT_ID = 12345;
    private final CommandHandler commandHandler;
    private final InputService inputService;
    private final OutputService outputService;


    public ConsoleBot(ConsoleInputService inputService,
                      ConsoleOutputService outputService,
                      CommandHandler commandHandler) {
        this.inputService = inputService;
        this.outputService = outputService;
        this.commandHandler = commandHandler;
    }

    public void start() {

        outputService.sendMessage(commandHandler.showHelp(CHAT_ID));

        while (true) {
            outputService.sendMessage(new MessageOutputData("Введите команду: ", CHAT_ID));
            String userRequest = inputService.read();
            Command command = new Command(userRequest);
            if (command.isValid()) {
                MessageOutputData messageOutputData = commandHandler.handleMessage(
                        new MessageCommandData(command, CHAT_ID));
                outputService.sendMessage(messageOutputData);
                if (command.isExit()) {
                    break;
                }
            } else {
                outputService.sendMessage(new MessageOutputData(
                        new InvalidCommandError().getErrorMessage(), CHAT_ID));
            }
        }
    }
}