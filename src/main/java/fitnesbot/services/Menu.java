package fitnesbot.services;



public class Menu {
    public String getMenu() {
        return  """
        Выберите действие:
        addПользователь [имя] [возраст] [рост] [вес]
        "информация"
        "КБЖУ"
        Для выбора действия введите название действия.
        """;
    }
}
