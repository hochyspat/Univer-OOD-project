package fitnesbot.InOut;
import java.util.Scanner;

public class ConsoleInputService implements InputService {
    private Scanner scanner = new Scanner(System.in);
    @Override
    public String read(String prompt) {
        System.out.println(prompt);
        String s = scanner.nextLine();
        return s;
    }
}
