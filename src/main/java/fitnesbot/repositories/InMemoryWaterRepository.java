package fitnesbot.repositories;

import fitnesbot.services.WaterInTakeRepository;

import java.util.HashMap;
import java.util.Map;

public class InMemoryWaterRepository implements WaterInTakeRepository {
    private final Map<Long, Map<String, Integer>> waterInTakes = new HashMap<>();

    @Override
    public void save(long chatId, String datetime) {
        Map<String, Integer> userWaterMap = waterInTakes.get(chatId);
        if (userWaterMap == null) {
            userWaterMap = new HashMap<>();
            waterInTakes.put(chatId, userWaterMap);
        }
        userWaterMap.put(datetime, userWaterMap.getOrDefault(datetime, 0) + 1);
    }

    @Override
    public int findWaterInTakeByDate(long chatId, String date) {
        Map<String, Integer> userWaterMap = waterInTakes.get(chatId);

        if (userWaterMap != null && userWaterMap.containsKey(date)) {
            return userWaterMap.get(date);
        }

        System.out.println("Not found waterInTake by date: " + date);
        return -1;
    }
}
