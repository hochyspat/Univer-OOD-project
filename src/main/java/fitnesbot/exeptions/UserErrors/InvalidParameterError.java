package fitnesbot.exeptions.UserErrors;

public class InvalidParameterError extends UserErrors {
    public InvalidParameterError(String parameterName) {
        super("Параметр " + parameterName + " введён неверно.");
    }
}
