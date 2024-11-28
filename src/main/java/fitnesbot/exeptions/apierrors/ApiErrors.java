package fitnesbot.exeptions.apierrors;

public class ApiErrors {
    protected String message;

    public ApiErrors(String message) {
        this.message = message;
    }

    public String getErrorMessage() {
        return message;
    }
}
