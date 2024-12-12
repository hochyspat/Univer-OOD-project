package fitnesbot.models;

import com.fasterxml.jackson.annotation.JsonProperty;
import fitnesbot.services.enums.NutrientUnits;


public class Nutrient {
    private NutrientUnits unit;
    private double quantity;
    private String label;

    public Nutrient(String unit, double quantity, String label) {
        this.unit = NutrientUnits.fromString(unit);
        this.quantity = quantity;
        this.label = label;
    }

    public Nutrient() {
    }

    @JsonProperty("quantity")
    public double getQuantity() {
        return quantity;
    }

    @JsonProperty("label")
    public String getLabel() {
        return label;
    }

    @JsonProperty("unit")
    public NutrientUnits getUnit() {
        return unit;
    }


    public void setQuantity(double quantity) {
        this.quantity = quantity;
    }

    public void setLabel(String label) {
        this.label = label;
    }

    public void setUnit(NutrientUnits unit) {
        this.unit = unit;
    }

    public String showNutrient() {
        return quantity + " " + unit + " " + label;
    }
}
