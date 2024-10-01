import org.example.CalorieCountingService;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
import java.io.*;
import java.util.*;

public class CPFCCountTest {
    @Test
    public void countingTest() {
        //CPFCCount test = new CPFCCount(System.in,System.out);
        //test.calculateCalories(171, 58, 19);
        //double result = test.getCalories();
        //assertEquals(1392.75, result);
    }

    @Test
    public void testStartValidInput() {
        ByteArrayOutputStream outContent = new ByteArrayOutputStream();
        System.setOut(new PrintStream(outContent));

        ByteArrayInputStream inContent = new ByteArrayInputStream("170\n70\n25\n".getBytes());
        System.setIn(inContent);

        //CPFCCount.start();

        String expectedOutput = "Твой рост в сантиметрах:\n" +
                "Твой вес в килограммах:\n" +
                "Твой возраст:\n" +
                "Ваша норма калорий в день: 1476.5";

        assertTrue(outContent.toString().trim().contains(expectedOutput));

        //assertEquals(1476.5, CPFCCount.getCalories(), 0.001);
    }

    @Test
    public void testStartInvalidInput() {


        String expectedOutput = "Введены неверные данные. Попробуй еще раз\n";

    }
}
