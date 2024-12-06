package fitnesbot.repositories;

import fitnesbot.models.SleepGoal;
import fitnesbot.models.User;
import fitnesbot.models.WaterGoal;
import fitnesbot.services.NutrientUnits;
import fitnesbot.services.UserRepository;

import java.util.HashMap;
import java.util.Map;

public class InMemoryUserRepository implements UserRepository {
    private final Map<Long, User> users = new HashMap<>();


    @Override
    public void save(User user) {
        users.put(user.getChatId(), user);
    }

    @Override
    public User findById(long chatId) {
        User user = users.get(chatId);
        if (user != null && !user.isDeleted()) {
            return user;
        }
        return null;
    }

    @Override
    public void delete(long chatId) {
        User user = users.get(chatId);
        if (user != null) {
            user.setDeleted(true);
        }
    }

    @Override
    public boolean existsById(long chatId) {
        User user = users.get(chatId);
        return (user != null && user.isDeleted());
    }

    @Override
    public void updateWaterGoal(long chatId, double quantity) {
        User user = users.get(chatId);
        if (user != null) {
            user.setWaterGoal(new WaterGoal(quantity, NutrientUnits.ML));
        }
    }
    @Override
    public void updateSleepGoal(long chatId, double quantity) {
        User user = users.get(chatId);
        if (user != null) {
            user.setSleepGoal(new SleepGoal(quantity));
        }
    }

}
