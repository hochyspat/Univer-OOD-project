import fitnesbot.bot.*;
import fitnesbot.in.ConsoleInputService;
import fitnesbot.in.InputService;
import fitnesbot.models.User;
import fitnesbot.repositories.InMemoryUserRepository;
import fitnesbot.services.*;
import fitnesbot.out.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class CommandHandlerTest {

    private CommandHandler commandHandler;
    private UserRepository userRepository;
    private UserService userService;
    private CalorieCountingService calorieService;
    private OutputService outputService;

    @BeforeEach
    void setUp() {
        userRepository = new InMemoryUserRepository();
        outputService = new ConsoleOutputService();
        calorieService = new CalorieCountingService();
        userService = new UserService(userRepository, outputService);
        commandHandler = new CommandHandler( new Help(), new Menu(), calorieService, userService);
    }



    @Test
    void testAddUserToRepository() {
        Command command = new Command("addПользователь Alice 19 171 58");
        commandHandler.handleMessage(new MessageCommandData(command,12345L));
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
        Command command = new Command("КБЖУ");
        commandHandler.handleMessage(new MessageCommandData(command,12345L));
        User alice = userRepository.findById(12345L);
        assertNotNull(alice);
        assertEquals(1392.75, alice.getCalories(), 0.01);
    }


    @Test
    void testAddUserInvalidData() {
        Command command = new Command("addПользователь Alice 19 171");
        commandHandler.handleMessage(new MessageCommandData(command,12345L));
        User savedUser = userRepository.findById(12345L);
        assertNull(savedUser);
    }

    @Test
    void testInvalidParamAddUser() {
        Command command = new Command("addПользователь Alice 19 171 250");
        commandHandler.handleMessage(new MessageCommandData(command,12345L));
        User savedUser = userRepository.findById(12345L);
        assertNull(savedUser);
    }

    @Test
    void testInvalidCommand() {
        Command command = new Command("неизвестнаяКоманда");
        commandHandler.handleMessage(new MessageCommandData(command,12345L));
    }




}

