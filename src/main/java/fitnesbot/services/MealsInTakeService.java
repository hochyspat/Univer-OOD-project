package fitnesbot.services;

import fitnesbot.bot.MessageOutputData;
import fitnesbot.models.MealsInTake;
import fitnesbot.services.enums.MealType;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;


public class MealsInTakeService {
    private final MealsInTakeRepository mealsIntakeRepository;
    private final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd.MM.yyyy");
    private final WaterInTakeRepository waterInTakeRepository;

    public MealsInTakeService(MealsInTakeRepository mealsIntakeRepository,
                              WaterInTakeRepository waterInTakeRepository) {
        this.mealsIntakeRepository = mealsIntakeRepository;
        this.waterInTakeRepository = waterInTakeRepository;
    }

    public MessageOutputData saveMealIntake(MealsInTake mealInTake, long chatId, MealType mealType) {
        LocalDate currentDate = LocalDate.now();
        String date = currentDate.format(formatter);

        mealsIntakeRepository.save(mealInTake, chatId, date, mealType);

        return new MessageOutputData("Отлично! Запись в дневник осуществлена."
                + " Введи /help для справки или /menu для выбора команд", chatId);
    }

    public MessageOutputData saveWaterInTake(long chatId) {
        LocalDate currentDate = LocalDate.now();
        String date = currentDate.format(formatter);
        waterInTakeRepository.save(chatId, date);
        return new MessageOutputData("Прием воды добавлен", chatId);
    }

    public int getWaterInTake(long chatId, String date) {
        date = date.formatted(formatter);
        return waterInTakeRepository.findWaterInTakeByDate(chatId, date);

    }

    public MealsInTake getMealsInTake(String date, MealType mealType, long chatId) {
        date = date.formatted(formatter);
        return mealsIntakeRepository.findByMealsInTakeType(mealType, date, chatId);
    }

    public void deleteMealInTake(long chatId, String date, MealType mealType) {
        mealsIntakeRepository.deleteMealType(mealType, date, chatId);
    }


}
