import org.example.*;
import org.junit.jupiter.api.BeforeEach;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class HelpTest {
    @Test
    public void testGetHelp() {


        String expectedOutput = "Привет! Меня зовут *название бота*. Я буду помогать тебе при похудении и не только. С моей помощью ты сможешь:\n" +
                "    •рассчитать свои нормы КБЖУ и создать собственный дневник питания;\n" +
                "    •составить меню на день;\n" +
                "    •найти интересующие тебя тренировки;\n" +
                "    •напоминать пить воду;\n" +
                "    •записывать свои параметры на протяжении всего пути;\n" +
                "    •не потерять мотивацию.\n" +
                "Также в конце недели я буду подводить итоги, тем самым отслеживая твой прогресс.\n" +
                "\n" +
                "Чтобы выйти в меню введите \"/menu\"" +
                "\n" +
                "Для справки введите \"/help\n";

    }
}
