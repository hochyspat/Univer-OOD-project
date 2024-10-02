import org.example.*;
import org.junit.jupiter.api.BeforeEach;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class BotTest {
    private Bot bot;
    @Mock
    private Help mockHelp;

    @Mock
    private Menu mockMenu;

    @Mock
    private CalorieCountingService mockCaloriesService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        bot = new Bot();
        Menu menu = mockMenu;
        Help help = mockHelp;
        CalorieCountingService caloriesService = mockCaloriesService;
    }

    @Test
    void testAddUser() {
        bot.setUser("Alice", 171, 58, 19);
        User user = bot.getUserByName("Alice");
        assertNotNull(user);
        assertEquals("Alice", user.getName());
        assertEquals(171, user.getHeight());
        assertEquals(58, user.getWeight());
        assertEquals(19, user.getAge());
    }
    @Test
    void testCalculateCalories() {
        bot.setUser("Alice", 171, 58, 19);
        when(mockCaloriesService.calculate(171,58,19)).thenReturn(1476.5);
        bot.executeCommand(new Command("КБЖУ Alice"),mockHelp,mockMenu);
        User alice = bot.getUserByName("Alice");
        assertNotNull(alice);
        assertEquals(1392.75,alice.getCalories(),0.01);
    }




}

