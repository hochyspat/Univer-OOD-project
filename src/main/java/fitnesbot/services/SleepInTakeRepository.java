package fitnesbot.services;

import java.util.Map;

public interface SleepInTakeRepository {
    void save(long chatId, String datetime, double sleepQuantity);

    double getWeekStat(long chatId);

    double getDayStat(long chatId, String datetime);

    public Map<String, Double> getDataByChatId(long chatId);

}
