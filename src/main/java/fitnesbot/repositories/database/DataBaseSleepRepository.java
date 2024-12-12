package fitnesbot.repositories.database;

import fitnesbot.models.SleepSQL;
import fitnesbot.services.DataBaseService;
import fitnesbot.services.SleepInTakeRepository;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Objects;

public class DataBaseSleepRepository implements SleepInTakeRepository {
    private static final SimpleDateFormat DATE_FORMAT = new SimpleDateFormat("dd.MM.yyyy");

    @Override
    public void save(long chatId, String datetime, double sleepQuantity) {
        try (Connection connection = DataBaseService.connect();
             PreparedStatement statement = Objects.requireNonNull(connection).prepareStatement(
                     SleepSQL.INSERT_SLEEP)) {
            statement.setLong(1, chatId);
            statement.setString(2, datetime);
            statement.setDouble(3, sleepQuantity);
            statement.executeUpdate();
        } catch (SQLException e) {
            System.err.println("Ошибка сохранения данных о сне: " + e.getMessage());
        }
    }

    @Override
    public double getWeekStat(long chatId) {
        try (Connection connection = DataBaseService.connect();
             PreparedStatement statement = Objects.requireNonNull(connection).prepareStatement(
                     SleepSQL.SELECT_WEEK_SLEEP)) {
            Date today = new Date();
            Calendar calendar = Calendar.getInstance();
            calendar.setTime(today);
            calendar.add(Calendar.DAY_OF_YEAR, -7);
            Date weekAgo = calendar.getTime();

            statement.setLong(1, chatId);
            statement.setString(2, DATE_FORMAT.format(weekAgo));
            statement.setString(3, DATE_FORMAT.format(today));

            ResultSet resultSet = statement.executeQuery();
            double totalSleep = 0;
            int count = 0;
            while (resultSet.next()) {
                totalSleep += resultSet.getDouble("sleep_quantity");
                count++;
            }
            return count > 0 ? totalSleep / count : 0;
        } catch (SQLException e) {
            System.err.println("Ошибка получения недельной статистики сна: " + e.getMessage());
            return -1;
        }
    }

    @Override
    public double getDayStat(long chatId, String datetime) {
        try (Connection connection = DataBaseService.connect();
             PreparedStatement statement = Objects.requireNonNull(connection).prepareStatement(
                     SleepSQL.SELECT_SLEEP_BY_DATE)) {
            statement.setLong(1, chatId);
            statement.setString(2, datetime);

            ResultSet resultSet = statement.executeQuery();
            if (resultSet.next()) {
                return resultSet.getDouble("sleep_quantity");
            }
        } catch (SQLException e) {
            System.err.println("Ошибка получения данных о сне за день: " + e.getMessage());
        }
        return 0;
    }
}
