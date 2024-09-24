package org.example;

public class Menu {
    private static String menuText = "Выберите действие:\n" +
            "    Рассчитать КБЖУ;\n" +
            "    Дневник питания \n" +
            "    Составить меню на день;\n" +
            "    Найти тренировки;\n" +
            "    Настроить трекер воды;\n" +
            "    Записать свои параметры.\n" +
            "Для выбора действия введите название действия.";
    public static void GetMenu()
    {
        System.out.print(menuText);
    }
}
