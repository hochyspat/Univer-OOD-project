package fitnesbot.services;

import fitnesbot.bot.MessageOutputData;
import fitnesbot.models.MealsInTake;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

import static fitnesbot.bot.CommandHandler.processedRequest;

public class MealService {
    private final MealRepository mealRepository;

    public MealService(MealRepository mealRepository) {
        this.mealRepository = mealRepository;
    }

    public MessageOutputData addMeal(MealsInTake mealInTake, long chatId, String mealType){
        LocalDate currentDate = LocalDate.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd.MM.yyyy");
        String date = currentDate.format(formatter);
        mealRepository.save(mealInTake, chatId, date, mealType);

        return new MessageOutputData("Отлично! Запись в дневник осуществлена."
                + " Введи /help для справки или /menu для выбора команд", chatId);
    }

    public MessageOutputData getMeal(String date, String mealType, long chatId) {
        MealsInTake meal = mealRepository.findByMealType(mealType, date, chatId);

        return new MessageOutputData(
                processedRequest(meal, mealType), chatId);
    }
}
