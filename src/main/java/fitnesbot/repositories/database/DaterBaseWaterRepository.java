package fitnesbot.repositories.database;

import fitnesbot.models.WaterInTakeSql;
import fitnesbot.services.DataBaseService;
import fitnesbot.services.WaterInTakeRepository;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.Objects;

public class DaterBaseWaterRepository implements WaterInTakeRepository {

    @Override
    public void save(long chatId, String datetime) {
        try (Connection connection = DataBaseService.connect();
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
        try (Connection connection = DataBaseService.connect();
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


}



