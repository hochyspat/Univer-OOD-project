package fitnesbot.exeptions.CommandErrors;

public class CommandErrors {
    protected String message;

    public CommandErrors(String message) {
        this.message = message;
    }

    public String getErrorMessage() {
        return message;
    }
}