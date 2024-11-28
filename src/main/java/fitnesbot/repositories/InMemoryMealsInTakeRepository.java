package fitnesbot.repositories;

import fitnesbot.exeptions.MealInTakeErrors.MealsInTakeAlreadyDeletedError;
import fitnesbot.exeptions.MealInTakeErrors.MealsInTakeNotFoundError;
import fitnesbot.exeptions.MealInTakeErrors.UserDiaryNotFoundError;
import fitnesbot.models.MealsInTake;
import fitnesbot.services.MealType;
import fitnesbot.services.MealsInTakeRepository;

import java.util.HashMap;
import java.util.Map;

public class InMemoryMealsInTakeRepository implements MealsInTakeRepository {
    private final Map<Long, Map<String, Map<MealType, MealsInTake>>> usersDiary = new HashMap<>();


    @Override
    public void save(MealsInTake mealInTake, long chatId, String date, MealType mealType) {
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
    public Map<String, Map<MealType, MealsInTake>> findDiaryByChatId(long chatId) {
        Map<String, Map<MealType, MealsInTake>> userMealsData = usersDiary.get(chatId);
        if (userMealsData != null) {
            return userMealsData;
        } else {
            System.out.println(new UserDiaryNotFoundError(chatId).getErrorMessage());
            return null;
        }
    }

    @Override
    public Map<MealType, MealsInTake> findByDate(long chatId, String data) {
        Map<String, Map<MealType, MealsInTake>> userMealsData = usersDiary.get(chatId);
        if (userMealsData != null) {
            Map<MealType, MealsInTake> mealsByDate = userMealsData.get(data);
            if (mealsByDate != null) {
                return mealsByDate;
            } else {
                System.out.println("Meals by date not found");
                return null;
            }
        } else {
            System.out.println(new UserDiaryNotFoundError(chatId).getErrorMessage());
            return null;
        }
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
