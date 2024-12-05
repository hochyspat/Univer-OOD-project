import fitnesbot.services.Help;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class HelpTest {

    @Test
    public void testGetHelp() {

        Help testHelp = new Help();
        String expectedOutput = """
                Привет! Меня зовут FitExpert. Я буду помогать тебе при похудении и не только.
                С моей помощью ты сможешь:
                    •рассчитать свои нормы КБЖУ и создать собственный дневник питания;
                    •составить меню на день;
                    •найти интересующие тебя тренировки;
                    •напоминать пить воду;
                    •записывать свои параметры на протяжении всего пути;
                    •не потерять мотивацию.
                Также в конце недели я буду подводить итоги, тем самым отслеживая твой прогресс.
                
                Чтобы выйти в меню введите "/menu"
                Для справки введите "/help"
                """;


        assertEquals(testHelp.getHelp(), expectedOutput);
    }
}
