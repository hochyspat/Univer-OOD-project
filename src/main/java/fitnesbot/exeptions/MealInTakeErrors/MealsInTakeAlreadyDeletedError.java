package fitnesbot.exeptions.MealInTakeErrors;

public class MealsInTakeAlreadyDeletedError extends MealInTakeErrors {
    public MealsInTakeAlreadyDeletedError(String mealType, String date, long chatId) {
        super("Meal of type " + mealType + " on date " + date + " for user ID " + chatId + " is already marked as deleted.");
    }
}