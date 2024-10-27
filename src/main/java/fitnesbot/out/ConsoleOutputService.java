package fitnesbot.out;
import fitnesbot.bot.MessageOutputData;
public class ConsoleOutputService implements OutputService {

    @Override
    public void output(MessageOutputData messageData) {
        System.out.println(messageData.getMessageData());
    }
}
