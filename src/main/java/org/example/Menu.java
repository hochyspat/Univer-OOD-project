package org.example;

public class Menu {
    public String getMenu() {
        return  "Выберите действие:\n" +
                "    КБЖУ;\n" +
                "    Дневник питания \n" +
                "    Составить меню на день;\n" +
                "    Найти тренировки;\n" +
                "    Настроить трекер воды;\n" +
                "    Записать свои параметры;\n" +
                "    addПользователь.\n" +
                "    Для выбора действия введите название действия.";
    }
    public void showMenu()
    {
        System.out.print(getMenu());
    }
}
