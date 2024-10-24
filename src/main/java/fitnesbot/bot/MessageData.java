package fitnesbot.bot;

public class MessageData {
    private Command command;
    private final long chatId;

    public MessageData(Command command, long chatId) {
        this.command = command;
        this.chatId = chatId;
    }

    public String getText() {
        return command.getText();
    }

    public long getChatId() {
        return chatId;
    }
}

