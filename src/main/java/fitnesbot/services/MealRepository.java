package fitnesbot.services;

import fitnesbot.models.MealsInTake;

import java.util.Map;


public interface MealRepository {
    void save(MealsInTake mealInTake, long chatId, String date, String mealType);

    MealsInTake findByMealType(String mealType, String date, long chatId);

    Map<String, Map<String, MealsInTake>> findByChatId(long chatId);

    Map<String, MealsInTake> findByDate(long chatId, String data);

    void deleteMealType(String mealType, String date, long chatId);

}
