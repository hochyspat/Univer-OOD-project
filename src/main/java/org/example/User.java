package org.example;

public record User(String name, int height, int weight, int age) {

    public void showUserInfo(){
        System.out.println(getInfo());
    }

    private String getInfo() {
        return "Имя: " + name + "\nРост: " + height + " см\nВес: " + weight + " кг\nВозраст: " + age + " лет";
    }
}