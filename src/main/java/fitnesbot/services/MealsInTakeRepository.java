package fitnesbot.services;

import fitnesbot.models.MealsInTake;
import fitnesbot.services.enums.MealType;


public interface MealsInTakeRepository {
    void save(MealsInTake mealInTake, long chatId, String date, MealType mealType);

    MealsInTake findByMealsInTakeType(MealType mealType, String date, long chatId);

    void deleteMealType(MealType mealType, String date, long chatId);

}
