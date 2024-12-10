package fitnesbot.services;

import fitnesbot.models.MealsInTake;


public interface MealsInTakeRepository {
    void save(MealsInTake mealInTake, long chatId, String date, MealType mealType);

    MealsInTake findByMealsInTakeType(MealType mealType, String date, long chatId);

    void deleteMealType(MealType mealType, String date, long chatId);

}
