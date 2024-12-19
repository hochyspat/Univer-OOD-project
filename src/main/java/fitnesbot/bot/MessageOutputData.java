package fitnesbot.bot;

public record MessageOutputData(String messageData, long chatId, String image) {
    public MessageOutputData(String messageData, long chatId) {
        this(messageData, chatId, null);
    }

    public boolean hasImage() {
        return image != null;
    }

}
