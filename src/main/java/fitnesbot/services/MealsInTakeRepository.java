package fitnesbot.services;

import fitnesbot.models.MealsInTake;

import java.util.Map;


public interface MealsInTakeRepository {
    void save(MealsInTake mealInTake, long chatId, String date, MealType mealType);

    MealsInTake findByMealsInTakeType(MealType mealType, String date, long chatId);

    Map<String, Map<MealType, MealsInTake>> findDiaryByChatId(long chatId);

    Map<MealType, MealsInTake> findByDate(long chatId, String data);

    void deleteMealType(MealType mealType, String date, long chatId);

}
