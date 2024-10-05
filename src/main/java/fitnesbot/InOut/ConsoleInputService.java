package fitnesbot.InOut;
import java.util.Scanner;

public class ConsoleInputService implements InputService {
    private final Scanner scanner = new Scanner(System.in);
    @Override
    public String read() {
        String s = scanner.nextLine();
        return s;
    }
}
