package fitnesbot.exeptions.MealInTakeErrors;

public class MealsInTakeNotFoundError extends MealInTakeErrors {
    public MealsInTakeNotFoundError(String mealType, String date, long chatId) {
        super("Meal of type " + mealType + " not found on date " + date + " for user ID " + chatId);
    }
}
