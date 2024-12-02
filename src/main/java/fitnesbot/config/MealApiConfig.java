package fitnesbot.config;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class MealApiConfig {
    private String apiId;
    private String apiKey;

    public MealApiConfig() {
        Properties properties = new Properties();
        try (InputStream input = getClass().getClassLoader().getResourceAsStream(
                "application.properties")) {
            if (input == null) {
                System.out.println("файл конфигурации не найден.");
                return;
            }
            properties.load(input);
            this.apiId = properties.getProperty("edamam.api.appId");
            this.apiKey = properties.getProperty("edamam.api.appKey");
        } catch (IOException e) {
            System.err.println("Error with MealApiConfig: " + e.getMessage());
        }
    }

    public String getMealApiId() {
        return apiId;
    }

    public String getMealApikey() {
        return apiKey;
    }


}
