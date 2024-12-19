package fitnesbot.repositories.database;

import fitnesbot.models.sql.TrainingSQL;
import fitnesbot.models.TrainingSession;
import fitnesbot.services.DataBaseService;
import fitnesbot.services.TrainingRepository;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

public class DataBaseTrainingRepository implements TrainingRepository {

    private final DataBaseService dataBaseService;

    public DataBaseTrainingRepository(DataBaseService dataBaseService) {
        this.dataBaseService = dataBaseService;
    }

    @Override
    public void save(long chatId, String date, TrainingSession trainingSession) {
        try (Connection connection = dataBaseService.getConnection();
             PreparedStatement statement = Objects.requireNonNull(connection).prepareStatement(
                     TrainingSQL.INSERT_TRAINING)) {
            statement.setLong(1, chatId);
            statement.setString(2, date);
            statement.setString(3, trainingSession.getName());
            statement.setDouble(4, trainingSession.getTrainingTime());
            statement.setDouble(5, trainingSession.getCaloriesBurned());
            statement.executeUpdate();
        } catch (Exception e) {
            System.err.println("Не удалось сохранить тренировку " + e.getMessage());
        }
    }

    @Override
    public List<TrainingSession> findByChatId(long chatId) {
        List<TrainingSession> sessions = new ArrayList<>();
        try (Connection connection = dataBaseService.getConnection();
             PreparedStatement statement = Objects.requireNonNull(connection).prepareStatement(
                     TrainingSQL.SELECT_TRAININGS_BY_CHAT_ID)) {
            statement.setLong(1, chatId);
            ResultSet rs = statement.executeQuery();
            while (rs.next()) {
                sessions.add(new TrainingSession(
                        rs.getString("name"),
                        rs.getDouble("training_time"),
                        rs.getDouble("calories_burned")
                ));
            }
        } catch (Exception e) {
            System.err.println("Error finding trainings by chatId: " + e.getMessage());
        }
        return sessions;
    }

    @Override
    public List<TrainingSession> findByDate(long chatId, String date) {
        List<TrainingSession> sessions = new ArrayList<>();
        try (Connection connection = dataBaseService.getConnection();
             PreparedStatement statement = Objects.requireNonNull(connection).prepareStatement(
                     TrainingSQL.SELECT_TRAININGS_BY_DATE)) {
            statement.setLong(1, chatId);
            statement.setString(2, date);
            ResultSet rs = statement.executeQuery();
            while (rs.next()) {
                sessions.add(new TrainingSession(
                        rs.getString("name"),
                        rs.getDouble("training_time"),
                        rs.getDouble("calories_burned")
                ));
            }
        } catch (Exception e) {
            System.err.println("Не удалось найти тренировки для даты: " + e.getMessage());
        }
        return sessions;
    }

    @Override
    public void deleteSession(long chatId, String date, String sessionName) {
        try (Connection connection = dataBaseService.getConnection();
             PreparedStatement statement = Objects.requireNonNull(connection).prepareStatement(
                     TrainingSQL.DELETE_TRAINING)) {
            statement.setLong(1, chatId);
            statement.setString(2, date);
            statement.setString(3, sessionName);
            int isUpdated = statement.executeUpdate();
            if (isUpdated == 0) {
                System.out.println("Тренировка не найдена или уже удалена.");
            } else {
                System.out.println("Тренировка успешно удалена.");
            }

        } catch (Exception e) {
            System.err.println("Ошибка при удалении тренировки: " + e.getMessage());
        }
    }

    public Map<String, List<TrainingSession>> getTrainingByChatId(long chatId) {
        Map<String, List<TrainingSession>> trainingData = new HashMap<>();
        List<TrainingSession> sessions = new ArrayList<>();
        try (Connection connection = dataBaseService.getConnection();
             PreparedStatement statement = Objects.requireNonNull(connection).prepareStatement(
                     TrainingSQL.SELECT_TRAININGS_BY_CHAT_ID)) {
            statement.setLong(1, chatId);
            ResultSet rs = statement.executeQuery();
            while (rs.next()) {
                String date = rs.getString("training_date");
                sessions = trainingData.getOrDefault(date, new ArrayList<>());
                sessions.add(new TrainingSession(
                        rs.getString("name"),
                        rs.getDouble("training_time"),
                        rs.getDouble("calories_burned")
                ));
                trainingData.put(date, sessions);
            }
        } catch (Exception e) {
            System.err.println("Error finding trainings by chatId: " + e.getMessage());
        }
        return trainingData;
    }
}
