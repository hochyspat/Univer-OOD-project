import fitnesbot.services.Menu;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class MenuTest {

    @Test
    public void testGetMenu() {

        Menu testMenu = new Menu();
        String expectedOutput = """
        Выберите действие:
            КБЖУ;
            Дневник питания;
            Составить меню на день;
            Найти тренировки;
            Настроить трекер воды;
            Записать свои параметры;
            addПользователь.
            Для выбора действия введите название действия.
        """;

        assertEquals(testMenu.getMenu(),expectedOutput);
    }
}
