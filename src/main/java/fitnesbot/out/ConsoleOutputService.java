package fitnesbot.out;

import fitnesbot.bot.MessageOutputData;

public class ConsoleOutputService implements OutputService {

    @Override
    public void sendMessage(MessageOutputData messageData) {
        System.out.println(messageData.messageData());
    }
}
