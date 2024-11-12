package fitnesbot.bot;

public class MessageCommandData {
    private Command command;
    private final long chatId;

    public MessageCommandData(Command command, long chatId) {
        this.command = command;
        this.chatId = chatId;
    }


    public Command getCommand() {return command;}
    public long getChatId() {return chatId;}
}

