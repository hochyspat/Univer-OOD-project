package fitnesbot.exeptions;

public class InvalidParameterError extends UserErrors {
    public InvalidParameterError(String parameterName) {
        super("Параметр " + parameterName + " введён неверно.");
    }
}
