package fitnesbot.InOut;

public class ConsoleOutputService implements OutputService {

    @Override
    public void output(String message) {
        System.out.println(message);
    }

}
