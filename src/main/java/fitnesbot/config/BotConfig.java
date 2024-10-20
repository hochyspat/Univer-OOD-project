package fitnesbot.config;

import java.io.IOException;
import java.util.Properties;
import java.io.InputStream;

public class BotConfig {
    private String botName;
    private String botToken;

    public BotConfig() {
        Properties properties = new Properties();
        try (InputStream input = getClass().getClassLoader().getResourceAsStream("application.properties")) {
            if (input == null) {
                System.out.println("файл конфигурации не найден.");
                return;
            }
            properties.load(input);


            this.botName = properties.getProperty("telegram.bot.username");
            this.botToken = properties.getProperty("telegram.bot.token");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public String getBotUsername() {
        return botName;
    }

    public String getBotToken() {
        return botToken;
    }


    }


