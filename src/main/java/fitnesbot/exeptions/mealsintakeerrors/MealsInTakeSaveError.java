package fitnesbot.exeptions.mealsintakeerrors;

public class MealsInTakeSaveError extends MealInTakeErrors {
    public MealsInTakeSaveError(String mealType, String date, long chatId) {
        super("Failed to save meal of type '" + mealType + "' on date '" + date + "' for user ID " + chatId);
    }
}