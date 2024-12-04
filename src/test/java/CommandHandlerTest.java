import fitnesbot.bot.Command;
import fitnesbot.bot.CommandHandler;
import fitnesbot.bot.MessageCommandData;
import fitnesbot.bot.MessageOutputData;
import fitnesbot.models.User;
import fitnesbot.repositories.InMemoryUserRepository;

import fitnesbot.repositories.InMemoryWaterRepository;
import fitnesbot.services.CalorieCountingService;
import fitnesbot.services.Help;
import fitnesbot.services.MealsInTakeRepository;
import fitnesbot.services.MealsInTakeService;
import fitnesbot.services.Menu;
import fitnesbot.services.SleepInTakeRepository;
import fitnesbot.services.SleepInTakeService;
import fitnesbot.services.UserRepository;
import fitnesbot.services.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;

public class CommandHandlerTest {

    private CommandHandler commandHandler;
    private UserRepository userRepository;
    private UserService userService;
    private CalorieCountingService calorieService;
    private MealsInTakeService mealService;
    private SleepInTakeService sleepService;
    private MealsInTakeRepository mealsIntakeRepository;
    private SleepInTakeRepository sleepInTakeRepository;

    @BeforeEach
    void setUp() {
        userRepository = new InMemoryUserRepository();
        calorieService = new CalorieCountingService();
        userService = new UserService(userRepository);
        mealService = new MealsInTakeService(mealsIntakeRepository,new InMemoryWaterRepository());
        sleepService = new SleepInTakeService(sleepInTakeRepository);

        commandHandler = new CommandHandler(new Help(), new Menu(),
                calorieService, userService, mealService, sleepService);
    }


    @Test
    void testAddUserToRepository() {
        Command command = new Command("addUser Alice 19 171 58");
        MessageOutputData outputData = commandHandler.handleMessage(
                new MessageCommandData(command, 12345L));
        assertEquals("Отлично! Пользователь добавлен. Введи /help для справки "
                + "или /menu для выбора команд", outputData.messageData());
        User savedUser = userRepository.findById(12345L);
        assertNotNull(savedUser);
        assertEquals("Alice", savedUser.getName());
        assertEquals(19, savedUser.getAge());
        assertEquals(171, savedUser.getHeight());
        assertEquals(58, savedUser.getWeight());
    }

    @Test
    void testCalculateCalories() {
        userService.registerUser("Alice", "19", "171", "58", 12345L);
        Command command = new Command("/mycalories");
        MessageOutputData outputData = commandHandler.handleMessage(
                new MessageCommandData(command, 12345L));
        User alice = userRepository.findById(12345L);
        assertNotNull(alice);
        assertEquals(1392.75, alice.getCalories(), 0.01);
        assertEquals("Твоя норма калорий на день: " + alice.getCalories(),
                outputData.messageData());
    }


    @Test
    void testAddUserInvalidData() {
        Command command = new Command("addUser Alice 19 171");
        MessageOutputData outputData = commandHandler.handleMessage(
                new MessageCommandData(command, 12345L));
        assertNull(userRepository.findById(12345L));
        assertEquals("Неверное количество аргументов. "
                + "Используйте addUser [имя] [возраст] [рост] [вес]", outputData.messageData());
    }

    @Test
    void testInvalidParamAddUser() {
        Command command = new Command("addUser Alice 19 171 250");
        MessageOutputData outputData = commandHandler.handleMessage(
                new MessageCommandData(command, 12345L));
        assertNull(userRepository.findById(12345L));
        assertEquals("Параметр вес введён неверно.", outputData.messageData());
    }

    @Test
    void testInvalidCommand() {
        Command command = new Command("неизвестнаяКоманда");
        MessageOutputData outputData = commandHandler.handleMessage(
                new MessageCommandData(command, 12345L));
        assertEquals("Неверная команда", outputData.messageData());
    }


}

