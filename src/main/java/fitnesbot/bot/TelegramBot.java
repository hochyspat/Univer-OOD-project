package fitnesbot.bot;

import com.pengrad.telegrambot.request.SendMessage;
import fitnesbot.in.ConsoleInputService;
import fitnesbot.in.InputService;
import fitnesbot.out.ConsoleOutputService;
import fitnesbot.out.OutputService;
import fitnesbot.out.TelegramOutputService;
import fitnesbot.services.CalorieCountingService;
import fitnesbot.services.Help;
import fitnesbot.services.Menu;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import org.telegram.telegrambots.meta.api.objects.Update;

public class TelegramBot extends TelegramLongPollingBot {
    final String BOT_USERNAME = "FitExpertt_bot";
    final String BOT_TOKEN = "7864436723:AAGf8MoqA7g_uTGadhwC9GzuzOtpzrQaiuM";
    private CommandHandler commandHandler;
    private OutputService outputService;

    public TelegramBot(TelegramOutputService telegramOutputService, CommandHandler commandHandler) {
        this.outputService = telegramOutputService;
        this.commandHandler = commandHandler;
    }

    @Override
    public String getBotUsername() {
        return BOT_USERNAME;
    }

    @Override
    public String getBotToken() {
        return BOT_TOKEN;
    }

    @Override
    public void onUpdateReceived(Update update) {
        if (update.hasMessage() && update.getMessage().hasText()) {
            String messageText = update.getMessage().getText();
            long chatId = update.getMessage().getChatId();
            Command command = new Command(messageText);
            commandHandler.handleMessage(command,chatId);
            execute(sendMessage);
        }
    }
}
