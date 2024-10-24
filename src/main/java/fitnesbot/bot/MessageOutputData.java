package fitnesbot.bot;

public class MessageOutputData {
    private String messageData;
    private final long chatId;

    public MessageOutputData(String messageData, long chatId) {
        this.messageData = messageData;
        this.chatId = chatId;
    }

    public String getMessageData() {return messageData;}

    public long getChatId() {
        return chatId;
    }
}
