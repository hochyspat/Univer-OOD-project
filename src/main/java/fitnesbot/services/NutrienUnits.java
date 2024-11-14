package fitnesbot.services;

public enum NutrienUnits {
    GRAM,
    MG,
    ML,
    L,
    CUP;

    public static NutrienUnits fromString(String nutrinentUnit) {
        return switch (nutrinentUnit.toUpperCase()) {
            case "GRAM" -> GRAM;
            case "MG" -> MG;
            case "ML" -> ML;
            case "L" -> L;
            case "CUP" -> CUP;
            default -> throw new IllegalStateException("Unexpected value: " + nutrinentUnit.toUpperCase());
        };

    }

}
