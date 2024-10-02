import org.example.*;
import org.junit.jupiter.api.BeforeEach;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class MenuTest {

    @Test
    public void testGetMenu() {



        String expectedOutput = "Выберите действие:\n" +
                "    Рассчитать КБЖУ;\n" +
                "    Дневник питания \n" +
                "    Составить меню на день;\n" +
                "    Найти тренировки;\n" +
                "    Настроить трекер воды;\n" +
                "    Записать свои параметры;\n" +
                "Добавить пользователя.\n" +
                "Для выбора действия введите название действия.";


    }
}
