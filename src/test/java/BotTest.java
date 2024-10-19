
import fitnesbot.bot.*;
import fitnesbot.in.InputService;
import fitnesbot.models.User;
import fitnesbot.services.*;
import fitnesbot.out.*;
import org.junit.jupiter.api.BeforeEach;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class BotTest {

    @InjectMocks
    private ConsoleBot bot;

    @Mock
    private Help mockHelp;

    @Mock
    private Menu mockMenu;

    @Mock
    private CalorieCountingService mockCaloriesService;

    @Mock
    private InputService mockInputService;

    @Mock
    private OutputService mockOutputService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        Menu menu = mockMenu;
        Help help = mockHelp;

        CalorieCountingService caloriesService = mockCaloriesService;
    }

    @Test
    void testAddUser() {
        bot.setUser("Alice", "19", "171", "58",1234);
        User user = bot.("Alice");
        assertNotNull(user);
        assertEquals("Alice", user.getName());
        assertEquals(171, user.getHeight());
        assertEquals(58, user.getWeight());
        assertEquals(19, user.getAge());
    }
    @Test
    void testCalculateCalories() {
        bot.setUser("Alice", 171, 58, 19);
        when(mockCaloriesService.calculate(171,58,19)).thenReturn(1392.75);
        bot.executeCommand(new Command("КБЖУ Alice"));
        User alice = bot.getUserByName("Alice");
        System.out.println("Calories returned by mock: " + alice.getCalories());
        assertNotNull(alice);
        assertEquals(1392.75,alice.getCalories(),0.01);
    }




}

