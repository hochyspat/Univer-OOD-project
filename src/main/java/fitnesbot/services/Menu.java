package fitnesbot.services;


public class Menu {
    public String getMenu() {
        return """
            Выберите действие:
            addUser [имя] [возраст] [рост] [вес]
            "/myprofile"
            "/mycalories"
            Для выбора действия введите название действия.
            """;
    }
}
