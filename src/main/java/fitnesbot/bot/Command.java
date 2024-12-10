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
            "addMeal",
            "learnMeal",
            "getMeal",
            "addWaterGoal",
            "addWaterInTake",
            "getWaterInTakeInfo",
            "getWeekSleepStat",
            "getDaySleepStat",
            "addSleepGoal",
            "addSleep",
            "addTraining",
            "getTrainings",
            "getTrainingsByDate",
            "deleteTraining"
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
            String[] parts = commandData.substring(
                    commandData.indexOf(' ') + 1).split(" ", 2);
            String[] argsMeal = parts[1].split(",\\s*");
            String[] result = new String[1 + argsMeal.length];
            result[0] = parts[0];
            System.arraycopy(argsMeal, 0, result, 1, argsMeal.length);
            return result;
        } else if (commandData.contains(" ")) {
            return commandData.substring(commandData.indexOf(' ') + 1).trim().split(
                    "\\s+");
        } else {
            return new String[0];
        }

    }

    public boolean isExit() {
        return "/exit".equals(command);
    }

}
