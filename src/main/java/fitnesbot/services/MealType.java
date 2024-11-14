package fitnesbot.services;

public enum MealType {
    BREAKFAST,
    LUNCH,
    DINNER,
    SNACK,
    INTAKE;

    public static MealType fromString(String mealType) {
        for (MealType type : MealType.values()) {
            if (type.name().equalsIgnoreCase(mealType)) {
                return type;
            }
        }
        throw new IllegalStateException("Unexpected value: " + mealType);
    }

}
