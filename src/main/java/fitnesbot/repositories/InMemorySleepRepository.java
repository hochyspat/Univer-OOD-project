package fitnesbot.repositories;

import fitnesbot.services.SleepInTakeRepository;

import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.text.ParseException;
import java.text.SimpleDateFormat;

public class InMemorySleepRepository implements SleepInTakeRepository {
    private final Map<Long, Map<String, Double>> sleepInTakes = new HashMap<>();
    private static final SimpleDateFormat DATE_FORMAT = new SimpleDateFormat("dd.MM.yyyy");

    @Override
    public void save(long chatId, String datetime, double sleepQuantity) {
        sleepInTakes.computeIfAbsent(chatId, k -> new HashMap<>());
        sleepInTakes.get(chatId).put(datetime, sleepQuantity);
    }

    @Override
    public double getWeekStat(long chatId) {
        Map<String, Double> userSleepMap = sleepInTakes.get(chatId);
        Date today = new Date();
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(today);
        calendar.add(Calendar.DAY_OF_YEAR, -7);
        Date weekAgo = calendar.getTime();
        System.out.println(weekAgo);
        double totalSleep = 0;
        int count = 0;
        for (Map.Entry<String, Double> entry : userSleepMap.entrySet()) {
            try {
                Date date = DATE_FORMAT.parse(entry.getKey());
                if (!date.before(weekAgo) && !date.after(today)) {
                    totalSleep += entry.getValue();
                    count++;
                }
            } catch (ParseException e) {
                System.out.println("Invalid date format: " + entry.getKey());
                return -1;
            }
        }

        return count > 0 ? totalSleep / count : 0;
    }

    @Override
    public double getDayStat(long chatId, String datetime) {
        Map<String, Double> userSleepMap = sleepInTakes.get(chatId);
        return userSleepMap.getOrDefault(datetime, 0.0);
    }
}
