package fitnesbot.services;

public interface WaterInTakeRepository {

    void save(long chatId, String datetime);

    int findWaterInTakeByDate(long chatId, String datetime);
}
