package fitnesbot.services;

import fitnesbot.bot.MessageOutputData;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

public class SleepInTakeService {
    private final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd.MM.yyyy");
    private final SleepInTakeRepository sleepInTakeRepository;

    public SleepInTakeService(SleepInTakeRepository sleepInTakeRepository) {
        this.sleepInTakeRepository = sleepInTakeRepository;
    }

    public MessageOutputData saveSleepInTake(long chatId, int quantity){
        LocalDate currentDate = LocalDate.now();
        String date = currentDate.format(formatter);
        sleepInTakeRepository.save(chatId, date, quantity);
        return new MessageOutputData("Количество сна добавлено", chatId);
    }

    public MessageOutputData getWeekSleepStat(long chatId) {
        double result = sleepInTakeRepository.getWeekStat(chatId);
        return (new MessageOutputData(new StringBuilder("Среднее количество сна за неделю: ").append(result).toString(), chatId));
    }
}
