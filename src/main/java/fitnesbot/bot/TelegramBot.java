package fitnesbot.bot;


import fitnesbot.config.BotConfig;
import fitnesbot.out.OutputService;
import fitnesbot.out.TelegramOutputService;

import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import org.telegram.telegrambots.meta.api.objects.Update;

public class TelegramBot extends TelegramLongPollingBot {
    private BotConfig botConfig;
    private CommandHandler commandHandler;
    private OutputService outputService;

    public TelegramBot(TelegramOutputService telegramOutputService, CommandHandler commandHandler) {
        this.outputService = telegramOutputService;
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
            commandHandler.handleMessage(command,chatId);
        }
    }
}
