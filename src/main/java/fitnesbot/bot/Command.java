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

    public String parseArgsInfo() {
        int firstSpaceIndex = args[0].indexOf(" ");
        return args[0].substring(0, firstSpaceIndex);
    }

    public boolean isValid() {
        return validCommands.contains(command);
    }

    public Command(String commandData) {
        this(commandData.split(" ")[0],
                parserArguments(commandData));
    }

    private static String[] parserArguments(String commandData) {
        if (commandData.contains(",")) {
            return commandData.substring(commandData.indexOf(' ') + 1).trim().split(
                    ",\\s*");
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
