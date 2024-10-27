package fitnesbot.exeptions;

public abstract class UserErrors {

    protected String message;

    public UserErrors(String message) {
        this.message = message;
    }
    public String getErrorMessage() {
        return message;
    }

}
