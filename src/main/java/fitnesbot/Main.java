package fitnesbot;


import fitnesbot.out.*;
import fitnesbot.bot.Bot;
import fitnesbot.in.ConsoleInputService;
import fitnesbot.in.InputService;
import fitnesbot.services.*;

public class Main {
    public static void main(String[] args) {
        InputService inputService = new ConsoleInputService();
        OutputService outputService = new ConsoleOutputService();
        CalorieCountingService calorieCountingService = new CalorieCountingService();
        Help help = new Help();
        Menu menu = new Menu();
        Bot bot = new Bot(inputService,outputService,help,menu,calorieCountingService);
        bot.start();
    }
}