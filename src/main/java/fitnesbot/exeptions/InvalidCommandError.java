package fitnesbot.exeptions;

public class InvalidCommandError  extends UserErrors{
    public InvalidCommandError  () {
        super("Неверная команда");
    }
}
