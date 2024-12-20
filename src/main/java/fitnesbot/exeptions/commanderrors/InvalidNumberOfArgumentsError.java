package fitnesbot.exeptions.commanderrors;

public class InvalidNumberOfArgumentsError extends CommandErrors {
    public InvalidNumberOfArgumentsError(String methodName, String... args) {
        super("Неверное количество аргументов. Используйте " + methodName + " "
                + String.join(" ", args));
    }
}
