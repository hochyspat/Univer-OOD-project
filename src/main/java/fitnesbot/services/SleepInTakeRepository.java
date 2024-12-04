package fitnesbot.services;

public interface SleepInTakeRepository {
    void save(long chatId, String datetime, int sleepQuantity);

    double getWeekStat(long chatId);
}
