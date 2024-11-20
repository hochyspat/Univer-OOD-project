package fitnesbot.repositories;

import fitnesbot.models.User;
import fitnesbot.services.UserRepository;
import java.util.HashMap;
import java.util.Map;

public class InMemoryUserRepository implements UserRepository {
    private Map<Long, User> users = new HashMap<>();


    @Override
    public void save(User user) {
        users.put(user.getChatId(),user);
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
    public void delete(long chatId){
        User user = users.get(chatId);
        if(user != null){
            user.setDeleted(true);
        }
    }

    @Override
    public boolean existsById(long chatId) {
        User user = users.get(chatId);
        return (user != null && !user.isDeleted());
    }



}
