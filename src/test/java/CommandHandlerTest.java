import fitnesbot.bot.*;
import fitnesbot.models.User;
import fitnesbot.repositories.InMemoryUserRepository;
import fitnesbot.services.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class CommandHandlerTest {

    private CommandHandler commandHandler;
    private UserRepository userRepository;
    private UserService userService;
    private CalorieCountingService calorieService;

    @BeforeEach
    void setUp() {
        userRepository = new InMemoryUserRepository();
        calorieService = new CalorieCountingService();
        userService = new UserService(userRepository);
        commandHandler = new CommandHandler( new Help(), new Menu(), calorieService, userService);
    }



    @Test
    void testAddUserToRepository() {
        Command command = new Command("addUser Alice 19 171 58");
        MessageOutputData outputData = commandHandler.handleMessage(new MessageCommandData(command,12345L));
        User savedUser = userRepository.findById(12345L);
        assertNotNull(savedUser);
        assertEquals("Alice", savedUser.getName());
        assertEquals(19, savedUser.getAge());
        assertEquals(171, savedUser.getHeight());
        assertEquals(58, savedUser.getWeight());
        assertEquals("Отлично! Пользователь добавлен. Введи /help для справки или /menu для выбора команд",outputData.getMessageData());
    }

    @Test
    void testCalculateCalories() {
        userService.registerUser("Alice", "19", "171", "58", 12345L);
        Command command = new Command("/mycalories");
        MessageOutputData outputData = commandHandler.handleMessage(new MessageCommandData(command, 12345L));
        User alice = userRepository.findById(12345L);
        assertNotNull(alice);
        assertEquals(1392.75, alice.getCalories(), 0.01);
        assertEquals("Твоя норма калорий на день: " + alice.getCalories(), outputData.getMessageData());
    }


    @Test
    void testAddUserInvalidData() {
        Command command = new Command("addUser Alice 19 171");
        MessageOutputData outputData = commandHandler.handleMessage(new MessageCommandData(command, 12345L));
        assertNull(userRepository.findById(12345L));
        assertEquals("Неверное количество аргументов. Используйте addUser [имя] [возраст] [рост] [вес]", outputData.getMessageData());
    }

    @Test
    void testInvalidParamAddUser() {
        Command command = new Command("addUser Alice 19 171 250");
        MessageOutputData outputData = commandHandler.handleMessage(new MessageCommandData(command, 12345L));
        assertNull(userRepository.findById(12345L));
        assertEquals("Параметр вес введён неверно.", outputData.getMessageData());
    }

    @Test
    void testInvalidCommand() {
        Command command = new Command("неизвестнаяКоманда");
        MessageOutputData outputData = commandHandler.handleMessage(new MessageCommandData(command, 12345L));
        assertEquals("Неверная команда", outputData.getMessageData());
    }




}

