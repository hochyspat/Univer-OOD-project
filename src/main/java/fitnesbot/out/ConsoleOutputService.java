package fitnesbot.out;

import fitnesbot.bot.MessageOutputData;

public class ConsoleOutputService implements OutputService {

    @Override
    public void sendMessage(MessageOutputData messageData) {
        System.out.println(messageData.messageData());
        if (messageData.image() != null) {
            System.out.println("Изображение сохранено по следующему пути: " + messageData.image());
        }
    }
}
