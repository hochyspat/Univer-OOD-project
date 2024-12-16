package fitnesbot.exeptions.databaseerrors;

public class DataBaseError extends Exception {
    protected String message;

    public DataBaseError(String message) {
        this.message = message;
    }

    public String getErrorMessage() {
        return message;
    }

}
