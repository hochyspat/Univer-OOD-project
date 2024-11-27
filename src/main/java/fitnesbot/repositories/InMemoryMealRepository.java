package fitnesbot.repositories;

import fitnesbot.models.MealsInTake;
import fitnesbot.services.MealRepository;

import java.util.HashMap;
import java.util.Map;

public class InMemoryMealRepository implements MealRepository {
    private final Map<Long, Map<String, Map<String, MealsInTake>>> usersDiary = new HashMap<>();
    private Map<String, Map<String, MealsInTake>> getData = new HashMap<>();
    private Map<String, MealsInTake> getMealType = new HashMap<>();


    @Override
    public void save(MealsInTake mealInTake, long chatId, String date, String mealType) {
        Map<String, MealsInTake> mapByName = new HashMap<>();
        mapByName.put(mealType, mealInTake);
        Map<String, Map<String, MealsInTake>> mapByData = new HashMap<>();
        mapByData.put(date, mapByName);
        usersDiary.put(chatId, mapByData);
    }

    @Override
    public MealsInTake findByMealType(String mealType, String date, long chatId) {
        try {
            getData = usersDiary.get(chatId);
            getMealType = getData.get(date);
            return getMealType.get(mealType);
        } catch (Exception e) {
            return null;
        }
    }

    @Override
    public Map<String, Map<String, MealsInTake>> findByChatId(long chatId){
        try {
            getData = usersDiary.get(chatId);
            return getData;
        } catch (Exception e) {
            return null;
        }
    }

    @Override
    public Map<String, MealsInTake> findByDate(long chatId, String data){
        try {
            getData = usersDiary.get(chatId);
            return getData.get(data);
        } catch (Exception e) {
            return null;
        }
    }


    @Override
    public void deleteMealType(String mealType, String date, long chatId) {
        getData = usersDiary.get(chatId);
        getMealType = getData.get(date);
        getMealType.remove(mealType);
    }
}
