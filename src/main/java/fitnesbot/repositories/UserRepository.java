package fitnesbot.repositories;

import fitnesbot.models.User;
import fitnesbot.services.IUserRepository;
import fitnesbot.services.UserService;

import java.util.HashMap;
import java.util.Map;

public class UserRepository implements IUserRepository {
    private Map<Long, User> users = new HashMap<>();


    @Override
    public void save(User user) {
        users.put(user.getChatId(),user);
    }

    @Override
    public User findById(long chatId) {
        return users.get(chatId);
    }

    @Override
    public void delete(long chatId){
        users.remove(chatId);
    }

    @Override
    public boolean existsById(long chatId) {
        return users.containsKey(chatId);
    }



}
