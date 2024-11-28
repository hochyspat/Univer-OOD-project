package fitnesbot.services;

public enum MealType {
    BREAKFAST("завтрак"),
    LUNCH("обед"),
    DINNER("ужин"),
    SNACK("перекус");

    private final String mealType;

    MealType(String mealType ) {
        this.mealType = mealType;
    }

    public String getMealType() {
        return mealType;
    }

    public static MealType fromString(String mealType) {
        for (MealType type : MealType.values()) {
            mealType = mealType.toLowerCase();
            if (type.getMealType().equals(mealType)){
                return type;
            }
            else {
                System.out.println("Incorrect MealType");
                return null;

            }
        }
        return null;
    }

}
