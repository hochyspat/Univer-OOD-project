package fitnesbot.exeptions.mealsintakeerrors;

public class MealInTakeErrors {
    protected String message;

    public MealInTakeErrors(String message) {
        this.message = message;
    }

    public String getErrorMessage() {
        return message;
    }
}
