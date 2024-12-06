package fitnesbot.services;

import fitnesbot.models.User;

public interface UserRepository {
    void save(User user);

    User findById(long chatId);

    void delete(long chatId);

    boolean existsById(long chatId);

    public void updateWaterGoal(long chatId, double quantity);

    public void updateSleepGoal(long chatId, double quantity);
}
