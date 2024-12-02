package fitnesbot.exeptions.mealsintakeerrors;

public class MealsInTakeDeleteError extends MealInTakeErrors {
    public MealsInTakeDeleteError(String mealType, String date, long chatId) {
        super("Failed to delete meal of type '" + mealType + "' on date '" + date + "' for user ID " + chatId);
    }
}