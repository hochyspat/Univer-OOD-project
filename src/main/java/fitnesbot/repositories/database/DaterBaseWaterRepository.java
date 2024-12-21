package fitnesbot.repositories.database;

import fitnesbot.models.sql.WaterInTakeSql;
import fitnesbot.services.DataBaseService;
import fitnesbot.services.WaterInTakeRepository;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class DaterBaseWaterRepository implements WaterInTakeRepository {

    private final DataBaseService dataBaseService;

    public DaterBaseWaterRepository(DataBaseService dataBaseService) {
        this.dataBaseService = dataBaseService;
    }

    @Override
    public void save(long chatId, String datetime) {
        try (Connection connection = dataBaseService.getConnection();
             PreparedStatement statement = Objects.requireNonNull(connection).prepareStatement(
                     WaterInTakeSql.INSERT_WATER_INTAKE)) {
            statement.setLong(1, chatId);
            statement.setString(2, datetime);
            statement.executeUpdate();
            System.out.println("Данные о воде успешно сохранены.");
        } catch (Exception e) {
            System.err.println("Ошибка сохранения данных о воде: " + e.getMessage());
        }
    }

    @Override
    public int findWaterInTakeByDate(long chatId, String date) {
        try (Connection connection = dataBaseService.getConnection();
             PreparedStatement statement = Objects.requireNonNull(connection).prepareStatement(
                     WaterInTakeSql.SELECT_WATER_INTAKE)) {
            statement.setLong(1, chatId);
            statement.setString(2, date);
            ResultSet resultSet = statement.executeQuery();

            if (resultSet.next()) {
                return resultSet.getInt("intake_count");
            } else {
                System.out.println("Данные о приеме воды не найдены: " + date);
                return -1;
            }
        } catch (Exception e) {
            System.err.println("Ошибка получения данных о приеме воды: " + e.getMessage());
            return -1;
        }
    }

    @Override
    public Map<String, Integer> getWaterByChatId(long chatId) {
        Map<String, Integer> waterData = new HashMap<>();
        try (Connection connection = dataBaseService.getConnection();
             PreparedStatement statement = Objects.requireNonNull(connection).prepareStatement(
                     WaterInTakeSql.SELECT_ALL_WATER)) {
            statement.setLong(1, chatId);
            ResultSet resultSet = statement.executeQuery();
            while (resultSet.next()) {
                String date = resultSet.getString("water_date");
                int waterQuantity = resultSet.getInt("intake_count");
                waterData.put(date, waterQuantity);
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return waterData;
    }


}



