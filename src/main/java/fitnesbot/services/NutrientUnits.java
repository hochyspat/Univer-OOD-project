package fitnesbot.services;

import com.fasterxml.jackson.annotation.JsonCreator;

public enum NutrientUnits {
    GRAM("g"),
    IU("iu"),
    MG("mg"),
    UG("ug"),
    L("l"),
    ML("ml"),
    CUP("cup"),
    KCAL("kcal"),
    PRESENT("%");

    private final String unit;

    NutrientUnits(String unit) {
        this.unit = unit;
    }

    public String getValue() {
        return unit;
    }

    @JsonCreator
    public static NutrientUnits fromString(String nutrientUnit) {
        for (NutrientUnits unit : values()) {
            nutrientUnit = nutrientUnit.toLowerCase();
            if (unit.getValue().equals(nutrientUnit)) {
                return unit;
            }
        }
        throw new IllegalStateException("Unexpected value: " + nutrientUnit);

    }

}
