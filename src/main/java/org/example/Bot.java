package org.example;
import java.util.HashMap;
import java.io.InputStream;
import java.io.PrintStream;
import java.util.Scanner;

public class Bot {
    private Scanner scanner;
    private PrintStream out;
    private HashMap<String,User> users = new HashMap<>();
    public Bot(InputStream input, PrintStream out) {
        this.scanner = new Scanner(input);
        this.out = out;
    }
    public void start() {
        Help help = new Help();
        help.getHelp();
        while (true) {
            Scanner in = new Scanner(System.in);
            String userRequest = in.nextLine();
            switch (userRequest) {
                case "/help":
                    help.getHelp();
                    break;
                case "/menu":
                    Menu menu = new Menu();
                    menu.getMenu();
                    break;
                case "Рассчитать КБЖУ":
                    CalorieCountingService countedCalories = new CalorieCountingService();
                    countedCalories.startCalculate();
                    System.out.println("Твоя норма калорий на день:" + " " + countedCalories.getCalories());
                    break;
                case "Добавить пользователя":
                    setMyName();
                    break;
                case "/exit":
                    System.out.print("Finish bot");
                    break;
                default:
                    System.out.println("Извини, но я тебя не понял. Для выбора действия введите название действия.");
            }
            if (userRequest.equals("/exit")) {
                break;
            }
        }
    }
    private void setMyName(){
        System.out.print("как тебя звать?");
        String name = scanner.nextLine();
        System.out.print("рост пожалуйста в см");
        String height = scanner.nextLine();
        System.out.print("теперь что весы говорят в кг ");
        String weight = scanner.nextLine();
        System.out.print("сколько годиков");
        String age = scanner.nextLine();
        User user = new User(name, height, weight, age);
        users.put(name, user);

        System.out.println("Пользователь " + name + " успешно добавлен!");
    }
    private void getuser(String name){
        User user = users.get(name);
        if (user != null) {
            System.out.println(user.getInfo());
        }
        else {
            System.out.println("ERROR user not found");
        }
    }
}
