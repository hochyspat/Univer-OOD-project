import org.example.Help;
import org.example.Menu;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
import java.io.*;


public class HelpTest {
    @Test
    public void testGetHelp() {
        ByteArrayOutputStream outContent = new ByteArrayOutputStream();
        System.setOut(new PrintStream(outContent));
        Help test = new Help();
        test.GetHelp();
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
                "Для справки введите \"/help\"";
        assertEquals(expectedOutput, outContent.toString());
    }
}
