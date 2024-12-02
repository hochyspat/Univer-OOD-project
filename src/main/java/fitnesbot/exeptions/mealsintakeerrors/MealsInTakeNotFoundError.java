package fitnesbot.exeptions.mealsintakeerrors;

import fitnesbot.services.MealType;

public class MealsInTakeNotFoundError extends MealInTakeErrors {
    public MealsInTakeNotFoundError(MealType mealType, String date, long chatId) {
        super("Meal of type " + mealType + " not found on date " + date + " for user ID " + chatId);
    }
}
