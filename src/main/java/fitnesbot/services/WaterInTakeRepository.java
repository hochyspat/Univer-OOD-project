package fitnesbot.services;

import java.util.Map;

public interface WaterInTakeRepository {

    void save(long chatId, String datetime);

    int findWaterInTakeByDate(long chatId, String datetime);

    Map<String, Integer> getWaterByChatId(long chatId);
}
