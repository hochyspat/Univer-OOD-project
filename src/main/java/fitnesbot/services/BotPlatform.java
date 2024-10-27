package fitnesbot.services;

public enum BotPlatform {
    CONSOLE,
    TELEGRAM,
    BOTH;

    public static BotPlatform fromString(String platform) {
        return switch (platform.toUpperCase()) {
            case "TELEGRAM" -> TELEGRAM;
            case "BOTH" -> BOTH;
            default -> CONSOLE;
        };
    }
}
