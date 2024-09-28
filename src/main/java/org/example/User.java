package org.example;

public record User(String name, String age, String height, String weight) {

    public void showInfoUser(){
        System.out.println(getInfo());
    }

    private String getInfo() {
        return "Имя: " + name + "\nРост: " + height + " см\nВес: " + weight + " кг\nВозраст: " + age + " лет";
    }
}