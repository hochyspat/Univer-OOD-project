package fitnesbot.services;

import fitnesbot.models.User;

public interface MemoryUserReposytory {
    void save(User user);
    User findById(long chatId);
    void delete(long chatId);
    boolean existsById(long chatId);

}
