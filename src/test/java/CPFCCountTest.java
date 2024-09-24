import org.example.CPFCCount;
import org.junit.Test;
import static org.junit.jupiter.api.Assertions.*;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import static org.junit.Assert.assertTrue;

public class CPFCCountTest {
    @Test
    public void countingTest(){
        CPFCCount test = new CPFCCount();
        test.counting(171, 58, 19);
        double result = test.getCalories();
        assertEquals(1392.75, result);
    }

}
