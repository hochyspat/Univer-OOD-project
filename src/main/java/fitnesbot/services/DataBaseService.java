package fitnesbot.services;

import fitnesbot.config.DataBaseConfig;
import fitnesbot.exeptions.databaseerrors.DriverNotFoundException;
import fitnesbot.models.sql.MealSQL;
import fitnesbot.models.sql.SleepSQL;
import fitnesbot.models.sql.TrainingSQL;
import fitnesbot.models.sql.UserSQL;
import fitnesbot.models.sql.WaterInTakeSql;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;


public class DataBaseService {
    private static String URL = "";
    private static String USER = "";
    private static String PASSWORD = "";

    private Connection connection;

    public DataBaseService() {
        DataBaseConfig dataBaseConfig = new DataBaseConfig();
        URL = dataBaseConfig.getDataBaseUrl();
        USER = dataBaseConfig.getBotUsername();
        PASSWORD = dataBaseConfig.getDataBasePassword();
    }

    {
        try {
            checkDriver();
        } catch (DriverNotFoundException e) {
            throw new RuntimeException(e);
        }
    }

    private void checkDriver() throws DriverNotFoundException {
        try {
            Class.forName("org.postgresql.Driver");
        } catch (ClassNotFoundException e) {
            throw new DriverNotFoundException();
        }
    }

    public Connection getConnection() {
        try {
            if (connection == null || connection.isClosed()) {
                connection = DriverManager.getConnection(URL, USER, PASSWORD);
            }
        } catch (SQLException e) {
            System.err.println("Ошибка получения соединения: " + e.getMessage());
        }
        return connection;
    }


    public void createAllTables() {
        try (Connection conn = getConnection();
             Statement statement = conn != null ? conn.createStatement() : null) {
            if (statement != null) {
                statement.executeUpdate(UserSQL.CREATE_TABLE);
                statement.executeUpdate(MealSQL.CREATE_MEALINTAKES_TABLE);
                statement.executeUpdate(TrainingSQL.CREATE_TRAININGS_TABLE);
                statement.executeUpdate(WaterInTakeSql.CREATE_WATER_INTAKES_TABLE);
                statement.executeUpdate(SleepSQL.CREATE_SLEEP_RECORDS);
                System.out.println("Таблицы успешно созданы.");
            } else {
                System.err.println("Не удалось создать таблицы: соединение с базой данных не установлено.");
            }
        } catch (SQLException e) {
            System.err.println("Ошибка создания таблицы: " + e.getMessage());
        }
    }
}
