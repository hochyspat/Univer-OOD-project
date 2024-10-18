package fitnesbot.out;

import com.pengrad.telegrambot.request.SendMessage;
import fitnesbot.bot.MessageData;
import fitnesbot.bot.TelegramBot;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

public class TelegramOutputService implements OutputService  {
    private TelegramBot telegramBot;

    public void setTelegramBot(TelegramBot telegramBot) {
        this.telegramBot = telegramBot;
    }
    @Override
    public void output(MessageData messageData) {
        SendMessage sendMessage = new SendMessage(messageData.getChatId(), messageData.getText());
        try {
            // Отправляем обработанное сообщение
            telegramBot.execute(sendMessage);
        } catch (TelegramApiException e) {
            e.printStackTrace();
        }
    }
}
