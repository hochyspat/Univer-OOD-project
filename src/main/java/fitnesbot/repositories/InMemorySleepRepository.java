package fitnesbot.repositories;

import fitnesbot.services.SleepInTakeRepository;

import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.text.ParseException;
import java.text.SimpleDateFormat;

public class InMemorySleepRepository implements SleepInTakeRepository {
    private final Map<Long, Map<String, Integer>> sleepInTakes = new HashMap<>();

    @Override
    public void save(long chatId, String datetime, int sleepQuantity) {
        Map<String, Integer> userSleepMap = sleepInTakes.get(chatId);
        if (userSleepMap == null) {
            userSleepMap = new HashMap<>();
            sleepInTakes.put(chatId, userSleepMap);
        }
        userSleepMap.put(datetime, sleepQuantity);
    }

    @Override
    public double getWeekStat(long chatId) {
        Map<String, Integer> userSleepMap = sleepInTakes.get(chatId);
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd.MM.yyyy");
        Date today = new Date();

        Calendar calendar = Calendar.getInstance();
        calendar.setTime(today);
        calendar.add(Calendar.DAY_OF_YEAR, -7);
        Date weekAgo = calendar.getTime();

        double totalSleep = 0;
        int count = 0;

        for (Map.Entry<String, Integer> entry : userSleepMap.entrySet()) {
            try {
                Date date = dateFormat.parse(entry.getKey());
                if (!date.before(weekAgo) && !date.after(today)) {
                    totalSleep += entry.getValue();
                    count++;
                }
            } catch (ParseException e) {
                System.out.println("Invalid date format: " + entry.getKey());
                double parseExceptionCode = -1;
                return parseExceptionCode;
            }
        }

        return count > 0 ? totalSleep / count : 0;
        }
    }
