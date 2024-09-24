import org.example.Menu;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
import java.io.*;

public class MenuTest {

    @Test
    public void testGetMenu() {
        ByteArrayOutputStream outContent = new ByteArrayOutputStream();
        System.setOut(new PrintStream(outContent));
        Menu test = new Menu();

        test.GetMenu();

        String expectedOutput = "Выберите действие:\n" +
                "    Рассчитать КБЖУ;\n" +
                "    Дневник питания \n" +
                "    Составить меню на день;\n" +
                "    Найти тренировки;\n" +
                "    Настроить трекер воды;\n" +
                "    Записать свои параметры.\n" +
                "Для выбора действия введите название действия.";

        assertEquals(expectedOutput, outContent.toString());
    }
}
