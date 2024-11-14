package fitnesbot.bot;


import fitnesbot.config.BotConfig;

import fitnesbot.out.OutputService;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

public class TelegramBot extends TelegramLongPollingBot implements OutputService {
    private BotConfig botConfig;
    private CommandHandler commandHandler;

    public TelegramBot(CommandHandler commandHandler) {
        this.commandHandler = commandHandler;
        this.botConfig = new BotConfig();
    }

    @Override
    public String getBotUsername() {
        return botConfig.getBotUsername();
    }

    @Override
    public String getBotToken() {
        return botConfig.getBotToken();
    }

    @Override
    public void onUpdateReceived(Update update) {
        if (update.hasMessage() && update.getMessage().hasText()) {
            String messageText = update.getMessage().getText();
            long chatId = update.getMessage().getChatId();
            Command command = new Command(messageText);
            MessageOutputData messageOutputData = commandHandler.handleMessage(new MessageCommandData(command, chatId));
            if (messageOutputData != null && messageOutputData.getMessageData() != null) {
                sendMessage(messageOutputData);
            }
        }


    }

    @Override
    public void sendMessage(MessageOutputData messageData) {
        SendMessage sendMessage = new SendMessage(String.valueOf(messageData.getChatId()), messageData.getMessageData());
        try {
            execute(sendMessage);
        } catch (TelegramApiException e) {
            e.printStackTrace();
        }
    }
}
