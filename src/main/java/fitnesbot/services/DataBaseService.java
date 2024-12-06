package fitnesbot.services;

import fitnesbot.config.DataBaseConfig;
import fitnesbot.models.UserSQL;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;


public class DataBaseService {
    private static String URL = "";
    private static String USER = "";
    private static String PASSWORD = "" ;

    public DataBaseService() {
        DataBaseConfig dataBaseConfig = new DataBaseConfig();
        URL= dataBaseConfig.getDataBaseUrl();
        USER = dataBaseConfig.getBotUsername();
        PASSWORD = dataBaseConfig.getDataBasePassword();
    }
    public static Connection connect() {

        try {
            Class.forName("org.postgresql.Driver");
            System.out.println("Подключение к базе данных выполнено успешно!");
            return DriverManager.getConnection(URL, USER, PASSWORD);
        } catch (ClassNotFoundException e) {
            System.out.println("Драйвер PostgreSQL не найден: " + e.getMessage());
        } catch (SQLException e) {
            System.out.println("Ошибка подключения к базе данных: " + e.getMessage());
        }
        return null;
    }

    public void createAllTable() {
        try (Connection conn = connect();
             Statement stmt = conn != null ? conn.createStatement() : null) {
            if (stmt != null) {
                stmt.executeUpdate(UserSQL.CREATE_TABLE);
                System.out.println("Таблица Users успешно создана.");
            } else {
                System.err.println("Не удалось создать таблицу: соединение с базой данных не установлено.");
            }
        } catch (SQLException e) {
            System.err.println("Ошибка создания таблицы: " + e.getMessage());
        }
    }
}
