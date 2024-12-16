package fitnesbot.config;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class DataBaseConfig {
    private String dataBaseName;
    private String dataBasePassword;
    private String dataBaseUrl;

    public DataBaseConfig() {
        Properties properties = new Properties();
        try (InputStream input = getClass().getClassLoader().getResourceAsStream(
                "application.properties")) {
            if (input == null) {
                System.out.println("файл конфигурации не найден.");
                return;
            }
            properties.load(input);
            this.dataBaseName = properties.getProperty("db.username");
            this.dataBasePassword = properties.getProperty("db.password");
            this.dataBaseUrl = properties.getProperty("db.url");
        } catch (IOException e) {
            System.err.println("Error with BotConfig properties file." + e.getMessage());
        }
    }

    public String getBotUsername() {
        return dataBaseName;
    }

    public String getDataBasePassword() {
        return dataBasePassword;
    }

    public String getDataBaseUrl() {
        return dataBaseUrl;
    }
}
