package fitnesbot.out;

import fitnesbot.bot.MessageData;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;


public class ConsoleOutputService implements OutputService {

    @Override
    public void output(MessageData messageData) {
        System.out.println(messageData.getTextData());
    }
}
