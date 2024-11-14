package fitnesbot.services;

public enum MealType {
    BREAKFAST,
    LUNCH,
    DINNER,
    SNACK,
    INTAKE;

    public static MealType fromString(String mealType) {

        return switch (mealType.toUpperCase()) {
            case "BREAKFAST" -> BREAKFAST;
            case "LUNCH" -> LUNCH;
            case "DINNER" -> DINNER;
            case "SNACK" -> SNACK;
            default -> INTAKE;
        };

    }


}
