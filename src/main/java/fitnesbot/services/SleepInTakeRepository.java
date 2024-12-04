package fitnesbot.services;

public interface SleepInTakeRepository {
    void save(long chatId, String datetime, double sleepQuantity);

    double getWeekStat(long chatId);

    double getDayStat(long chatId, String datetime);
}
