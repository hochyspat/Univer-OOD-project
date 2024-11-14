package fitnesbot.models;

import fitnesbot.services.NutrienUnits;

public class Nutrient {
    private NutrienUnits unit;
    private double quantity;
    private String label;

    public Nutrient(String unit, double quantity, String label) {
        this.unit = NutrienUnits.fromString(unit);
        this.quantity = quantity;
        this.label = label;
    }

    public double getQuantity() {
        return quantity;
    }

    public String getLabel() {
        return label;
    }

    public NutrienUnits getUnit() {
        return unit;
    }

    public String showNutrient() {
        return quantity + " " + unit + " " + label;
    }
}
