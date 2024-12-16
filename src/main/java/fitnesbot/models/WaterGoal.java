package fitnesbot.models;

import fitnesbot.services.enums.NutrientUnits;

public record WaterGoal(double quantity, NutrientUnits units) {
}
