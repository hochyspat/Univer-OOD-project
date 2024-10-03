import org.example.*;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class MenuTest {

    @Test
    public void testGetMenu() {

        Menu testMenu = new Menu();
        String expectedOutput = "Выберите действие:\n" +
                "    КБЖУ;\n" +
                "    Дневник питания \n" +
                "    Составить меню на день;\n" +
                "    Найти тренировки;\n" +
                "    Настроить трекер воды;\n" +
                "    Записать свои параметры;\n" +
                "    addПользователь.\n" +
                "    Для выбора действия введите название действия.";

        assertEquals(testMenu.getMenu(),expectedOutput);
    }
}
