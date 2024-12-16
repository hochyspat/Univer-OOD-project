package fitnesbot.exeptions.mealsintakeerrors;

import fitnesbot.services.enums.MealType;

public class MealsInTakeAlreadyDeletedError extends MealInTakeErrors {
    public MealsInTakeAlreadyDeletedError(MealType mealType, String date, long chatId) {
        super("Meal of type " + mealType + " on date " + date + " for user ID "
                + chatId + " is already marked as deleted.");
    }
}