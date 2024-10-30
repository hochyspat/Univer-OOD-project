package fitnesbot.bot;


import fitnesbot.config.BotConfig;

import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.objects.Update;

public class TelegramBot extends TelegramLongPollingBot {
    private BotConfig botConfig;
    private CommandHandler commandHandler;

    public TelegramBot() {
        this.botConfig = new BotConfig();
    }

    public void setCommandHandler(CommandHandler commandHandler) {
        this.commandHandler = commandHandler;
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
            commandHandler.handleMessage(new MessageCommandData(command, chatId));
        }
    }
}
