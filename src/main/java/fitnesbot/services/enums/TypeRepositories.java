package fitnesbot.services.enums;

public enum TypeRepositories {
    IN_MEMORY,
    DATABASE;

    public static TypeRepositories fromString(String platform) {
        return switch (platform.toUpperCase()) {
            case "IN_MEMORY" -> IN_MEMORY;
            case "DATABASE" -> DATABASE;
            default -> IN_MEMORY;
        };
    }

}
