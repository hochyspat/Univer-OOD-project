package fitnesbot.out;

import fitnesbot.bot.MessageOutputData;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import fitnesbot.bot.TelegramBot;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

public class TelegramOutputService implements OutputService  {
    private TelegramBot telegramBot;

    public TelegramOutputService(TelegramBot telegramBot) {
        this.telegramBot = telegramBot;
    }
    @Override
    public void sendMessage(MessageOutputData messageOutputData) {
        SendMessage sendMessage = new SendMessage(String.valueOf(messageOutputData.getChatId()), messageOutputData.getMessageData());
        try {
            telegramBot.execute(sendMessage);
        } catch (TelegramApiException e) {
            e.printStackTrace();
        }
    }
}
