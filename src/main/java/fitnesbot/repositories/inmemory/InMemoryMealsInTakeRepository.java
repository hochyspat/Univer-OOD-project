package fitnesbot.repositories.inmemory;

import fitnesbot.exeptions.mealsintakeerrors.MealsInTakeAlreadyDeletedError;
import fitnesbot.exeptions.mealsintakeerrors.MealsInTakeNotFoundError;
import fitnesbot.exeptions.mealsintakeerrors.UserDiaryNotFoundError;
import fitnesbot.models.MealsInTake;
import fitnesbot.services.enums.MealType;
import fitnesbot.services.MealsInTakeRepository;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class InMemoryMealsInTakeRepository implements MealsInTakeRepository {
    private final Map<Long, Map<String, Map<MealType, MealsInTake>>> usersDiary = new HashMap<>();


    @Override
    public void save(MealsInTake mealInTake, long chatId, String date, MealType mealType) {
        if (usersDiary.containsKey(chatId)) {
            if (usersDiary.get(chatId).containsKey(date)) {
                usersDiary.get(chatId).get(date).put(mealType, mealInTake);
                return;
            }
        }
        Map<MealType, MealsInTake> mapByName = new HashMap<>();
        mapByName.put(mealType, mealInTake);
        Map<String, Map<MealType, MealsInTake>> mapByData = new HashMap<>();
        mapByData.put(date, mapByName);
        usersDiary.put(chatId, mapByData);
    }

    @Override
    public MealsInTake findByMealsInTakeType(MealType mealType, String date, long chatId) {
        Map<String, Map<MealType, MealsInTake>> userDiaryData = usersDiary.get(chatId);
        if (userDiaryData != null) {
            Map<MealType, MealsInTake> mealsByDate = userDiaryData.get(date);
            if (mealsByDate != null) {
                MealsInTake meal = mealsByDate.get(mealType);
                if (meal != null && !meal.isDeleted()) {
                    return meal;
                } else {
                    System.out.println(new MealsInTakeNotFoundError(mealType, date, chatId).getErrorMessage());
                }
            } else {
                System.out.println("MealInTakes by date not found");
                return null;
            }
        } else {
            System.out.println(new UserDiaryNotFoundError(chatId).getErrorMessage());
            return null;
        }
        return null;
    }

    @Override
    public Map<String, List<MealsInTake>> getDataById(long chatId) {
        Map<String, List<MealsInTake>> dataMeal = new HashMap<>();

        Map<String, Map<MealType, MealsInTake>> userMap = usersDiary.get(chatId);
        if (userMap != null) {
            for (Map.Entry<String, Map<MealType, MealsInTake>> entry : userMap.entrySet()) {
                String date = entry.getKey();
                Map<MealType, MealsInTake> mealsMap = entry.getValue();
                List<MealsInTake> mealsInTakes = new ArrayList<>(mealsMap.values());
                dataMeal.put(date, mealsInTakes);
            }
        }

        return dataMeal;
    }


    @Override
    public void deleteMealType(MealType mealType, String date, long chatId) {
        Map<String, Map<MealType, MealsInTake>> userDiary = usersDiary.get(chatId);
        if (userDiary != null) {
            Map<MealType, MealsInTake> mealsByDate = userDiary.get(date);
            if (mealsByDate != null && mealsByDate.containsKey(mealType)) {
                mealsByDate.remove(mealType);
                if (mealsByDate.isEmpty()) {
                    userDiary.remove(date);
                }
            } else {
                System.out.println(new MealsInTakeAlreadyDeletedError(mealType, date, chatId).getErrorMessage());
            }
        } else {
            System.out.println(new MealsInTakeAlreadyDeletedError(mealType, date, chatId).getErrorMessage());
        }
    }
}
