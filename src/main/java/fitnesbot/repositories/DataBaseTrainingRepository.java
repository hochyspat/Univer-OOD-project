package fitnesbot.repositories;

import fitnesbot.models.TrainingSQL;
import fitnesbot.models.TrainingSession;
import fitnesbot.services.TrainingRepository;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

public class DataBaseTrainingRepository implements TrainingRepository {
    private final Connection connection;

    public DataBaseTrainingRepository(Connection connection) {
        this.connection = connection;
    }

    @Override
    public void save(long chatId, String date, TrainingSession trainingSession) {
        try (PreparedStatement statement = connection.prepareStatement(TrainingSQL.INSERT_TRAINING)) {
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
        try (PreparedStatement statement = connection.prepareStatement(TrainingSQL.SELECT_TRAININGS_BY_CHAT_ID)) {
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
        try (PreparedStatement statement = connection.prepareStatement(TrainingSQL.SELECT_TRAININGS_BY_DATE)) {
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
        try (PreparedStatement statement = connection.prepareStatement(TrainingSQL.DELETE_TRAINING)) {
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
}