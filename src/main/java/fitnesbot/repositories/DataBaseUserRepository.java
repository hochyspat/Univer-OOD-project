package fitnesbot.repositories;

import fitnesbot.config.DataBaseConfig;
import fitnesbot.models.User;
import fitnesbot.models.UserSQL;
import fitnesbot.services.UserRepository;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Objects;

public class DataBaseUserRepository implements UserRepository {

    @Override
    public void save(User user) {
        try (Connection connection = DataBaseConfig.connect();
             PreparedStatement statement = Objects.requireNonNull(connection).prepareStatement(UserSQL.INSERT_USER)) {
            statement.setLong(1, user.getChatId());
            statement.setString(2, user.getName());
            statement.setInt(3, user.getHeight());
            statement.setInt(4, user.getWeight());
            statement.setInt(5, user.getAge());
            statement.setDouble(6, user.getCalories());
            statement.setObject(7, user.getWaterGoal() != null ? user.getWaterGoal().quantity() : null);
            statement.setString(8, user.getWaterGoal() != null ? user.getWaterGoal().units().name() : null);
            statement.setObject(9, user.getSleepGoal() != null ? user.getSleepGoal().quantity() : null);
            statement.setBoolean(10, user.isDeleted());
            statement.executeUpdate();
            System.out.println("Пользователь успешно сохранен: " + user.getName());
        } catch (SQLException e) {
            System.err.println("Ошибка сохранения пользователя: " + e.getMessage());
        }
    }

    @Override
    public User findById(long chatId) {
        try (Connection connection = DataBaseConfig.connect();
             PreparedStatement statement = Objects.requireNonNull(connection).prepareStatement(UserSQL.SELECT_USER_BY_ID)) {
            statement.setLong(1, chatId);
            ResultSet resultSet = statement.executeQuery();
            if (resultSet.next()) {
                return new User(
                        resultSet.getString("name"),
                        resultSet.getInt("height"),
                        resultSet.getInt("weight"),
                        resultSet.getInt("age"),
                        resultSet.getLong("chat_id")
                );
            }
        } catch (SQLException e) {
            System.err.println("Ошибка поиска пользователя: " + e.getMessage());
        }
        return null;
    }

    @Override
    public void delete(long chatId) {
        try (Connection connection = DataBaseConfig.connect();
             PreparedStatement preparedStatement = Objects.requireNonNull(connection).prepareStatement(UserSQL.DELETE_USER)) {
            preparedStatement.setLong(1, chatId);
            preparedStatement.executeUpdate();
            System.out.println("Пользователь с ID " + chatId + " помечен как удаленный.");
        } catch (SQLException e) {
            System.err.println("Ошибка удаления пользователя: " + e.getMessage());
        }
    }
    public boolean existsById(long chatId) {
        try (Connection connection = DataBaseConfig.connect();
             PreparedStatement preparedStatement = Objects.requireNonNull(connection).prepareStatement(UserSQL.EXISTS_BY_ID)) {
            preparedStatement.setLong(1, chatId);
            ResultSet rs = preparedStatement.executeQuery();
            if (rs.next()) {
                return rs.getInt(1) > 0;
            }
        } catch (SQLException e) {
            System.err.println("Ошибка проверки существования пользователя: " + e.getMessage());
        }
        return false;
    }


}
