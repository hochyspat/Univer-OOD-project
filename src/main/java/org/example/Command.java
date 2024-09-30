package org.example;

import java.util.Set;

public record Command(String command, String[] args) {

    private static Set<String> validCommands = Set.of(
            "/help",
            "/menu",
            "Рассчитать КБЖУ",
            "Добавить пользователя",
            "информация",
            "/exit"
    );
    public boolean isValid() {
        return validCommands.contains(command);
    }
    public Command(String commandData) {
        this(commandData.split(" ")[0],
                (commandData.contains(" ") ? commandData.substring(commandData.indexOf(' ') + 1).split(" ") : new String[0]));
    }
    public boolean isExit() {
        return "/exit".equals(command);
    }




}
