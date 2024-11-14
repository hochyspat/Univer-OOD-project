package fitnesbot.exeptions;

public class InvalidCommandError extends CommandErrors {
    public InvalidCommandError() {
        super("Неверная команда");
    }
}
