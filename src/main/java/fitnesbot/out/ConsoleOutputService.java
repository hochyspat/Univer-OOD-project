package fitnesbot.out;

public class ConsoleOutputService implements OutputService {

    @Override
    public void output(String message) {
        System.out.println(message);
    }

}
