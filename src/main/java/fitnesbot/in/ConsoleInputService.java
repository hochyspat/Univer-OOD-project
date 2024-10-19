package fitnesbot.in;
import java.util.Scanner;
import org.telegram.telegrambots.meta.api.objects.Update;

public class ConsoleInputService implements InputService{
    private final Scanner scanner = new Scanner(System.in);
    @Override
    public String read() {
        String s = scanner.nextLine();
        return s;
    }
}
