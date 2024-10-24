package fitnesbot.out;

import fitnesbot.bot.MessageCommandData;
import fitnesbot.bot.MessageOutputData;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;


public class ConsoleOutputService implements OutputService {

    @Override
    public void output(MessageOutputData messageData) {
        System.out.println(messageData.getMessageData());
    }
}
