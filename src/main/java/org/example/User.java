package org.example;

public class User {
    private String name;
    private String age;
    private String height;
    private String weight;

    public User(String name, String age, String height, String weight) {
        this.name = name;
        this.age = age;
        this.height = height;
        this.weight = weight;
    }
    public String getName() {
        return name;
    }

    public String getHeight() {
        return height;
    }

    public String getWeight() {
        return weight;
    }

    public String getAge() {
        return age;
    }
    public String getInfo() {
        return "Имя: " + name + "\nРост: " + height + " см\nВес: " + weight + " кг\nВозраст: " + age + " лет";
    }


}