package fitnesbot.bot;

import fitnesbot.config.BotConfig;
import fitnesbot.out.OutputService;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.send.SendPhoto;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.InputFile;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import java.io.File;


public class TelegramBot extends TelegramLongPollingBot implements OutputService {
    private final BotConfig botConfig;
    private final CommandHandler commandHandler;

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
            MessageOutputData messageOutputData = commandHandler.handleMessage(
                    new MessageCommandData(command, chatId));
            if (messageOutputData != null && messageOutputData.messageData() != null) {
                sendMessage(messageOutputData);
            }
        }


    }

    @Override
    public void sendMessage(MessageOutputData messageData) {
        if (messageData.hasImage()) {
            SendPhoto sendPhoto = new SendPhoto();
            sendPhoto.setChatId(String.valueOf(messageData.chatId()));
            sendPhoto.setPhoto(new InputFile(new File(messageData.image())));
            try {
                execute(sendPhoto);
            } catch (TelegramApiException e) {
                System.err.println("Ошибка при отправке изображения: " + e.getMessage());
            }
        } else {
            SendMessage sendMessage = new SendMessage(
                    String.valueOf(messageData.chatId()), messageData.messageData());
            try {
                execute(sendMessage);
            } catch (TelegramApiException e) {
                System.err.println("Ошибка при отправке текстового сообщения: " + e.getMessage());
            }
        }
    }
}
