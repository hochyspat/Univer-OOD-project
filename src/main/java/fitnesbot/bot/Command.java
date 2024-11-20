package fitnesbot.bot;

import java.util.Set;

public record Command(String command, String[] args) {

    private static final Set<String> validCommands = Set.of(
            "/help",
            "/menu",
            "/mycalories",
            "addUser",
            "/myprofile",
            "/exit",
            "addMeals"
    );

    public boolean isValid() {
        return validCommands.contains(command);
    }

    public Command(String commandData) {
        this(commandData.split(" ")[0],
                parserArguments(commandData));
    }

    private static String[] parserArguments(String commandData) {
        if (commandData.contains(",")) {
            return commandData.substring(commandData.indexOf(' ') + 1)
                    .trim().split(",\\s*");
        }
        else if (commandData.contains(" ")) {
            return commandData.substring(commandData.indexOf(' ') + 1)
                    .trim().split("\\s+");
        } else {
            return new String[0];
        }

    }

    public boolean isExit() {
        return "/exit".equals(command);
    }

}
