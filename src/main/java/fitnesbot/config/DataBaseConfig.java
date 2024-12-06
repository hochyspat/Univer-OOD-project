package fitnesbot.config;

import fitnesbot.models.UserSQL;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;


public class DataBaseConfig {
    private static final String URL = "jdbc:postgresql://localhost:5432/fitnessExpert-bot";
    private static final String USER = "postgres";
    private static final String PASSWORD = "Z8z!GhQ";

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
