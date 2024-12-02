package fitnesbot.services;

import fitnesbot.bot.MessageOutputData;
import fitnesbot.models.MealsInTake;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;


public class MealsInTakeService {
    private final MealsInTakeRepository mealsIntakeRepository;
    private final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd.MM.yyyy");

    public MealsInTakeService(MealsInTakeRepository mealsIntakeRepository) {
        this.mealsIntakeRepository = mealsIntakeRepository;
    }

    public MessageOutputData saveMealIntake(MealsInTake mealInTake, long chatId, MealType mealType) {
        LocalDate currentDate = LocalDate.now();
        String date = currentDate.format(formatter);
        mealsIntakeRepository.save(mealInTake, chatId, date, mealType);

        return new MessageOutputData("Отлично! Запись в дневник осуществлена."
                + " Введи /help для справки или /menu для выбора команд", chatId);
    }

    public MealsInTake getMealsInTake(String date, MealType mealType, long chatId) {
        MealsInTake meal = mealsIntakeRepository.findByMealsInTakeType(mealType, date, chatId);
        if (meal == null) {
            System.out.println("Meal not found for user " + chatId + " on " + date + " for mealType " + mealType);
        }
        return meal;
    }

    public void deleteMealInTake(long chatId, String date, MealType mealType) {
        mealsIntakeRepository.deleteMealType(mealType, date, chatId);
    }


}
