package fitnesbot.exeptions.databaseerrors;

public class DriverNotFoundException extends DataBaseError {

    public DriverNotFoundException() {
        super("\"Драйвер PostgreSQL не найден: \"");
    }
}
