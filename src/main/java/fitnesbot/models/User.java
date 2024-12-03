
package fitnesbot.models;

public class User {
    private final long chatId;
    private final String name;
    private final int height;
    private final int weight;
    private final int age;
    private double calories;
    private WaterGoal waterGoal;
    private boolean deleted;

    public User(String name, int height, int weight, int age, long chatId) {
        this.name = name;
        this.height = height;
        this.weight = weight;
        this.age = age;
        this.chatId = chatId;
        this.deleted = false;
    }

    public void updateCalories(double calories) {
        this.calories = calories;
    }

    public String getName() {
        return name;
    }

    public int getHeight() {
        return height;
    }

    public int getWeight() {
        return weight;
    }

    public int getAge() {
        return age;
    }

    public double getCalories() {
        return calories;
    }

    public long getChatId() {
        return chatId;
    }

    public boolean isDeleted() {
        return deleted;
    }

    public void setDeleted(boolean deleted) {
        this.deleted = deleted;
    }

    public WaterGoal getWaterGoal() {
        return this.waterGoal;
    }

    public void setWaterGoal(WaterGoal waterGoal) {
        this.waterGoal = waterGoal;
    }

    public String getInfo() {
        return String.format(
                "Имя: %s%nРост: %d см%nВес: %d кг%nВозраст: %d лет%nЦель по воде: %d %s",
                name, height, weight, age, waterGoal.quantity(), waterGoal.units()
        );
    }
}
