import fitnesbot.services.Menu;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class MenuTest {

    @Test
    public void testGetMenu() {

        Menu testMenu = new Menu();
        String expectedOutput = """
            Выберите действие:
            addUser [имя] [возраст] [рост] [вес]
            "/myprofile"
            "/mycalories"
            Для выбора действия введите название действия.
            """;

        assertEquals(testMenu.getMenu(), expectedOutput);
    }
}
