package fitnesbot.bot;

public class MessageData {
    private String textData;
    private long chatId;

    public MessageData(String textData, long chatId) {
        this.textData = textData;
        this.chatId = chatId;
    }

    public String getTextData() {
        return textData;
    }
    public long getChatId() {
        return chatId;
    }



}
