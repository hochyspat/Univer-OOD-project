
package fitnesbot.models;

public class User {
    private long chatId;
    private String name;
    private int height;
    private int weight;
    private int age;
    private double calories;
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


    public String getInfo() {
        return "Имя: " + name + "\nРост: " + height + " см\nВес: " + weight + " кг\nВозраст: " + age + " лет";
    }
}
