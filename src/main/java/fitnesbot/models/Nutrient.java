package fitnesbot.models;

public class Nutrient {
    private String unit;
    private double quantity;
    private String label;

    public Nutrient(String unit, double quantity, String label) {
        this.unit = unit;
        this.quantity = quantity;
        this.label = label;
    }

    public double getQuantity() {
        return quantity;
    }
}
