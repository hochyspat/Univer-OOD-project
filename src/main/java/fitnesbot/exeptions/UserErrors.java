package fitnesbot.exeptions;

public class UserErrors {

    protected String message;

    public UserErrors(String message) {
        this.message = message;
    }
    public String getErrorMessage() {
        return message;
    }

}
